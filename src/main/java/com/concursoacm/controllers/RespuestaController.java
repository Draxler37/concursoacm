package com.concursoacm.controllers;

import com.concursoacm.models.Respuesta;
import com.concursoacm.services.interfaces.IRespuestaService;
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
    public ResponseEntity<?> responderPregunta(@PathVariable int idParticipante, @RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("idPregunta") || !requestBody.containsKey("respuestaParticipante")) {
            return ResponseEntity.badRequest().body("Los campos 'idPregunta' y 'respuestaParticipante' son obligatorios.");
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
    public ResponseEntity<?> getRespuestasPorParticipante(@PathVariable int idParticipante) {
        List<Respuesta> respuestas = respuestaService.getRespuestasPorParticipante(idParticipante);
        if (respuestas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuestas);
    }
}



