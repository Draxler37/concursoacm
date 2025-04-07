package com.concursoacm.domain.services;

import java.util.List;

import com.concursoacm.domain.models.Region;

/**
 * *Interfaz que define los métodos para la gestión de regiones.
 */
public interface IRegionService {

    /**
     * *Obtiene una lista de todas las regiones.
     *
     * @return Lista de objetos Region.
     */
    List<Region> obtenerTodasLasRegiones();

    /**
     * *Guarda una nueva región en la base de datos.
     *
     * @param region Objeto Region a guardar.
     * @return Objeto Region guardado.
     */
    Region guardarRegion(Region region);

    /**
     * *Actualiza una región existente.
     *
     * @param id          ID de la región a actualizar.
     * @param nuevaRegion Objeto Region con los nuevos datos.
     * @return Objeto Region actualizado o null si no se encuentra.
     */
    Region actualizarRegion(int id, Region nuevaRegion);

    /**
     * *Elimina una región por su ID.
     *
     * @param id ID de la región a eliminar.
     */
    void eliminarRegion(int id);
}