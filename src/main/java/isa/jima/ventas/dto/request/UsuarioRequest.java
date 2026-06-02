package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(min = 6, max = 100) String contrasenia,
        @NotNull Integer idRol,
        @NotNull Boolean activo) {
}
