package com.concursoacm.dtos;

public class EquipoPuntuacionDTO {
    private String nombreEquipo;
    private int totalPuntos;

    public EquipoPuntuacionDTO(String nombreEquipo, int totalPuntos) {
        this.nombreEquipo = nombreEquipo;
        this.totalPuntos = totalPuntos;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }
    public int getTotalPuntos() {
        return totalPuntos;
    }
    public void setTotalPuntos(int totalPuntos) {
        this.totalPuntos = totalPuntos;
    }
}
