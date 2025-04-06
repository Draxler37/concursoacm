package com.concursoacm.controllers;

import com.concursoacm.models.Respuesta;
import com.concursoacm.services.RespuestaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    // Endpoint para que un participante responda a una pregunta.
    // El id del participante se pasa en la URL y en el body se envían el idPregunta y el texto de la respuesta.
    @PostMapping("/responder/{idParticipante}")
    public ResponseEntity<?> responderPregunta(@PathVariable int idParticipante, @RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("idPregunta") || !requestBody.containsKey("respuestaParticipante")) {
            return ResponseEntity.badRequest().body("Los campos 'idPregunta' y 'respuestaParticipante' son obligatorios.");
        }
        
        int idPregunta;
        try {
            idPregunta = (Integer) requestBody.get("idPregunta");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("El campo 'idPregunta' debe ser un número entero.");
        }
        
        String respuestaText = (String) requestBody.get("respuestaParticipante");
        
        try {
            Respuesta respuesta = respuestaService.crearRespuesta(idParticipante, idPregunta, respuestaText);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Endpoint para consultar respuestas por participante
    @GetMapping("/participante/{idParticipante}")
    public ResponseEntity<?> getRespuestasPorParticipante(@PathVariable int idParticipante) {
        List<Respuesta> respuestas = respuestaService.getRespuestasPorParticipante(idParticipante);
        if (respuestas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuestas);
    }
}



