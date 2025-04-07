package com.concursoacm.presentation.controllers;

import com.concursoacm.application.dtos.participantes.ParticipanteDTO;
import com.concursoacm.application.dtos.participantes.ParticipantesPorPaisDTO;
import com.concursoacm.application.dtos.participantes.ParticipantesPorRegionDTO;
import com.concursoacm.domain.models.Participante;
import com.concursoacm.domain.services.IParticipanteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * *Controlador REST para la gestión de participantes.
 */
@RestController
@RequestMapping("/participantes")
public class ParticipanteController {

    private final IParticipanteService participanteService;

    /**
     * *Constructor que inyecta el servicio de participantes.
     *
     * @param participanteService Servicio para la gestión de participantes.
     */
    public ParticipanteController(IParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    /**
     * *Obtiene una lista de todos los participantes.
     *
     * @return Lista de objetos ParticipanteDTO.
     */
    @GetMapping
    public List<ParticipanteDTO> getAllParticipantes() {
        return participanteService.getAllParticipantes();
    }

    /**
     * *Obtiene los participantes por país.
     *
     * @param idPais ID del país.
     * @return DTO con los participantes agrupados por país.
     */
    @GetMapping("/pais/{idPais}")
    public ResponseEntity<?> getParticipantesPorPais(@PathVariable int idPais) {
        try {
            ParticipantesPorPaisDTO participanteDTO = participanteService.getParticipantesPorPaisDTO(idPais);
            return ResponseEntity.ok(participanteDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * *Obtiene los participantes por región.
     *
     * @param idRegion ID de la región.
     * @return DTO con los participantes agrupados por región.
     */
    @GetMapping("/region/{idRegion}")
    public ResponseEntity<?> getParticipantesPorRegion(@PathVariable int idRegion) {
        try {
            ParticipantesPorRegionDTO dto = participanteService.getParticipantesPorRegionDTO(idRegion);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * *Obtiene los participantes por equipo.
     *
     * @param idEquipo ID del equipo.
     * @return Lista de objetos ParticipanteDTO.
     */
    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<?> getParticipantesPorEquipo(@PathVariable int idEquipo) {
        List<ParticipanteDTO> dtos = participanteService.getParticipantesPorEquipoDTO(idEquipo);
        if (dtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtos);
    }

    /**
     * *Obtiene un participante por su ID.
     *
     * @param id ID del participante.
     * @return Objeto ParticipanteDTO si se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getParticipanteById(@PathVariable int id) {
        ParticipanteDTO participanteDTO = participanteService.getParticipanteById(id);
        if (participanteDTO != null) {
            return ResponseEntity.ok(participanteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * *Agrega un nuevo participante.
     *
     * @param participante Objeto Participante a agregar.
     * @return Objeto ParticipanteDTO del participante agregado.
     */
    @PostMapping
    public ResponseEntity<?> crearParticipante(@RequestBody Participante participante) {
        participante.setEquipo(null); // Forzar a que no tenga equipo
        ParticipanteDTO nuevoParticipante = participanteService.crearParticipante(participante);
        return ResponseEntity.ok(nuevoParticipante);
    }

    /**
     * *Asigna un participante a un equipo.
     *
     * @param idParticipante ID del participante.
     * @param idEquipo       ID del equipo.
     * @return Objeto Participante actualizado.
     */
    @PutMapping("/{idParticipante}/asignar-equipo")
    public ResponseEntity<Participante> asignarParticipanteAEquipo(
            @PathVariable int idParticipante,
            @RequestParam int idEquipo) {
        Participante actualizado = participanteService.asignarParticipanteAEquipo(idParticipante, idEquipo);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * *Quita un participante de un equipo.
     *
     * @param idParticipante ID del participante.
     * @param idEquipo       ID del equipo.
     * @return Objeto Participante actualizado.
     */
    @PutMapping("/{idParticipante}/quitar-equipo")
    public ResponseEntity<Participante> quitarEquipo(@PathVariable int idParticipante, @RequestParam int idEquipo) {
        try {
            Participante actualizado = participanteService.quitarParticipanteDeEquipo(idParticipante, idEquipo);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * *Actualiza un participante existente.
     *
     * @param id           ID del participante a actualizar.
     * @param participante Objeto Participante con los nuevos datos.
     * @return Objeto ParticipanteDTO actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParticipante(@PathVariable int id, @RequestBody Participante participante) {
        participante.setEquipo(null); // Forzar a que no tenga equipo
        ParticipanteDTO participanteActualizado = participanteService.updateParticipante(id, participante);
        if (participanteActualizado != null) {
            return ResponseEntity.ok(participanteActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * *Elimina un participante por su ID.
     *
     * @param id ID del participante a eliminar.
     * @return Mensaje de éxito o error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipante(@PathVariable int id) {
        boolean eliminado = participanteService.deleteParticipante(id);
        if (eliminado) {
            return ResponseEntity.ok("Participante eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
