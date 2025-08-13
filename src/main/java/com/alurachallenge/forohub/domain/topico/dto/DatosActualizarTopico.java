package com.alurachallenge.forohub.domain.topico.dto;

// Este DTO no necesita validaciones como @NotBlank, ya que los campos son opcionales.
// El cliente puede enviar uno, el otro, o ambos. La validación de que no vengan vacíos
// si se envían se hará en la lógica de actualización.
public record DatosActualizarTopico(
        String titulo,
        String mensaje
) {
}