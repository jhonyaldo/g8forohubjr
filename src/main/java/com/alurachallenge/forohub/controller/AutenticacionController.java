package com.alurachallenge.forohub.controller;

import com.alurachallenge.forohub.domain.usuario.Perfil;
import com.alurachallenge.forohub.domain.usuario.Usuario;
import com.alurachallenge.forohub.domain.usuario.dto.DatosAutenticacionUsuario;
import com.alurachallenge.forohub.infra.security.DatosJWTToken;
import com.alurachallenge.forohub.infra.security.TokenService;
import com.alurachallenge.forohub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService,
                                   UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<DatosJWTToken> autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacion) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacion.email(),
                datosAutenticacion.contraseña()
        );
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var jwtToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(jwtToken));
    }

    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<String> registrarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosRegistro) {
        if (usuarioRepository.findByEmail(datosRegistro.email()) != null) {
            return ResponseEntity.badRequest().body("Error: El email ya está en uso.");
        }

        String contraseñaHasheada = passwordEncoder.encode(datosRegistro.contraseña());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Usuario de Prueba");
        nuevoUsuario.setEmail(datosRegistro.email());
        nuevoUsuario.setContraseña(contraseñaHasheada);
        nuevoUsuario.setPerfil(Perfil.USER);
        nuevoUsuario.setActivo(true);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario de prueba registrado exitosamente con email: " + datosRegistro.email());
    }
}