package isa.jima.ventas.dto.response;

import isa.jima.ventas.entity.enums.Referenciado;
import isa.jima.ventas.entity.enums.TipoCliente;

public record ClienteResponse(
        Integer idClientes,
        String nombreS,
        String apellidoPaterno,
        String apellidoMaterno,
        String direccion,
        String localidad,
        String colonia,
        String calleNumero,
        String codigoPostal,
        Integer idMunicipio,
        String nombreMunicipio,
        String telefono,
        String email,
        TipoCliente tipo,
        Referenciado referenciado) {
}
