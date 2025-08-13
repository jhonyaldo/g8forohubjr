package com.alurachallenge.forohub.domain.topico.dto;

import com.alurachallenge.forohub.domain.topico.Topico;
import java.time.LocalDateTime;

// Este 'record' define los datos que devolveremos al listar los Tópicos.
// Es una buena práctica no devolver la entidad JPA completa, sino una vista simplificada.
public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion
) {
    // Creamos un constructor adicional que facilita la conversión
    // de una entidad 'Topico' a este DTO.
    public DatosListadoTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion());
    }
}