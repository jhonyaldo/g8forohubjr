package com.alurachallenge.forohub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration le dice a Spring que esta es una clase de configuración.
@Configuration
// @EnableWebSecurity activa el módulo de seguridad web de Spring.
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    @Autowired
    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    // Este método define la cadena de filtros de seguridad.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 1. Desactivamos la protección CSRF. Es una protección para ataques en aplicaciones
                //    web tradicionales con sesiones, pero no es necesaria para una API REST sin estado (STATELESS).
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configuramos la gestión de sesiones como STATELESS.
                //    Esto le dice a Spring que no cree sesiones, ya que usaremos tokens JWT.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Definimos las reglas de autorización para las peticiones HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permitimos las peticiones POST a "/login" sin autenticación.
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        // Todas las demás peticiones deben ser autenticadas.
                        .anyRequest().authenticated()
                )

                // 4. Añadimos nuestro filtro personalizado (SecurityFilter) para que se ejecute
                //    antes del filtro de autenticación por defecto de Spring.
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Este bean expone el AuthenticationManager de Spring para que podamos usarlo en nuestro
    // controlador de login para autenticar a los usuarios.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Este bean le dice a Spring qué algoritmo de encriptación de contraseñas usar.
    // BCrypt es el estándar de oro para el hashing de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}