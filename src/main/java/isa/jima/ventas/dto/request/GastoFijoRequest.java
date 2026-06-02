package isa.jima.ventas.dto.request;

import java.math.BigDecimal;

import isa.jima.ventas.entity.enums.CategoriaGastoFijo;
import isa.jima.ventas.entity.enums.EstatusGastoFijo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GastoFijoRequest(
        @NotBlank @Size(max = 100) String descripcion,
        @NotNull CategoriaGastoFijo categoria,
        @NotNull @DecimalMin("0.00") BigDecimal montoEstimado,
        BigDecimal montoReal,
        @NotNull @DecimalMin("0.00") BigDecimal provision,
        @NotNull EstatusGastoFijo estatus,
        @NotNull @Min(1) @Max(12) Integer mes,
        @NotNull @Min(2000) Integer anio,
        Integer idUsuario) {
}
