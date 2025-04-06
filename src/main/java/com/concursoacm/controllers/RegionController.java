package com.concursoacm.controllers;

import com.concursoacm.models.Region;
import com.concursoacm.services.interfaces.IRegionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * *Controlador REST para la gestión de regiones.
 */
@RestController
@RequestMapping("/regiones")
public class RegionController {

    private final IRegionService regionService;

    /**
     * *Constructor que inyecta el servicio de regiones.
     *
     * @param regionService Servicio para la gestión de regiones.
     */
    public RegionController(IRegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * *Obtiene una lista de todas las regiones.
     *
     * @return Lista de objetos Region.
     */
    @GetMapping
    public List<Region> obtenerRegiones() {
        return regionService.obtenerTodasLasRegiones();
    }

    /**
     * *Guarda una nueva región.
     *
     * @param region Objeto Region a guardar.
     * @return Objeto Region guardado.
     */
    @PostMapping
    public Region guardarRegion(@RequestBody Region region) {
        return regionService.guardarRegion(region);
    }

    /**
     * *Actualiza una región existente.
     *
     * @param id          ID de la región a actualizar.
     * @param nuevaRegion Objeto Region con los nuevos datos.
     * @return Objeto Region actualizado.
     */
    @PutMapping("/{id}")
    public Region actualizarRegion(@PathVariable int id, @RequestBody Region nuevaRegion) {
        return regionService.actualizarRegion(id, nuevaRegion);
    }

    /**
     * *Elimina una región por su ID.
     *
     * @param id ID de la región a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarRegion(@PathVariable int id) {
        regionService.eliminarRegion(id);
    }
}
