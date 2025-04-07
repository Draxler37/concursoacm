package com.concursoacm.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "paises")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPais;

    @Column(nullable = false)
    private String nombrePais;

    @Column(nullable = false)
    private String codigoTelefonico;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "pais-participantes") // Relación con Participante
    @JsonIgnore
    private List<Participante> participantes;


    // Getters y Setters
    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getCodigoTelefonico() {
        return codigoTelefonico;
    }

    public void setCodigoTelefonico(String codigoTelefonico) {
        this.codigoTelefonico = codigoTelefonico;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }
}




