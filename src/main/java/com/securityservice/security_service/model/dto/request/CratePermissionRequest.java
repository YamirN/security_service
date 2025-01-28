package com.securityservice.security_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CratePermissionRequest {

    @NotBlank(message = "El nombre del permiso es obligatorio")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
