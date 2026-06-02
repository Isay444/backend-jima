package isa.jima.ventas.dto.response;

import java.time.LocalDateTime;

public record PuestoResponse(
        Integer idPuesto,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaRegistro) {
}
