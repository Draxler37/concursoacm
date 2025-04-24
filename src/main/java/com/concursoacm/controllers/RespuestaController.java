package com.concursoacm.controllers;

import com.concursoacm.application.dtos.resultados.ActualizarNotaDTO;
import com.concursoacm.interfaces.services.IRespuestaService;
import com.concursoacm.models.Respuesta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * *Controlador REST para la gestión de respuestas.
 */
@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    private final IRespuestaService respuestaService;

    /**
     * *Constructor que inyecta el servicio de respuestas.
     *
     * @param respuestaService Servicio para la gestión de respuestas.
     */
    public RespuestaController(IRespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    /**
     * *Permite a un participante responder a una pregunta.
     *
     * @param idParticipante ID del participante.
     * @param requestBody    Mapa con los datos de la respuesta.
     * @return Objeto Respuesta creado o mensaje de error.
     */
    @PostMapping("/responder/{idParticipante}")
    public ResponseEntity<?> responderPregunta(@PathVariable int idParticipante,
            @RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("idPregunta") || !requestBody.containsKey("respuestaParticipante")) {
            return ResponseEntity.badRequest()
                    .body("Los campos 'idPregunta' y 'respuestaParticipante' son obligatorios.");
        }

        try {
            int idPregunta = (Integer) requestBody.get("idPregunta");
            String respuestaText = (String) requestBody.get("respuestaParticipante");
            Respuesta respuesta = respuestaService.crearRespuesta(idParticipante, idPregunta, respuestaText);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ClassCastException e) {
            return ResponseEntity.badRequest().body("El campo 'idPregunta' debe ser un número entero.");
        }
    }

    /**
     * *Obtiene las respuestas de un participante.
     *
     * @param idParticipante ID del participante.
     * @return Lista de respuestas o mensaje de error.
     */
    @GetMapping("/participante/{idParticipante}")
    public ResponseEntity<?> getRespuestasDelParticipante(@PathVariable int idParticipante) {
        List<Respuesta> respuestas = respuestaService.getRespuestasDelParticipante(idParticipante);
        if (respuestas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuestas);
    }

    /**
     * *Actualiza la nota de una respuesta.
     *
     * @param idRespuesta ID de la respuesta.
     * @param request     Objeto DTO que contiene la nueva nota.
     * @return Respuesta actualizada o mensaje de error.
     */
    @PutMapping("/{idRespuesta}/actualizar-nota")
    public ResponseEntity<?> actualizarNota(@PathVariable int idRespuesta, @RequestBody ActualizarNotaDTO request) {
        try {
            Respuesta respuestaActualizada = respuestaService.actualizarNota(idRespuesta, request);
            return ResponseEntity.ok(respuestaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
