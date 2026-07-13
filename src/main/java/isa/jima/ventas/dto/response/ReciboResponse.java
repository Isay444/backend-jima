package isa.jima.ventas.dto.response;

import isa.jima.ventas.entity.enums.Banco;
import isa.jima.ventas.entity.enums.TipoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReciboResponse(
        Integer id,
        LocalDate fecha,
        BigDecimal monto,
        String metodoPago,
        TipoPago tipoPago,
        Boolean confirmado,
        Banco banco,
        OrdenInfo orden
) {
    // Record anidado para mostrar contexto de la orden en la tabla de recibos
    public record OrdenInfo(
            Integer id,
            String folio,
            BigDecimal montoTotal,
            BigDecimal saldoRestante
    ) {}
}