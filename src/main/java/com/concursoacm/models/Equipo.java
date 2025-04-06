package com.concursoacm.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

/**
 * *Entidad que representa un equipo en el sistema.
 */
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

    /**
     * *Constructor vacío (necesario para JPA).
     */
    public Equipo() {
    }

    /**
     * *Constructor parametrizado para asignar por ID.
     *
     * @param idEquipo ID del equipo.
     */
    public Equipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    // Getters y Setters

    /**
     * *Obtiene el ID del equipo.
     *
     * @return ID del equipo.
     */
    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    /**
     * *Obtiene el nombre del equipo.
     *
     * @return Nombre del equipo.
     */
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    /**
     * *Obtiene la categoría del equipo.
     *
     * @return Categoría del equipo.
     */
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * *Obtiene el país asociado al equipo.
     *
     * @return Objeto Pais asociado.
     */
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    /**
     * *Obtiene la lista de participantes asociados al equipo.
     *
     * @return Lista de participantes.
     */
    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }
}
