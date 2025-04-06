package com.concursoacm.dtos;

import com.concursoacm.models.Pais;

/**
 * Data Transfer Object (DTO) para la entidad Pais.
 */
public class PaisDTO {

    private int idPais;
    private String nombrePais;
    private String codigoTelefonico;
    private String nombreRegion;

    /**
     * Constructor que inicializa el DTO a partir de un objeto Pais.
     *
     * @param pais Objeto Pais.
     */
    public PaisDTO(Pais pais) {
        this.idPais = pais.getIdPais();
        this.nombrePais = pais.getNombrePais();
        this.codigoTelefonico = pais.getCodigoTelefonico();
        this.nombreRegion = pais.getRegion() != null ? pais.getRegion().getNombreRegion() : null;
    }

    // Getters
    public int getIdPais() {
        return idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public String getCodigoTelefonico() {
        return codigoTelefonico;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }
}