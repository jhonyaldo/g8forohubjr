package com.alurachallenge.forohub.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String contraseña;
    private String perfil;
    private Boolean activo;

    // ===================================================================
    // MÉTODOS DE LA INTERFAZ UserDetails (PARA SPRING SECURITY)
    // Estos métodos le dicen a Spring Security cómo interactuar con nuestro Usuario.
    // ===================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define los roles/permisos del usuario.
        // Por ahora, todos los usuarios tendrán el rol "ROLE_USER".
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // Spring Security necesita este método para obtener la contraseña
        // encriptada y compararla con la que el usuario envía al hacer login.
        return contraseña;
    }

    @Override
    public String getUsername() {
        // En nuestro sistema, el "username" que se usará para el login
        // será el email del usuario.
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Para este proyecto, las cuentas no expiran.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Para este proyecto, las cuentas no se bloquean.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Para este proyecto, las credenciales no expiran.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Para este proyecto, las cuentas están siempre habilitadas.
        // En un futuro, podríamos conectar esto a nuestro campo 'activo'.
        return true;
    }
}