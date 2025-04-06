package com.concursoacm.services.interfaces;

import com.concursoacm.dtos.PreguntasAsignadasDetalleDTO;

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
    PreguntasAsignadasDetalleDTO obtenerDetallesPreguntasAsignadas(int idEquipo, String usuarioNormalizado);
}
