package isa.jima.ventas.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record TrabajadorRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellidoPaterno,
        @Size(max = 50) String apellidoMaterno,
        @NotNull Integer idPuesto,
        @NotNull Integer idArea,
        @Size(min = 12, max = 13) String rfc,
        @Size(min = 18, max = 18) String curp,
        @Size(max = 15) @Pattern(regexp = "^\\d{10}$", message = "El telefono debe tener 10 digitos") String telefono,
        @Email @Size(max = 100) String email,
        @NotNull LocalDate fechaAlta,
        Boolean activo,
        Integer idUsuario) {
}
