package com.securityservice.security_service.controller;

import com.securityservice.security_service.model.dto.request.CreateRoleRequest;
import com.securityservice.security_service.model.dto.response.RoleResponse;
import com.securityservice.security_service.model.entity.Role;
import com.securityservice.security_service.service.RoleService;
import com.securityservice.security_service.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
            RoleResponse roleResponse = roleService.createRole(request);
            ApiResponse<RoleResponse> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Rol creado exitosamente",
                    roleResponse
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
