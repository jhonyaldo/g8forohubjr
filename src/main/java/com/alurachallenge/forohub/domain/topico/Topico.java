package com.alurachallenge.forohub.domain.topico;

import com.alurachallenge.forohub.domain.topico.dto.DatosActualizarTopico;
import com.alurachallenge.forohub.domain.topico.dto.DatosRegistroTopico;
import com.alurachallenge.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private String curso;
    private String categoria;

    private Boolean activo; // <-- CAMPO NUEVO AÑADIDO

    public Topico(DatosRegistroTopico datosRegistro, Usuario autor) {
        this.activo = true; // <-- SE INICIALIZA EL CAMPO
        this.titulo = datosRegistro.titulo();
        this.mensaje = datosRegistro.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = "ABIERTO";
        this.autor = autor;
        this.curso = datosRegistro.curso();
        this.categoria = datosRegistro.categoria();
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizar) {
        if (datosActualizar.titulo() != null && !datosActualizar.titulo().isBlank()) {
            this.titulo = datosActualizar.titulo();
        }
        if (datosActualizar.mensaje() != null && !datosActualizar.mensaje().isBlank()) {
            this.mensaje = datosActualizar.mensaje();
        }
    }

    // --- MÉTODO NUEVO AÑADIDO ---
    public void desactivar() {
        this.activo = false;
    }
}