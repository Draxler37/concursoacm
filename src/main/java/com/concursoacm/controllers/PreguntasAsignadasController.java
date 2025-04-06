package com.concursoacm.controllers;

import com.concursoacm.dtos.PreguntasAsignadasDetalleDTO;
import com.concursoacm.services.PreguntasAsignadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preguntas-asignadas")
public class PreguntasAsignadasController {

    @Autowired
    private PreguntasAsignadasService preguntasAsignadasService;

    // Endpoint para asignar preguntas a TODOS los equipos sin asignación previa
    @PostMapping("/asignar-todos")
    public ResponseEntity<?> asignarPreguntasATodos() {
        try {
            preguntasAsignadasService.asignarPreguntasATodosLosEquipos();
            return ResponseEntity.ok("Preguntas asignadas a todos los equipos sin asignación previa.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Endpoint para consultar las preguntas asignadas a un equipo
    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<?> obtenerDetallesPreguntasAsignadas(@PathVariable int idEquipo, Authentication authentication) {
        try {
            String usuarioNormalizado = authentication.getName();
            PreguntasAsignadasDetalleDTO dto = preguntasAsignadasService.obtenerDetallesPreguntasAsignadas(idEquipo, usuarioNormalizado);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

