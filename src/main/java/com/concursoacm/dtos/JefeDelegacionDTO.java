package com.concursoacm.dtos;

public class JefeDelegacionDTO {
    private int idJefe;
    private String nombreParticipante;

    public JefeDelegacionDTO(int idJefe, String nombreParticipante) {
        this.idJefe = idJefe;
        this.nombreParticipante = nombreParticipante;
    }

    // Getters y Setters
    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }

    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }
}


