package com.concursoacm.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * *Entidad que representa una respuesta en el sistema.
 */
@Entity
@Table(name = "respuestas")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRespuesta;

    @Column(nullable = false)
    @NotBlank(message = "La respuesta del participante no puede estar vacía.")
    private String respuestaParticipante;

    @Column(nullable = false)
    @Positive(message = "El ID del participante debe ser un número positivo.")
    private int idParticipante;

    @Column(nullable = false)
    @Positive(message = "El ID del equipo debe ser un número positivo.")
    private int idEquipo;

    @Column(nullable = false)
    @Positive(message = "El ID de la pregunta debe ser un número positivo.")
    private int idPregunta;

    @Column(nullable = false)
    @PositiveOrZero(message = "La puntuación obtenida no puede ser negativa.")
    private int puntuacionObtenida = 0;

    /**
     * *Obtiene el ID de la respuesta.
     *
     * @return ID de la respuesta.
     */
    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    /**
     * *Obtiene el texto de la respuesta del participante.
     *
     * @return Texto de la respuesta.
     */
    public String getRespuestaParticipante() {
        return respuestaParticipante;
    }

    public void setRespuestaParticipante(String respuestaParticipante) {
        this.respuestaParticipante = respuestaParticipante;
    }

    /**
     * *Obtiene el ID del participante que respondió.
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
     * *Obtiene el ID del equipo asociado a la respuesta.
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
     * *Obtiene el ID de la pregunta respondida.
     *
     * @return ID de la pregunta.
     */
    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    /**
     * *Obtiene la puntuación obtenida por la respuesta.
     *
     * @return Puntuación obtenida.
     */
    public int getPuntuacionObtenida() {
        return puntuacionObtenida;
    }

    public void setPuntuacionObtenida(int puntuacionObtenida) {
        this.puntuacionObtenida = puntuacionObtenida;
    }
}
