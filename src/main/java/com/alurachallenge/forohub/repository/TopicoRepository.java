package com.alurachallenge.forohub.repository;

import com.alurachallenge.forohub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // --- MÉTODO NUEVO AÑADIDO ---
    // Consulta derivada para encontrar solo los tópicos donde el campo 'activo' es true.
    // Spring Data JPA la implementará automáticamente.
    Page<Topico> findByActivoTrue(Pageable paginacion);

}