package com.securityservice.security_service.service;

import com.securityservice.security_service.model.dto.request.CreateRoleRequest;
import com.securityservice.security_service.model.dto.response.PermissionResponse;
import com.securityservice.security_service.model.dto.response.RoleResponse;
import com.securityservice.security_service.model.entity.Permission;
import com.securityservice.security_service.model.entity.Role;
import com.securityservice.security_service.repository.PermissionRepository;
import com.securityservice.security_service.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public RoleResponse createRole(CreateRoleRequest request) {
        List<Permission> permissions = permissionRepository.findByNameIn(request.getPermissions());
        if(permissions.size() != request.getPermissions().size()) {
            throw new IllegalArgumentException("Algunos permisos no existen");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setPermissions(permissions);

        Role savedRole = roleRepository.save(role);

        // Retornar un RoleResponse con los datos del rol creado
        return new RoleResponse(
                savedRole.getRoleid(), // Long
                savedRole.getName(), // String
                savedRole.getPermissions().stream()
                        .map(permission -> new PermissionResponse(permission.getId(), permission.getName()))
                        .collect(Collectors.toList()) // List<PermissionResponse>
        );
    }
}
