package isa.jima.ventas.dto.request;

import isa.jima.ventas.entity.enums.Banco;
import isa.jima.ventas.entity.enums.TipoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ReciboRequest(
        @NotNull(message = "El ID de la orden es obligatorio")
        Integer idOrden,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        @NotBlank(message = "El método de pago es obligatorio")
        String metodoPago,

        @NotNull(message = "El tipo de pago es obligatorio")
        TipoPago tipoPago,

        @NotNull(message = "Debe especificar si está confirmado")
        Boolean confirmado,

        Banco banco
) {
}