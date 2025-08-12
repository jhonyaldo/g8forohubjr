package com.alurachallenge.forohub.controller;

import com.alurachallenge.forohub.domain.usuario.Usuario;
import com.alurachallenge.forohub.domain.usuario.dto.DatosAutenticacionUsuario;
import com.alurachallenge.forohub.infra.security.DatosJWTToken;
import com.alurachallenge.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DatosJWTToken> autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacion) {
        // 1. Creamos un objeto 'token' con las credenciales (email y contraseña).
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacion.email(),
                datosAutenticacion.contraseña()
        );

        // 2. Pasamos el token al AuthenticationManager.
        //    Spring Security usará nuestro AuthenticationService para verificar si el usuario existe y si la contraseña es correcta.
        //    Si las credenciales son incorrectas, lanzará una excepción que será manejada globalmente.
        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        // 3. Si la autenticación es exitosa, generamos el token JWT.
        //    Obtenemos el objeto 'Usuario' del principal de la autenticación.
        var jwtToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        // 4. Devolvemos una respuesta 200 OK con el token JWT en el cuerpo.
        return ResponseEntity.ok(new DatosJWTToken(jwtToken));
    }
}