package com.concursoacm.dtos;

public class EquipoDTO {

    private int idEquipo;
    private String nombreEquipo;
    private String categoria;
    private String nombrePais;

    // Constructor
    public EquipoDTO(int idEquipo, String nombreEquipo, String categoria, String nombrePais) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.categoria = categoria;
        this.nombrePais = nombrePais;
    }

    // Getters y setters
    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
