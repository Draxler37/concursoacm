package com.concursoacm.application.services;

import com.concursoacm.application.dtos.preguntas.PreguntaDetalleDTO;
import com.concursoacm.application.dtos.preguntas.PreguntasAsignadasDetalleDTO;
import com.concursoacm.domain.models.Equipo;
import com.concursoacm.domain.models.JefeDelegacion;
import com.concursoacm.domain.models.Pregunta;
import com.concursoacm.domain.models.PreguntasAsignadas;
import com.concursoacm.domain.services.IPreguntasAsignadasService;
import com.concursoacm.infrastructure.repositories.EquipoRepository;
import com.concursoacm.infrastructure.repositories.JefeDelegacionRepository;
import com.concursoacm.infrastructure.repositories.PreguntaRepository;
import com.concursoacm.infrastructure.repositories.PreguntasAsignadasRepository;
import com.concursoacm.infrastructure.utils.Constantes;

import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.List;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de preguntas
 * *asignadas.
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
        List<Pregunta> seleccion5Preguntas = preguntasDisponibles.subList(0, 5);

        PreguntasAsignadas asignacion = new PreguntasAsignadas();
        asignacion.setEquipo(equipo);
        asignacion.setPregunta1(seleccion5Preguntas.get(0).getIdPregunta());
        asignacion.setPregunta2(seleccion5Preguntas.get(1).getIdPregunta());
        asignacion.setPregunta3(seleccion5Preguntas.get(2).getIdPregunta());
        asignacion.setPregunta4(seleccion5Preguntas.get(3).getIdPregunta());
        asignacion.setPregunta5(seleccion5Preguntas.get(4).getIdPregunta());

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
            case Constantes.CATEGORIA_COMPETENCIA:
                return Constantes.CLASE_A;
            case Constantes.CATEGORIA_JUNIOR:
                return Constantes.CLASE_B;
            default:
                throw new IllegalArgumentException(Constantes.ERROR_CATEGORIA_INVALIDA);
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
    public PreguntasAsignadasDetalleDTO getPreguntasAsignadasAlEquipo(int idEquipo, String usuarioNormalizado) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));

        JefeDelegacion jefe = jefeDelegacionRepository.findByUsuarioNormalizado(usuarioNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Jefe de delegación no encontrado."));

        if (jefe.getParticipante().getPais().getIdPais() != equipo.getPais().getIdPais()) {
            throw new AccessDeniedException("No estás autorizado para ver las preguntas de este equipo.");
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
