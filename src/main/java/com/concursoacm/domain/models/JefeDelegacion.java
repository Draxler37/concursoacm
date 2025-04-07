package com.concursoacm.domain.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "jefes_delegacion")
public class JefeDelegacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jefe")
    private int idJefe;

    @Column(name = "usuario_normalizado", nullable = false, unique = true)
    private String usuarioNormalizado;

    @Column(name = "contraseña", nullable = false)
    private String contrasena;

    @OneToOne
    @JoinColumn(name = "id_participante", nullable = false)
    private Participante participante;

    // Getters y setters
    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }

    public String getUsuarioNormalizado() {
        return usuarioNormalizado;
    }

    public void setUsuarioNormalizado(String usuarioNormalizado) {
        this.usuarioNormalizado = usuarioNormalizado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }
}




