package com.alurachallenge.forohub.domain.usuario;

import com.alurachallenge.forohub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// @Service le dice a Spring que esta es una clase de servicio y debe gestionarla.
@Service
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Inyectamos el repositorio de usuarios que necesitamos para buscar en la BD.
    @Autowired
    public AuthenticationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Este es el único método que debemos implementar de la interfaz UserDetailsService.
    // Spring Security lo llamará automáticamente durante el proceso de autenticación.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // "username" en el contexto de Spring Security es el identificador del usuario.
        // En nuestro caso, hemos decidido que el email será nuestro "username".
        // Simplemente, delegamos la búsqueda a nuestro repositorio.
        return usuarioRepository.findByEmail(username);
    }
}