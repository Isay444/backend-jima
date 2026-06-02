package isa.jima.ventas.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TrabajadorResponse(
        Integer idTrabajador,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        Integer idPuesto,
        String nombrePuesto,
        Integer idArea,
        String nombreArea,
        String rfc,
        String curp,
        String telefono,
        String email,
        LocalDate fechaAlta,
        LocalDate fechaBaja,
        String motivoBaja,
        Boolean activo,
        Integer idUsuario,
        String nombreUsuario,
        LocalDateTime fechaRegistro) {
}
