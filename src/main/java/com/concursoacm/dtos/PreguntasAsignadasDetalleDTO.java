package com.concursoacm.dtos;

import java.util.List;

public class PreguntasAsignadasDetalleDTO {
    private String nombreEquipo;
    private List<PreguntaDetalleDTO> preguntas;

    public PreguntasAsignadasDetalleDTO(String nombreEquipo, List<PreguntaDetalleDTO> preguntas) {
        this.nombreEquipo = nombreEquipo;
        this.preguntas = preguntas;
    }

    // Getters y Setters
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public List<PreguntaDetalleDTO> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaDetalleDTO> preguntas) {
        this.preguntas = preguntas;
    }
}

