package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MunicipioRequest(
        @NotBlank @Size(max = 100) String nombre) {
}
