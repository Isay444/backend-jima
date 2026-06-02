package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PuestoRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 200) String descripcion,
        Boolean activo) {
}
