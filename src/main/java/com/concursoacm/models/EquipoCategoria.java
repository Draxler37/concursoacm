package com.concursoacm.models;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos_categorias")
public class EquipoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategoria;

    @Column(nullable = false)
    private String categoria;

    // Getters y setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return categoria;
    }

    public void setNombreCategoria(String categoria) {
        this.categoria = categoria;
    }
}