package isa.jima.ventas.dto.response;

public record UsuarioResponse(
        Integer idUsuario,
        String nombre,
        Boolean activo,
        Integer idRol,
        String nombreRol) {
}
