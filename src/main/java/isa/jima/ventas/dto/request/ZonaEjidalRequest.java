package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ZonaEjidalRequest(
        @NotBlank @Size(max = 100) String nombre) {
}
