package isa.jima.ventas.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import isa.jima.ventas.entity.enums.CategoriaGasto;
import isa.jima.ventas.entity.enums.MetodoPagoGasto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GastoDiarioRequest(
        @NotNull LocalDate fecha,
        @NotBlank @Size(max = 100) String descripcion,
        @NotNull Boolean esMateriaPrima,
        @Size(max = 50) String tipoMateriaPrima,
        @NotNull MetodoPagoGasto metodoPago,
        @NotNull @DecimalMin("0.01") BigDecimal monto,
        @NotNull Boolean tieneRecibo,
        @NotNull CategoriaGasto categoria,
        @NotNull Integer idUsuario) {
}
