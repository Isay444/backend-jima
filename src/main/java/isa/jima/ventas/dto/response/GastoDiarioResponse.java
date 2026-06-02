package isa.jima.ventas.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import isa.jima.ventas.entity.enums.CategoriaGasto;
import isa.jima.ventas.entity.enums.MetodoPagoGasto;

public record GastoDiarioResponse(
        Integer idGasto,
        LocalDate fecha,
        String descripcion,
        Boolean esMateriaPrima,
        String tipoMateriaPrima,
        MetodoPagoGasto metodoPago,
        BigDecimal monto,
        Boolean tieneRecibo,
        CategoriaGasto categoria,
        Integer idUsuario,
        String nombreUsuario,
        LocalDateTime fechaRegistro) {
}
