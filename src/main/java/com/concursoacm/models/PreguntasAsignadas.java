package com.concursoacm.models;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_asignadas")
public class PreguntasAsignadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @Column(name = "pregunta_1", nullable = false)
    private int pregunta1;

    @Column(name = "pregunta_2", nullable = false)
    private int pregunta2;

    @Column(name = "pregunta_3", nullable = false)
    private int pregunta3;

    @Column(name = "pregunta_4", nullable = false)
    private int pregunta4;

    @Column(name = "pregunta_5", nullable = false)
    private int pregunta5;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(int pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public int getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(int pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public int getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(int pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public int getPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(int pregunta4) {
        this.pregunta4 = pregunta4;
    }

    public int getPregunta5() {
        return pregunta5;
    }

    public void setPregunta5(int pregunta5) {
        this.pregunta5 = pregunta5;
    }
}

