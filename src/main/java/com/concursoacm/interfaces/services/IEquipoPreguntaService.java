package com.concursoacm.interfaces.services;

import com.concursoacm.application.dtos.preguntas.PreguntasAsignadasDetalleDTO;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public interface IEquipoPreguntaService {

    /**
     * *Asigna preguntas a todos los equipos sin asignación previa.
     */
    void asignarPreguntasATodosLosEquipos();

    /**
     * *Obtiene los detalles de las preguntas asignadas a un equipo.
     *
     * @param idEquipo ID del equipo.
     * @return DTO con los detalles de las preguntas asignadas.
     */
    PreguntasAsignadasDetalleDTO getPreguntasAsignadasAlEquipo(int idEquipo, Authentication authentication)
            throws IllegalArgumentException, AccessDeniedException;
}