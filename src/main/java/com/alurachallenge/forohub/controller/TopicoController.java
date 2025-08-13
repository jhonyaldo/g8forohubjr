package com.alurachallenge.forohub.controller;

import com.alurachallenge.forohub.domain.topico.Topico;
import com.alurachallenge.forohub.domain.topico.dto.DatosActualizarTopico;
import com.alurachallenge.forohub.domain.topico.dto.DatosDetalleTopico;
import com.alurachallenge.forohub.domain.topico.dto.DatosListadoTopico;
import com.alurachallenge.forohub.domain.topico.dto.DatosRegistroTopico;
import com.alurachallenge.forohub.repository.TopicoRepository;
import com.alurachallenge.forohub.service.TopicoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;
    private final TopicoRepository topicoRepository;

    @Autowired
    public TopicoController(TopicoService topicoService, TopicoRepository topicoRepository) {
        this.topicoService = topicoService;
        this.topicoRepository = topicoRepository;
    }

    // Endpoint para CREAR un nuevo tópico
    @PostMapping
    @Transactional
    public ResponseEntity<DatosListadoTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistro,
                                                              UriComponentsBuilder uriBuilder) {
        Topico topicoCreado = topicoService.crearTopico(datosRegistro);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoCreado.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosListadoTopico(topicoCreado));
    }

    // Endpoint para LISTAR todos los tópicos con paginación
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = {"fechaCreacion"}) Pageable paginacion) {

        Page<Topico> paginaDeTopicos = topicoRepository.findByActivoTrue(paginacion); // Usando el método para activos
        Page<DatosListadoTopico> datosPagina = paginaDeTopicos.map(DatosListadoTopico::new);
        return ResponseEntity.ok(datosPagina);
    }

    // Endpoint para DETALLAR un tópico por ID
    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado con ID: " + id));
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    // ===================================================================
    // AQUÍ EMPIEZAN LOS NUEVOS MÉTODOS A AÑADIR
    // ===================================================================

    // Endpoint para ACTUALIZAR un tópico por ID
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosDetalleTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizar) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado con ID: " + id));

        topico.actualizarDatos(datosActualizar);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    // Endpoint para ELIMINAR (lógicamente) un tópico por ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado con ID: " + id));

        topico.desactivar();

        return ResponseEntity.noContent().build();
    }
}