package com.concursoacm.domain.models;

import jakarta.persistence.*;

/**
 * *Entidad que representa el resultado de un participante en el sistema.
 */
@Entity
@Table(name = "resultados")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idResultado;

    @Column(name = "id_participante", nullable = false)
    private int idParticipante;

    @Column(name = "puntuacion_total", nullable = false)
    private int puntuacionTotal;

    /**
     * *Obtiene el ID del resultado.
     *
     * @return ID del resultado.
     */
    public int getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }

    /**
     * *Obtiene el ID del participante asociado al resultado.
     *
     * @return ID del participante.
     */
    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    /**
     * *Obtiene la puntuación total del participante.
     *
     * @return Puntuación total.
     */
    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(int puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }
}



