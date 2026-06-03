package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record IngenieroRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String especialidad,
        @NotBlank @Size(max = 15)  @Pattern(regexp = "^\\d{10}$", message = "El telefono debe tener 10 digitos") String telefono) {
}
