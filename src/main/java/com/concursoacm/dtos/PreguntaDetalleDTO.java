package com.concursoacm.dtos;

public class PreguntaDetalleDTO {
    private int idPregunta;
    private String texto;

    public PreguntaDetalleDTO(int idPregunta, String texto) {
        this.idPregunta = idPregunta;
        this.texto = texto;
    }

    // Getters y Setters
    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

