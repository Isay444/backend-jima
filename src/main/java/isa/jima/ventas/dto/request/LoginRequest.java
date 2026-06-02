package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank (message = "El nombre de usuario es obligatorio") String nombre,
        @NotBlank (message = "La contraseña es obligatoria") String contrasenia
) {

}
