package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordRequest(
        @NotBlank String contraseniaActual,
        @NotBlank @Size(min = 6, max = 100) String contraseniaNueva) {
}
