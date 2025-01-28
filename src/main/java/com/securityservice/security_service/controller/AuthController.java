package com.securityservice.security_service.controller;

import com.securityservice.security_service.model.dto.request.LoginRequest;
import com.securityservice.security_service.model.dto.request.LogoutRequest;
import com.securityservice.security_service.model.dto.request.RegisterUserRequest;
import com.securityservice.security_service.model.dto.response.LoginResponse;
import com.securityservice.security_service.model.dto.response.UserResponse;
import com.securityservice.security_service.service.JwtService;
import com.securityservice.security_service.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.securityservice.security_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Llamar al servicio para autenticar y generar el token
        LoginResponse response = authService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Obtener el token del encabezado Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No se encontró un token válido");
        }

        String token = authHeader.substring(7);

        // Invalidar el token
        jwtService.invalidateToken(token);

        return ResponseEntity.ok("Sesión cerrada con éxito");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        ApiResponse<UserResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Usuario registrado exitosamente",
                userResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
