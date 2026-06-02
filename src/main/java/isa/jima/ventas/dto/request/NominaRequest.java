package isa.jima.ventas.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import isa.jima.ventas.entity.enums.EstatusPago;
import isa.jima.ventas.entity.enums.MetodoPagoNomina;
import isa.jima.ventas.entity.enums.Periodicidad;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NominaRequest(
        @NotNull Integer idTrabajador,
        @NotNull @DecimalMin("0.01") BigDecimal sueldo,
        @NotNull Periodicidad periodicidad,
        @NotNull MetodoPagoNomina metodoPago,
        @NotNull LocalDate fechaPago,
        LocalDate periodoInicio,
        LocalDate periodoFin,
        @NotNull EstatusPago estatusPago,
        @Size(max = 500) String observaciones,
        @NotNull Integer idUsuario) {
}
