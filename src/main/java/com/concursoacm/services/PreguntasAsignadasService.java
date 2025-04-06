package com.concursoacm.services;

import com.concursoacm.dtos.PreguntaDetalleDTO;
import com.concursoacm.dtos.PreguntasAsignadasDetalleDTO;
import com.concursoacm.models.Equipo;
import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.models.Pregunta;
import com.concursoacm.models.PreguntasAsignadas;
import com.concursoacm.repositories.EquipoRepository;
import com.concursoacm.repositories.JefeDelegacionRepository;
import com.concursoacm.repositories.PreguntaRepository;
import com.concursoacm.repositories.PreguntasAsignadasRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PreguntasAsignadasService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private PreguntasAsignadasRepository preguntasAsignadasRepository;
    
    @Autowired
    private EquipoRepository equipoRepository;
    
    @Autowired
    private JefeDelegacionRepository jefeDelegacionRepository;

    // Asignar preguntas a TODOS los equipos sin asignación previa
    public void asignarPreguntasATodosLosEquipos() {
        // Obtener todos los equipos
        List<Equipo> equipos = equipoRepository.findAll();
        for (Equipo equipo : equipos) {
            // Solo asignar si aún no hay registro de preguntas asignadas para este equipo
            if (preguntasAsignadasRepository.findByEquipoIdEquipo(equipo.getIdEquipo()).isEmpty()) {
                asignarPreguntasAEquipo(equipo);
            }
        }
    }
    
    private PreguntasAsignadas asignarPreguntasAEquipo(Equipo equipo) {
        // Determinar la clase requerida según la categoría del equipo
        String claseRequerida;
        if (equipo.getCategoria().equalsIgnoreCase("Competencia")) {
            claseRequerida = "A";
        } else if (equipo.getCategoria().equalsIgnoreCase("Junior")) {
            claseRequerida = "B";
        } else {
            throw new IllegalArgumentException("Categoría de equipo no válida.");
        }
        
        // Obtener todas las preguntas disponibles de la clase requerida que no hayan sido usadas
        List<Pregunta> preguntasDisponibles = preguntaRepository.findAll();
        preguntasDisponibles.removeIf(p -> !p.getClase().toString().equalsIgnoreCase(claseRequerida) || p.isUsada());
        
        System.out.println(preguntasDisponibles);
        System.out.println(preguntasDisponibles.size());
        // Si hay menos de 25 preguntas disponibles, lanzar error
        if (preguntasDisponibles.size() < 25) {
            throw new IllegalArgumentException("No hay suficientes preguntas disponibles para asignar al equipo " + equipo.getNombreEquipo());
        }
        
        // Barajar la lista y tomar 25 preguntas aleatorias
        Collections.shuffle(preguntasDisponibles);
        List<Pregunta> seleccion25 = preguntasDisponibles.subList(0, 25);
        
        // Barajar el subconjunto y seleccionar 5 preguntas definitivas
        Collections.shuffle(seleccion25);
        List<Pregunta> seleccion5 = seleccion25.subList(0, 5);
        
        PreguntasAsignadas asignacion = new PreguntasAsignadas();
        asignacion.setEquipo(equipo);
        asignacion.setPregunta1(seleccion5.get(0).getIdPregunta());
        asignacion.setPregunta2(seleccion5.get(1).getIdPregunta());
        asignacion.setPregunta3(seleccion5.get(2).getIdPregunta());
        asignacion.setPregunta4(seleccion5.get(3).getIdPregunta());
        asignacion.setPregunta5(seleccion5.get(4).getIdPregunta());
        
        return preguntasAsignadasRepository.save(asignacion);
    }
    
    // Obtener la asignación de preguntas para un equipo, por su idEquipo
    public PreguntasAsignadasDetalleDTO obtenerDetallesPreguntasAsignadas(int idEquipo, String usuarioNormalizado) {
        // Obtener el equipo
        Equipo equipo = equipoRepository.findById(idEquipo)
            .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));

        // Obtener el jefe de delegación autenticado
        JefeDelegacion jefe = jefeDelegacionRepository.findByUsuarioNormalizado(usuarioNormalizado)
            .orElseThrow(() -> new IllegalArgumentException("Jefe de delegación no encontrado."));
        
        // Verificar autorización: el jefe solo puede consultar equipos de su país.
        if (jefe.getParticipante().getPais().getIdPais() != equipo.getPais().getIdPais()) {
            throw new SecurityException("No estás autorizado para ver las preguntas de este equipo.");
        }
        
        // Obtener la asignación de preguntas para este equipo
        PreguntasAsignadas asignacion = preguntasAsignadasRepository.findByEquipoIdEquipo(idEquipo)
            .orElseThrow(() -> new IllegalArgumentException("No hay preguntas asignadas para este equipo."));
        
        // Recoger los 5 id de preguntas
        int[] ids = new int[] {
            asignacion.getPregunta1(),
            asignacion.getPregunta2(),
            asignacion.getPregunta3(),
            asignacion.getPregunta4(),
            asignacion.getPregunta5()
        };
        
        List<PreguntaDetalleDTO> detalles = new ArrayList<>();
        for (int pid : ids) {
            Pregunta preg = preguntaRepository.findById(pid)
                    .orElseThrow(() -> new IllegalArgumentException("Pregunta con id " + pid + " no encontrada."));
            detalles.add(new PreguntaDetalleDTO(preg.getIdPregunta(), preg.getTexto()));
        }
        
        // Retornar DTO con el nombre del equipo y la lista de preguntas
        return new PreguntasAsignadasDetalleDTO(equipo.getNombreEquipo(), detalles);
    }
}


