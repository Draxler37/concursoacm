package com.concursoacm.models;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos_preguntas")
public class EquipoPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEquipoPregunta;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;

    // Getters y setters
    public int getIdEquipoPregunta() {
        return idEquipoPregunta;
    }

    public void setIdEquipoPregunta(int idEquipoPregunta) {
        this.idEquipoPregunta = idEquipoPregunta;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
}