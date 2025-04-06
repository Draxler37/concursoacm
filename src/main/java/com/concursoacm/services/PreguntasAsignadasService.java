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
import com.concursoacm.services.interfaces.IPreguntasAsignadasService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de preguntas
 * asignadas.
 */
@Service
public class PreguntasAsignadasService implements IPreguntasAsignadasService {

    private final PreguntaRepository preguntaRepository;
    private final PreguntasAsignadasRepository preguntasAsignadasRepository;
    private final EquipoRepository equipoRepository;
    private final JefeDelegacionRepository jefeDelegacionRepository;

    /**
     * *Constructor que inyecta las dependencias necesarias.
     *
     * @param preguntaRepository           Repositorio para la gestión de preguntas.
     * @param preguntasAsignadasRepository Repositorio para la gestión de preguntas
     *                                     asignadas.
     * @param equipoRepository             Repositorio para la gestión de equipos.
     * @param jefeDelegacionRepository     Repositorio para la gestión de jefes de
     *                                     delegación.
     */
    public PreguntasAsignadasService(PreguntaRepository preguntaRepository,
            PreguntasAsignadasRepository preguntasAsignadasRepository,
            EquipoRepository equipoRepository,
            JefeDelegacionRepository jefeDelegacionRepository) {
        this.preguntaRepository = preguntaRepository;
        this.preguntasAsignadasRepository = preguntasAsignadasRepository;
        this.equipoRepository = equipoRepository;
        this.jefeDelegacionRepository = jefeDelegacionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asignarPreguntasATodosLosEquipos() {
        List<Equipo> equipos = equipoRepository.findAll();
        equipos.stream()
                .filter(equipo -> preguntasAsignadasRepository.findByEquipoIdEquipo(equipo.getIdEquipo()).isEmpty())
                .forEach(this::asignarPreguntasAEquipo);
    }

    /**
     * *Asigna preguntas a un equipo específico.
     *
     * @param equipo Objeto Equipo.
     */
    private void asignarPreguntasAEquipo(Equipo equipo) {
        String claseRequerida = determinarClasePorCategoria(equipo.getCategoria());
        List<Pregunta> preguntasDisponibles = obtenerPreguntasDisponibles(claseRequerida);

        if (preguntasDisponibles.size() < 25) {
            throw new IllegalArgumentException(
                    "No hay suficientes preguntas disponibles para asignar al equipo " + equipo.getNombreEquipo());
        }

        Collections.shuffle(preguntasDisponibles);
        List<Pregunta> seleccion5 = preguntasDisponibles.subList(0, 5);

        PreguntasAsignadas asignacion = new PreguntasAsignadas();
        asignacion.setEquipo(equipo);
        asignacion.setPregunta1(seleccion5.get(0).getIdPregunta());
        asignacion.setPregunta2(seleccion5.get(1).getIdPregunta());
        asignacion.setPregunta3(seleccion5.get(2).getIdPregunta());
        asignacion.setPregunta4(seleccion5.get(3).getIdPregunta());
        asignacion.setPregunta5(seleccion5.get(4).getIdPregunta());

        preguntasAsignadasRepository.save(asignacion);
    }

    /**
     * *Determina la clase de preguntas según la categoría del equipo.
     *
     * @param categoria Categoría del equipo.
     * @return Clase de preguntas requerida.
     */
    private String determinarClasePorCategoria(String categoria) {
        switch (categoria.toLowerCase()) {
            case "competencia":
                return "A";
            case "junior":
                return "B";
            default:
                throw new IllegalArgumentException("Categoría de equipo no válida.");
        }
    }

    /**
     * *Obtiene las preguntas disponibles de una clase específica.
     *
     * @param clase Clase de preguntas requerida.
     * @return Lista de preguntas disponibles.
     */
    private List<Pregunta> obtenerPreguntasDisponibles(String clase) {
        return preguntaRepository.findByClaseAndUsadaFalse(clase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreguntasAsignadasDetalleDTO obtenerDetallesPreguntasAsignadas(int idEquipo, String usuarioNormalizado) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));

        JefeDelegacion jefe = jefeDelegacionRepository.findByUsuarioNormalizado(usuarioNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Jefe de delegación no encontrado."));

        if (jefe.getParticipante().getPais().getIdPais() != equipo.getPais().getIdPais()) {
            throw new SecurityException("No estás autorizado para ver las preguntas de este equipo.");
        }

        PreguntasAsignadas asignacion = preguntasAsignadasRepository.findByEquipoIdEquipo(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("No hay preguntas asignadas para este equipo."));

        List<PreguntaDetalleDTO> detalles = List.of(
                obtenerDetallePregunta(asignacion.getPregunta1()),
                obtenerDetallePregunta(asignacion.getPregunta2()),
                obtenerDetallePregunta(asignacion.getPregunta3()),
                obtenerDetallePregunta(asignacion.getPregunta4()),
                obtenerDetallePregunta(asignacion.getPregunta5()));

        return new PreguntasAsignadasDetalleDTO(equipo.getNombreEquipo(), detalles);
    }

    /**
     * *Obtiene los detalles de una pregunta por su ID.
     *
     * @param idPregunta ID de la pregunta.
     * @return DTO con los detalles de la pregunta.
     */
    private PreguntaDetalleDTO obtenerDetallePregunta(int idPregunta) {
        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta con ID " + idPregunta + " no encontrada."));
        return new PreguntaDetalleDTO(pregunta.getIdPregunta(), pregunta.getTexto());
    }
}
