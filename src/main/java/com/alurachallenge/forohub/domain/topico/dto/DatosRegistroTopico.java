package com.alurachallenge.forohub.domain.topico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Este 'record' define los datos necesarios para registrar un nuevo Tópico.
// Usamos anotaciones de Jakarta Bean Validation para asegurar la integridad de los datos.
public record DatosRegistroTopico(
        // El título del tópico no puede ser nulo ni estar vacío.
        @NotBlank
        String titulo,

        // El mensaje del tópico no puede ser nulo ni estar vacío.
        @NotBlank
        String mensaje,

        // El ID del autor que crea el tópico no puede ser nulo.
        @NotNull
        Long autorId,

        // El nombre del curso asociado al tópico no puede ser nulo ni estar vacío.
        @NotBlank
        String curso,

        // La categoría del curso no puede ser nula ni estar vacía.
        @NotBlank
        String categoria
) {
}