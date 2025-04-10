package com.concursoacm.application.dtos.jefedelegacion;

import jakarta.validation.constraints.NotBlank;

public class CrearJefeDelegacionDTO {

    @NotBlank(message = "El usuario no puede estar vacío.")
    private String usuario;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String contraseña;

    // Getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
