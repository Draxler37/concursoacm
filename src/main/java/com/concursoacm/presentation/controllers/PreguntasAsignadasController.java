package com.concursoacm.presentation.controllers;

import com.concursoacm.application.dtos.preguntas.PreguntasAsignadasDetalleDTO;
import com.concursoacm.domain.services.IPreguntasAsignadasService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

/**
 * *Controlador REST para la gestión de preguntas asignadas.
 */
@RestController
@RequestMapping("/preguntas-asignadas")
public class PreguntasAsignadasController {

    private final IPreguntasAsignadasService preguntasAsignadasService;

    /**
     * *Constructor que inyecta el servicio de preguntas asignadas.
     *
     * @param preguntasAsignadasService Servicio para la gestión de preguntas
     *                                  asignadas.
     */
    public PreguntasAsignadasController(IPreguntasAsignadasService preguntasAsignadasService) {
        this.preguntasAsignadasService = preguntasAsignadasService;
    }

    /**
     * *Asigna preguntas a todos los equipos sin asignación previa.
     *
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/asignar-todos")
    public ResponseEntity<String> asignarPreguntasATodos() {
        preguntasAsignadasService.asignarPreguntasATodosLosEquipos();
        return ResponseEntity.ok("Preguntas asignadas a todos los equipos sin asignación previa.");
    }

    /**
     * *Obtiene los detalles de las preguntas asignadas a un equipo.
     *
     * @param idEquipo       ID del equipo.
     * @param authentication Información del usuario autenticado.
     * @return DTO con los detalles de las preguntas asignadas.
     */
    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<?> obtenerPreguntasAsignadasAlEquipo(
            @PathVariable int idEquipo, Authentication authentication) {
        try {
            String usuarioNormalizado = authentication.getName();
            PreguntasAsignadasDetalleDTO dto = preguntasAsignadasService.getPreguntasAsignadasAlEquipo(idEquipo,
                    usuarioNormalizado);
            return ResponseEntity.ok(dto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
