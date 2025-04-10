package com.concursoacm.domain.services;

import com.concursoacm.domain.models.Pregunta;

import java.util.List;

/**
 * *Interfaz para la gestión de preguntas.
 */
public interface IPreguntaService {

    /**
     * *Obtiene todas las preguntas.
     *
     * @return Lista de preguntas.
     */
    List<Pregunta> obtenerTodasLasPreguntas();

    /**
     * *Obtiene una pregunta por su ID.
     *
     * @param idPregunta ID de la pregunta.
     * @return Pregunta encontrada.
     */
    Pregunta obtenerPreguntaPorId(int idPregunta);

    /**
     * *Crea una nueva pregunta.
     *
     * @param pregunta Pregunta a crear.
     * @return Pregunta creada.
     */
    Pregunta crearPregunta(Pregunta pregunta);

    /**
     * *Actualiza una pregunta existente.
     *
     * @param idPregunta ID de la pregunta a actualizar.
     * @param pregunta   Pregunta con los nuevos datos.
     * @return Pregunta actualizada.
     */
    Pregunta actualizarPregunta(int idPregunta, Pregunta pregunta);

    /**
     * *Elimina una pregunta por su ID.
     *
     * @param idPregunta ID de la pregunta a eliminar.
     * @return true si la pregunta fue eliminada, false en caso contrario.
     */
    boolean eliminarPregunta(int idPregunta);
}
