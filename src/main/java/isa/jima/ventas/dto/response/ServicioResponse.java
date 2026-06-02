package isa.jima.ventas.dto.response;

public record ServicioResponse(
        Integer idServicio,
        String nombre,
        String descripcion,
        Integer idTipoServicio,
        String nombreTipoServicio) {
}
