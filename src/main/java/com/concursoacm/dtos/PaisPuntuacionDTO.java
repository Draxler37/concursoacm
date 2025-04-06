package com.concursoacm.dtos;

public class PaisPuntuacionDTO {
    private String nombrePais;
    private int totalPuntos;

    public PaisPuntuacionDTO(String nombrePais, int totalPuntos) {
        this.nombrePais = nombrePais;
        this.totalPuntos = totalPuntos;
    }

    public String getNombrePais() {
        return nombrePais;
    }
    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
    public int getTotalPuntos() {
        return totalPuntos;
    }
    public void setTotalPuntos(int totalPuntos) {
        this.totalPuntos = totalPuntos;
    }
}

