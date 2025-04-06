package com.concursoacm.services;

import com.concursoacm.dtos.PaisDTO;
import com.concursoacm.models.Pais;
import com.concursoacm.models.Region;
import com.concursoacm.repositories.PaisRepository;
import com.concursoacm.repositories.RegionRepository;
import com.concursoacm.services.interfaces.IPaisService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que implementa la lógica de negocio para la gestión de países.
 */
@Service
public class PaisService implements IPaisService {

    private final PaisRepository paisRepository;
    private final RegionRepository regionRepository;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param paisRepository   Repositorio para la gestión de países.
     * @param regionRepository Repositorio para la gestión de regiones.
     */
    public PaisService(PaisRepository paisRepository, RegionRepository regionRepository) {
        this.paisRepository = paisRepository;
        this.regionRepository = regionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PaisDTO> obtenerTodosLosPaises() {
        return paisRepository.findAll().stream()
                .map(PaisDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PaisDTO> obtenerPaisPorId(int id) {
        return paisRepository.findById(id).map(PaisDTO::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaisDTO guardarPais(Pais pais) {
        Region region = regionRepository.findById(pais.getRegion().getIdRegion())
                .orElseThrow(() -> new RuntimeException("Región no encontrada"));
        pais.setRegion(region);
        return new PaisDTO(paisRepository.save(pais));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaisDTO actualizarPais(int id, Pais nuevoPais) {
        return paisRepository.findById(id).map(pais -> {
            pais.setNombrePais(nuevoPais.getNombrePais());
            pais.setCodigoTelefonico(nuevoPais.getCodigoTelefonico());
            Region region = regionRepository.findById(nuevoPais.getRegion().getIdRegion())
                    .orElseThrow(() -> new RuntimeException("Región no encontrada"));
            pais.setRegion(region);
            return new PaisDTO(paisRepository.save(pais));
        }).orElseThrow(() -> new RuntimeException("País no encontrado"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean eliminarPais(int id) {
        if (paisRepository.existsById(id)) {
            paisRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
