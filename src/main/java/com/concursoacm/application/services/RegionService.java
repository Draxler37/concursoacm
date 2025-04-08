package com.concursoacm.application.services;

import com.concursoacm.domain.models.Region;
import com.concursoacm.domain.services.IRegionService;
import com.concursoacm.infrastructure.repositories.RegionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *Servicio que implementa la lógica de negocio para la gestión de regiones.
 */
@Service
public class RegionService implements IRegionService {

    private final RegionRepository regionRepository;

    /**
     * *Constructor que inyecta el repositorio de regiones.
     *
     * @param regionRepository Repositorio para la gestión de regiones.
     */
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Region> obtenerTodasLasRegiones() {
        return regionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Region guardarRegion(Region region) {
        return regionRepository.save(region);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Region actualizarRegion(int id, Region nuevaRegion) {
        return regionRepository.findById(id).map(region -> {
            region.setNombreRegion(nuevaRegion.getNombreRegion());
            return regionRepository.save(region);
        }).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eliminarRegion(int id) {
        regionRepository.deleteById(id);
    }
}
