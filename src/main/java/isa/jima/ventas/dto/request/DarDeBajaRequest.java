package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DarDeBajaRequest(
        @NotBlank String motivo) {
}
