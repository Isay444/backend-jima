package isa.jima.ventas.dto.request;

import isa.jima.ventas.entity.enums.Referenciado;
import isa.jima.ventas.entity.enums.TipoCliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @Size(max = 100) String nombreS,
        @Size(max = 50) String apellidoPaterno,
        @Size(max = 50) String apellidoMaterno,
         // @NotBlank @Size(max = 200) String direccion,
        @NotBlank @Size(max = 100) String localidad,
        @Size(max = 100) String colonia,
        @Size(max = 200) String calleNumero,
        @Size(max = 10) String codigoPostal,
        Integer idMunicipio,
        @NotBlank @Size(max = 15) @Pattern(regexp = "^\\d{10}$", message = "El telefono debe tener 10 digitos") String telefono,
        @Email @Size(max = 100) String email,
        TipoCliente tipo,
        Referenciado referenciado) {
}
