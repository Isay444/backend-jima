package isa.jima.ventas.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RolRequest(
        @NotBlank @Size(max = 50) String nombre,
        @Size(max = 200) String descripcion,
        @NotNull Boolean activo,
        @NotNull @NotEmpty List<Integer> idPermisos) {
}
