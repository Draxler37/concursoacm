package com.concursoacm.controllers;

import com.concursoacm.dtos.EquipoDTO;
import com.concursoacm.models.Equipo;
import com.concursoacm.services.EquipoService;
import com.concursoacm.services.JefeDelegacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;
    
    @Autowired
    private JefeDelegacionService jefeDelegacionService;

    // Endpoint para obtener todos los equipos
    @GetMapping
    public List<EquipoDTO> getAllEquipos() {
        return equipoService.getAllEquipos();
    }

    // Endpoint para obtener un equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipoById(@PathVariable int id) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        if (equipo != null) {
            return ResponseEntity.ok(equipo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un equipo (solo jefes de delegación autenticados)
    @PostMapping
    public ResponseEntity<?> crearEquipo(@RequestBody Equipo equipo, Authentication authentication) {
        // Obtener el ID del participante desde el usuario autenticado
        String usuarioNormalizado = authentication.getName();
        Equipo nuevoEquipo = equipoService.guardarEquipo(equipo, usuarioNormalizado);
        EquipoDTO equipoDTO = new EquipoDTO(
                nuevoEquipo.getIdEquipo(),
                nuevoEquipo.getNombreEquipo(),
                nuevoEquipo.getCategoria(),
                nuevoEquipo.getPais().getNombrePais()
        );
        return ResponseEntity.ok(equipoDTO);
    }

    // Actualizar un equipo existente (solo jefes de delegación autenticados)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEquipo(@PathVariable int id, @RequestBody Equipo equipo, Authentication authentication) {
        equipo.setIdEquipo(id);
        String usuarioNormalizado = authentication.getName();
        Equipo equipoActualizado = equipoService.guardarEquipo(equipo, usuarioNormalizado);

        EquipoDTO equipoDTO = new EquipoDTO(
                equipoActualizado.getIdEquipo(),
                equipoActualizado.getNombreEquipo(),
                equipoActualizado.getCategoria(),
                equipoActualizado.getPais().getNombrePais()
        );

        return ResponseEntity.ok(equipoDTO);
    }


    // Eliminar un equipo (solo jefes de delegación)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEquipo(@PathVariable int id, Authentication authentication) {
        int idParticipanteJefe = jefeDelegacionService.obtenerIdParticipantePorUsuario(authentication.getName());
        equipoService.eliminarEquipo(id, idParticipanteJefe);
        return ResponseEntity.ok("Equipo eliminado correctamente.");
    }

}








