package com.securityservice.security_service.service;

import com.securityservice.security_service.model.dto.request.LoginRequest;
import com.securityservice.security_service.model.dto.request.RegisterUserRequest;
import com.securityservice.security_service.model.dto.response.LoginResponse;
import com.securityservice.security_service.model.dto.response.UserResponse;
import com.securityservice.security_service.model.entity.Role;
import com.securityservice.security_service.model.entity.User;
import com.securityservice.security_service.repository.BlacklistedTokenRepository;
import com.securityservice.security_service.repository.RoleRepository;
import com.securityservice.security_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {
        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Generar el token JWT
        String token = jwtService.generateToken(request.getUsername());

        // Devolver la respuesta con el token
        return new LoginResponse(token);
    }

    public UserResponse registerUser(RegisterUserRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Este usuario ya existe");
            }

            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(defaultRole);
            user.setEnabled(true);

            User savedUser = userRepository.save(user);

            request.setUserId(savedUser.getUserId());

            return new UserResponse(
                    savedUser.getUserId(),
                    savedUser.getUsername(),
                    savedUser.getRole().getName(),
                    savedUser.isEnabled()
            );
        } catch (Exception e) {
            // Añadir log para capturar excepciones
            System.err.println("Error al registrar el usuario: " + e.getMessage());
            throw e;
        }
    }
}
