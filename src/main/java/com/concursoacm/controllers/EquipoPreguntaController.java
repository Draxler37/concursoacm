package com.concursoacm.controllers;

import com.concursoacm.application.dtos.preguntas.PreguntasAsignadasDetalleDTO;
import com.concursoacm.interfaces.services.IEquipoPreguntaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * *Controlador REST para la gestión de preguntas asignadas.
 */
@RestController
@RequestMapping("/equipos-preguntas")
public class EquipoPreguntaController {

    private final IEquipoPreguntaService equipoPreguntaService;

    /**
     * *Constructor que inyecta el servicio de preguntas asignadas.
     *
     * @param equipoPreguntaService Servicio para la gestión de preguntas
     *                              asignadas.
     */
    public EquipoPreguntaController(IEquipoPreguntaService equipoPreguntaService) {
        this.equipoPreguntaService = equipoPreguntaService;
    }

    /**
     * *Asigna preguntas a todos los equipos sin asignación previa.
     *
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/asignar-todos")
    public ResponseEntity<String> asignarPreguntasATodos() {
        equipoPreguntaService.asignarPreguntasATodosLosEquipos();
        return ResponseEntity.ok("Preguntas asignadas a todos los equipos sin asignación previa.");
    }

    /**
     * *Obtiene los detalles de las preguntas asignadas a un equipo.
     *
     * @param idEquipo ID del equipo.
     * @return DTO con los detalles de las preguntas asignadas.
     */
    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<?> obtenerPreguntasAsignadasAlEquipo(@PathVariable int idEquipo,
            Authentication authentication) {
        try {
            PreguntasAsignadasDetalleDTO dto = equipoPreguntaService.getPreguntasAsignadasAlEquipo(idEquipo,
                    authentication);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body("Acceso denegado: " + e.getMessage());
        }
    }
}