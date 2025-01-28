package com.securityservice.security_service.security;

import com.securityservice.security_service.model.entity.BlacklistedToken;
import com.securityservice.security_service.repository.BlacklistedTokenRepository;
import com.securityservice.security_service.service.CustomUserDetailsService;
import com.securityservice.security_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService, BlacklistedTokenRepository blacklistedTokenRepository) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtener el token del encabezado Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Eliminar "Bearer " del token

            Claims claims = jwtService.validateToken(token);

            String tokenId = claims.get("tokenId", String.class);
            if (blacklistedTokenRepository.existsByTokenId(tokenId)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido");
                return;
            }

            // Validar el token y obtener el nombre de usuario
            String username = jwtService.validateToken(token).getSubject();

            // Cargar los detalles del usuario
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Crear una autenticación y establecerla en el contexto de seguridad
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
