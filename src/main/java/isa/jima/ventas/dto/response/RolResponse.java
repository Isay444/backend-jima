package isa.jima.ventas.dto.response;

import java.util.List;

public record RolResponse(
        Integer idRol,
        String nombre,
        String descripcion,
        Boolean activo,
        List<PermisoResponse> permisos) {
}
