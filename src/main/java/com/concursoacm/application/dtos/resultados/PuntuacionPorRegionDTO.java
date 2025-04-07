package com.concursoacm.application.dtos.resultados;

public class PuntuacionPorRegionDTO {
    private String nombreRegion;
    private int totalPuntos;

    public PuntuacionPorRegionDTO(String nombreRegion, int totalPuntos) {
        this.nombreRegion = nombreRegion;
        this.totalPuntos = totalPuntos;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public int getTotalPuntos() {
        return totalPuntos;
    }

    public void setTotalPuntos(int totalPuntos) {
        this.totalPuntos = totalPuntos;
    }
}
