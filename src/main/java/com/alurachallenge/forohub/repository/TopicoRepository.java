package com.alurachallenge.forohub.repository;

import com.alurachallenge.forohub.domain.topico.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
}