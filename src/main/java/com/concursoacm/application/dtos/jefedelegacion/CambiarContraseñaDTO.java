package com.concursoacm.application.dtos.jefedelegacion;

import jakarta.validation.constraints.NotBlank;

public class CambiarContraseñaDTO {

    @NotBlank(message = "La contraseña actual no puede estar vacía.")
    private String contraseñaActual;

    @NotBlank(message = "La nueva contraseña no puede estar vacía.")
    private String nuevaContraseña;

    // Getters y setters
    public String getContraseñaActual() {
        return contraseñaActual;
    }

    public void setContraseñaActual(String contraseñaActual) {
        this.contraseñaActual = contraseñaActual;
    }

    public String getNuevaContraseña() {
        return nuevaContraseña;
    }

    public void setNuevaContraseña(String nuevaContraseña) {
        this.nuevaContraseña = nuevaContraseña;
    }
}
