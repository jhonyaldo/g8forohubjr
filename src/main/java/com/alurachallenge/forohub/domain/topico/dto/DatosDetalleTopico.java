package com.alurachallenge.forohub.domain.topico.dto;

import com.alurachallenge.forohub.domain.topico.Topico;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para devolver una respuesta detallada de un Tópico.
 * Utiliza un constructor para facilitar la conversión de una entidad Topico a este DTO,
 * seleccionando y formateando los campos específicos que se mostrarán al cliente.
 */
public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status,
        String autor,
        String curso
) {
    // Constructor de conveniencia que mapea una entidad Topico a este DTO.
    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre(), // Obtenemos el nombre del autor desde la entidad relacionada
                topico.getCurso()
        );
    }
}