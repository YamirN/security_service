package com.securityservice.security_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class CreateRoleRequest {

    private Long id;

    @NotBlank(message = "El nombre de rol es obligatorio")
    private String name;

    private List<String> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
