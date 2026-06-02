package isa.jima.ventas.dto.response;

import java.time.LocalDateTime;

public record AreaResponse(
        Integer idArea,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaRegistro) {
}
