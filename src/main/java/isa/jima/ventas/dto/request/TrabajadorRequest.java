package isa.jima.ventas.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrabajadorRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellidoPaterno,
        @Size(max = 50) String apellidoMaterno,
        @NotNull Integer idPuesto,
        @NotNull Integer idArea,
        @Size(min = 12, max = 13) String rfc,
        @Size(min = 18, max = 18) String curp,
        @Size(max = 15) String telefono,
        @Email @Size(max = 100) String email,
        @NotNull LocalDate fechaAlta,
        Boolean activo,
        Integer idUsuario) {
}
