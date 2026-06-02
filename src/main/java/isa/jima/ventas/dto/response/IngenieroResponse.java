package isa.jima.ventas.dto.response;

public record IngenieroResponse(
        Integer idIngeniero,
        String nombre,
        String especialidad,
        String telefono) {
}
