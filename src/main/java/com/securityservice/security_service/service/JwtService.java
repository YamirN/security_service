package com.securityservice.security_service.service;

import com.securityservice.security_service.model.entity.BlacklistedToken;
import com.securityservice.security_service.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.security.Key;
import java.util.UUID;

@Service
@Transactional
public class JwtService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public JwtService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public String generateToken(String username) {
        String tokenId = UUID.randomUUID().toString();
        return Jwts.builder()
                .setSubject(username) // Establecer el sujeto (usuario)
                .claim("tokenId", tokenId)
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
                .signWith(secretKey) // Firmar el token con la clave secreta
                .compact(); // Convertir a String
    }

    // Validar y extraer información del token JWT
    public Claims validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // Clave secreta para validar la firma
                .build()
                .parseClaimsJws(token) // Validar y parsear el token
                .getBody(); // Obtener los claims (información del token)

        // Verificar si el token está en la lista negra
        String tokenId = claims.get("tokenId", String.class);
        if (blacklistedTokenRepository.existsByTokenId(tokenId)) {
            throw new IllegalStateException("El token ha sido invalidado.");
        }
        return claims;
    }

    // Invalidar un token agregándolo a la lista negra
    public void invalidateToken(String token) {
        Claims claims = validateToken(token);
        String tokenId = claims.get("tokenId", String.class);
        String username = claims.getSubject();

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setTokenId(tokenId);
        blacklistedToken.setUsername(username);
        blacklistedToken.setExpirationDate(claims.getExpiration());

        blacklistedTokenRepository.save(blacklistedToken);
    }

}
