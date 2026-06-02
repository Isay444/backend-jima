package isa.jima.ventas.dto.response;

public record PermisoResponse(
        Integer idPermiso,
        String nombre,
        String descripcion,
        String entidad,
        String accion) {
}
