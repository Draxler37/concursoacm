package com.concursoacm.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEquipo;

    @Column(nullable = false)
    private String nombreEquipo;

    @Column(nullable = false)
    private String categoria;
    
    @ManyToOne
    @JoinColumn(name = "id_pais", nullable = false)
    private Pais pais;
    
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "equipo-participantes") // Relación con Participante
    private List<Participante> participantes;

    // Constructor vacío (necesario para JPA)
    public Equipo() {}

    // Constructor parametrizado para asignar por ID
    public Equipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    // Getters y Setters
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
    
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Participante> getParticipante() {
        return participantes;
    }

    public void setParticipante(List<Participante> participantes) {
        this.participantes = participantes;
    }
}








