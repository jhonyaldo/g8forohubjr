package com.alurachallenge.forohub.service;

import com.alurachallenge.forohub.domain.topico.Topico;
import com.alurachallenge.forohub.domain.topico.dto.DatosRegistroTopico;
import com.alurachallenge.forohub.domain.usuario.Usuario;
import com.alurachallenge.forohub.repository.TopicoRepository;
import com.alurachallenge.forohub.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TopicoService(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Topico crearTopico(DatosRegistroTopico datos) {
        // 1. Validar que el autor exista en la base de datos.
        Usuario autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un usuario con el ID proporcionado: " + datos.autorId()));

        // 2. Crear y guardar la nueva entidad Tópico usando el constructor personalizado.
        Topico nuevoTopico = new Topico(datos, autor);

        return topicoRepository.save(nuevoTopico);
    }

    // --- MÉTODO NUEVO AÑADIDO ---
    public Page<Topico> listarTopicos(Pageable paginacion) {
        // Simplemente delega la llamada al método findAll del repositorio.
        // Spring Data JPA se encarga de aplicar la paginación automáticamente.
        return topicoRepository.findAll(paginacion);
    }
}