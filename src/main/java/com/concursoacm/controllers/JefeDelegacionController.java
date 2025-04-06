package com.concursoacm.controllers;

import com.concursoacm.dtos.JefeDelegacionDTO;
import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.services.interfaces.IJefeDelegacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * *Controlador REST para la gestión de jefes de delegación.
 */
@RestController
@RequestMapping("/jefes-delegacion")
public class JefeDelegacionController {

    private final IJefeDelegacionService jefeDelegacionService;

    /**
     * *Constructor que inyecta el servicio de jefes de delegación.
     *
     * @param jefeDelegacionService Servicio para la gestión de jefes de delegación.
     */
    public JefeDelegacionController(IJefeDelegacionService jefeDelegacionService) {
        this.jefeDelegacionService = jefeDelegacionService;
    }

    /**
     * *Obtiene una lista de todos los jefes de delegación.
     *
     * @return Lista de objetos JefeDelegacionDTO.
     */
    @GetMapping
    public ResponseEntity<List<JefeDelegacionDTO>> obtenerTodosLosJefes() {
        List<JefeDelegacionDTO> jefes = jefeDelegacionService.obtenerTodosLosJefes();
        return ResponseEntity.ok(jefes);
    }

    /**
     * *Obtiene un jefe de delegación por su ID.
     *
     * @param idJefe ID del jefe de delegación.
     * @return Objeto JefeDelegacionDTO si se encuentra.
     */
    @GetMapping("/{idJefe}")
    public ResponseEntity<JefeDelegacionDTO> obtenerJefePorId(@PathVariable int idJefe) {
        return jefeDelegacionService.obtenerJefePorId(idJefe)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * *Crea un nuevo jefe de delegación.
     *
     * @param idParticipante ID del participante que será jefe de delegación.
     * @param contraseña     Contraseña del jefe de delegación.
     * @return Objeto JefeDelegacion creado.
     */
    @PostMapping("/{idParticipante}")
    public ResponseEntity<JefeDelegacion> crearJefeDelegacion(@PathVariable int idParticipante,
            @RequestBody String contraseña) {
        JefeDelegacion nuevoJefe = jefeDelegacionService.crearJefeDelegacion(idParticipante, contraseña);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJefe);
    }

    /**
     * *Actualiza la contraseña de un jefe de delegación.
     *
     * @param id             ID del jefe de delegación.
     * @param requestBody    Mapa con las claves "contraseñaActual" y
     *                       "nuevaContraseña".
     * @param authentication Información del usuario autenticado.
     * @return Mensaje de éxito o error.
     */
    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<String> cambiarContraseña(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String contraseñaActual = requestBody.get("contraseñaActual");
        String nuevaContraseña = requestBody.get("nuevaContraseña");

        if (contraseñaActual == null || nuevaContraseña == null) {
            return ResponseEntity.badRequest()
                    .body("Las claves 'contraseñaActual' y 'nuevaContraseña' son obligatorias.");
        }

        String usuarioNormalizado = authentication.getName();
        boolean cambiada = jefeDelegacionService.cambiarContraseña(id, usuarioNormalizado, contraseñaActual,
                nuevaContraseña);

        if (cambiada) {
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
    }

    /**
     * *Elimina un jefe de delegación por su ID.
     *
     * @param idJefe ID del jefe de delegación a eliminar.
     * @return Mensaje de éxito.
     */
    @DeleteMapping("/{idJefe}")
    public ResponseEntity<String> eliminarJefeDelegacion(@PathVariable int idJefe) {
        jefeDelegacionService.eliminarJefeDelegacion(idJefe);
        return ResponseEntity.ok("Jefe de delegación eliminado correctamente.");
    }
}
