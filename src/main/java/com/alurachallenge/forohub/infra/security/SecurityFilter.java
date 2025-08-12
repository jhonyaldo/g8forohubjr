package com.alurachallenge.forohub.infra.security;

import com.alurachallenge.forohub.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull; // <-- Se añade este import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el token del header "Authorization".
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 2. Si el header existe y empieza con "Bearer", extraemos solo el token.
            var token = authHeader.replace("Bearer ", "");

            // 3. Validamos el token usando nuestro TokenService para obtener el subject (email).
            var subject = tokenService.getSubject(token);

            if (subject != null) {
                // 4. Si el token es válido y tenemos el email, buscamos el usuario en la base de datos.
                var usuario = usuarioRepository.findByEmail(subject);

                if (usuario != null) {
                    // 5. Si el usuario existe, creamos un objeto de autenticación.
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                    // 6. Forzamos la autenticación para esta solicitud.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 7. Llamamos al siguiente filtro en la cadena de seguridad de Spring.
        filterChain.doFilter(request, response);
    }
}