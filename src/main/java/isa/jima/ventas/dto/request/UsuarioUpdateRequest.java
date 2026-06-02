package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull Integer idRol,
        @NotNull Boolean activo) {
}
