package com.securityservice.security_service.model.dto.request;

public class LogoutRequest {

    private String username; // Nombre de usuario que desea cerrar sesión
    private String token;    // Token actual (opcional, dependiendo de tu implementación)

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
