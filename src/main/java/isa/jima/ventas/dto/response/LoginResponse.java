package isa.jima.ventas.dto.response;

import java.util.List;

public record LoginResponse(
    String token,
    String nombre,
    Integer idUsuario,
    String nombreRol,
    List<String> permisos // (lista de strings ENTIDAD_ACCION, ej: ["CLIENTE_CREATE", "ORDEN_READ"])
) {

}
