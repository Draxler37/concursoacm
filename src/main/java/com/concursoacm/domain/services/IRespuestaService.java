package com.concursoacm.domain.services;

import java.util.List;

import com.concursoacm.application.dtos.resultados.ActualizarNotaDTO;
import com.concursoacm.domain.models.Respuesta;

/**
 * *Interfaz que define los métodos para la gestión de respuestas.
 */
public interface IRespuestaService {

    /**
     * *Crea una nueva respuesta para una pregunta asignada a un participante.
     *
     * @param idParticipante ID del participante.
     * @param idPregunta     ID de la pregunta.
     * @param respuestaText  Texto de la respuesta del participante.
     * @return Objeto Respuesta creado.
     */
    Respuesta crearRespuesta(int idParticipante, int idPregunta, String respuestaText);

    /**
     * *Obtiene las respuestas de un participante.
     *
     * @param idParticipante ID del participante.
     * @return Lista de respuestas del participante.
     */
    List<Respuesta> getRespuestasDelParticipante(int idParticipante);

    /**
     * *Actualiza la nota de una respuesta.
     *
     * @param idRespuesta  ID de la respuesta.
     * @param notaAsignada Nota asignada a la respuesta.
     * @return Respuesta actualizada.
     */
    Respuesta actualizarNota(int idRespuesta, ActualizarNotaDTO notaDTO);
}
