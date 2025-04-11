package com.concursoacm.application.services;

import com.concursoacm.domain.models.Participante;
import com.concursoacm.domain.models.PreguntasAsignadas;
import com.concursoacm.domain.models.Respuesta;
import com.concursoacm.domain.services.IRespuestaService;
import com.concursoacm.infrastructure.repositories.ParticipanteRepository;
import com.concursoacm.infrastructure.repositories.PreguntasAsignadasRepository;
import com.concursoacm.infrastructure.repositories.RespuestaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import com.concursoacm.application.dtos.ActualizarNotaDTO;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de respuestas.
 */
@Service
public class RespuestaService implements IRespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final ParticipanteRepository participanteRepository;
    private final PreguntasAsignadasRepository preguntasAsignadasRepository;

    /**
     * *Constructor que inyecta las dependencias necesarias.
     *
     * @param respuestaRepository          Repositorio para la gestión de
     *                                     respuestas.
     * @param participanteRepository       Repositorio para la gestión de
     *                                     participantes.
     * @param preguntasAsignadasRepository Repositorio para la gestión de preguntas
     *                                     asignadas.
     */
    public RespuestaService(RespuestaRepository respuestaRepository,
            ParticipanteRepository participanteRepository,
            PreguntasAsignadasRepository preguntasAsignadasRepository) {
        this.respuestaRepository = respuestaRepository;
        this.participanteRepository = participanteRepository;
        this.preguntasAsignadasRepository = preguntasAsignadasRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Respuesta crearRespuesta(int idParticipante, int idPregunta, String respuestaText) {
        Participante participante = validarParticipante(idParticipante);
        validarPreguntaAsignada(participante, idPregunta);

        Respuesta respuesta = new Respuesta();
        respuesta.setIdParticipante(idParticipante);
        respuesta.setIdEquipo(participante.getEquipo().getIdEquipo());
        respuesta.setIdPregunta(idPregunta);
        respuesta.setRespuestaParticipante(respuestaText);
        respuesta.setPuntuacionObtenida(0); // Inicialmente 0

        return respuestaRepository.save(respuesta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Respuesta> getRespuestasDelParticipante(int idParticipante) {
        return respuestaRepository.findByIdParticipante(idParticipante);
    }

    /**
     * *Valida si un participante existe y tiene un equipo asignado.
     *
     * @param idParticipante ID del participante.
     * @return Objeto Participante validado.
     */
    private Participante validarParticipante(int idParticipante) {
        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(() -> new IllegalArgumentException("Participante no encontrado."));

        if (participante.getEquipo() == null) {
            throw new IllegalArgumentException("El participante no tiene equipo asignado, no puede responder.");
        }

        return participante;
    }

    /**
     * *Valida si una pregunta está asignada al equipo del participante.
     *
     * @param participante Objeto Participante.
     * @param idPregunta   ID de la pregunta.
     */
    private void validarPreguntaAsignada(Participante participante, int idPregunta) {
        int idEquipo = participante.getEquipo().getIdEquipo();
        PreguntasAsignadas asignacion = preguntasAsignadasRepository.findByEquipoIdEquipo(idEquipo)
                .orElseThrow(() -> new IllegalArgumentException("No se han asignado preguntas al equipo."));

        if (idPregunta != asignacion.getPregunta1() &&
                idPregunta != asignacion.getPregunta2() &&
                idPregunta != asignacion.getPregunta3() &&
                idPregunta != asignacion.getPregunta4() &&
                idPregunta != asignacion.getPregunta5()) {
            throw new IllegalArgumentException("La pregunta no está asignada a este equipo.");
        }

        if (respuestaRepository.existsByIdParticipanteAndIdPregunta(participante.getIdParticipante(), idPregunta)) {
            throw new IllegalArgumentException("El participante ya ha respondido esta pregunta.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Respuesta actualizarNota(int idRespuesta, ActualizarNotaDTO notaDTO) {
        // Validar que la nota esté en el rango permitido
        if (notaDTO.getNota() < 0 || notaDTO.getNota() > 100) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 100.");
        }

        // Buscar la respuesta por ID
        Respuesta respuesta = respuestaRepository.findById(idRespuesta)
                .orElseThrow(() -> new IllegalArgumentException("No se encuentra la respuesta con ID " + idRespuesta));

        // Actualizar la nota
        respuesta.setPuntuacionObtenida(notaDTO.getNota());
        return respuestaRepository.save(respuesta);
    }
}