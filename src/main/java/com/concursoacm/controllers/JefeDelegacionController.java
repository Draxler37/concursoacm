package com.concursoacm.controllers;

import com.concursoacm.dtos.JefeDelegacionDTO;
import com.concursoacm.models.JefeDelegacion;
import com.concursoacm.services.JefeDelegacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/jefes-delegacion")
public class JefeDelegacionController {

    @Autowired
    private JefeDelegacionService jefeDelegacionService;

    // Endpoint para listar todos los jefes de delegación
    @GetMapping
    public List<JefeDelegacionDTO> obtenerTodosLosJefes() {
        return jefeDelegacionService.obtenerTodosLosJefes();
    }

    // Endpoint para obtener un jefe de delegación por ID
    @GetMapping("/{idJefe}")
    public ResponseEntity<?> obtenerJefePorId(@PathVariable int idJefe) {
        Optional<JefeDelegacionDTO> jefe = jefeDelegacionService.obtenerJefePorId(idJefe);
        return jefe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para crear un jefe de delegación
    @PostMapping("/{idParticipante}")
    public ResponseEntity<?> crearJefeDelegacion(@PathVariable int idParticipante, @RequestBody String contraseña) {
        JefeDelegacion nuevoJefe = jefeDelegacionService.crearJefeDelegacion(idParticipante, contraseña);
        return ResponseEntity.ok(nuevoJefe);
    }
    
    // Endpoint para actualizar un jefe de delegación(contraseña)
    @PutMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<?> cambiarContraseña(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String contraseñaActual = requestBody.get("contraseñaActual");
        String nuevaContraseña = requestBody.get("nuevaContraseña");

        if (contraseñaActual == null || nuevaContraseña == null) {
            return ResponseEntity.badRequest().body("Las claves 'contraseñaActual' y 'nuevaContraseña' son obligatorias.");
        }

        // Obtener el usuario autenticado
        String usuarioNormalizado = authentication.getName();

        // Llamar al servicio para cambiar la contraseña
        boolean cambiada = jefeDelegacionService.cambiarContraseña(id, usuarioNormalizado, contraseñaActual, nuevaContraseña);

        if (cambiada) {
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
    }

    // Endpoint para eliminar un jefe de delegación
    @DeleteMapping("/{idJefe}")
    public ResponseEntity<?> eliminarJefeDelegacion(@PathVariable int idJefe) {
        jefeDelegacionService.eliminarJefeDelegacion(idJefe);
        return ResponseEntity.ok("Jefe de delegación eliminado correctamente.");
    }
}




