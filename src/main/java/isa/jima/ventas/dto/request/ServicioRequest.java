package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServicioRequest(
        @NotBlank @Size(max = 200) String nombre,
        String descripcion,
        @NotNull Integer idTipoServicio) {
}
