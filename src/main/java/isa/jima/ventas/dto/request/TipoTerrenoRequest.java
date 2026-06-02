package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoTerrenoRequest(
        @NotBlank @Size(max = 50) String nombre) {
}
