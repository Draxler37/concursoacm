package com.concursoacm.domain.services;

import com.concursoacm.application.dtos.preguntas.PreguntasAsignadasDetalleDTO;

/**
 * *Interfaz que define los métodos para la gestión de preguntas asignadas.
 */
public interface IPreguntasAsignadasService {

    /**
     * *Asigna preguntas a todos los equipos sin asignación previa.
     */
    void asignarPreguntasATodosLosEquipos();

    /**
     * *Obtiene los detalles de las preguntas asignadas a un equipo.
     *
     * @param idEquipo           ID del equipo.
     * @param usuarioNormalizado Usuario autenticado.
     * @return DTO con los detalles de las preguntas asignadas.
     */
    PreguntasAsignadasDetalleDTO getPreguntasAsignadasAlEquipo(int idEquipo, String usuarioNormalizado);
}
