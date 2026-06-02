package isa.jima.ventas.dto.response;

import java.math.BigDecimal;

public record ResumenNominaResponse(
        BigDecimal totalGeneral,
        BigDecimal totalPagado,
        BigDecimal totalPendiente) {
}
