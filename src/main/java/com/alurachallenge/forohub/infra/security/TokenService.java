package com.alurachallenge.forohub.infra.security;

import com.alurachallenge.forohub.domain.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    // Método para obtener la clave de firma a partir del secreto del application.properties
    private Key getSigningKey() {
        // Usamos los bytes del string secreto directamente para crear la clave.
        return Keys.hmacShaKeyFor(this.apiSecret.getBytes());
    }

    public String generarToken(Usuario usuario) {
        try {
            return Jwts.builder()
                    .setIssuer("Foro Hub")
                    .setSubject(usuario.getEmail())
                    .claim("id", usuario.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // 2 horas
                    .signWith(getSigningKey()) // Usamos la clave fija
                    .compact();
        } catch (Exception e){
            throw new RuntimeException("Error al generar el token JWT", e);
        }
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException("Token no puede ser nulo");
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Usamos la misma clave fija
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token JWT inválido o expirado", e);
        }
    }
}