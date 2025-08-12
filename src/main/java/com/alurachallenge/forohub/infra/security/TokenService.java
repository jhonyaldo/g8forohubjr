package com.alurachallenge.forohub.infra.security;

import com.alurachallenge.forohub.domain.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    // Clave de firma para JWT (se genera una vez y se reutiliza)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generarToken(Usuario usuario) {
        try {
            return Jwts.builder()
                    .setIssuer("Foro Hub") // Quién emite el token
                    .setSubject(usuario.getEmail()) // A quién pertenece el token (el usuario)
                    .claim("id", usuario.getId()) // Podemos añadir información extra (claims)
                    .setIssuedAt(new Date()) // Fecha de emisión
                    .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // Caduca en 2 horas (en milisegundos)
                    .signWith(secretKey) // Firmamos el token con nuestra clave secreta
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
                    .setSigningKey(secretKey) // Usamos la misma clave para verificar
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // Maneja el caso en que el token no sea válido (firma incorrecta, expirado, etc.)
            throw new RuntimeException("Token JWT inválido o expirado", e);
        }
    }
}