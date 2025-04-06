package com.concursoacm.dtos;

public class PreguntaDTO {
    private int idPregunta;
    private String texto;
    private int totalPuntos;

    public PreguntaDTO(int idPregunta, String texto, int totalPuntos) {
        this.idPregunta = idPregunta;
        this.texto = texto;
        this.totalPuntos = totalPuntos;
    }

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
    public int getTotalPuntos() {
        return totalPuntos;
    }
    public void setTotalPuntos(int totalPuntos) {
        this.totalPuntos = totalPuntos;
    }
}

