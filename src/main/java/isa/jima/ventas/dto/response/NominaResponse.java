package isa.jima.ventas.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import isa.jima.ventas.entity.enums.EstatusPago;
import isa.jima.ventas.entity.enums.MetodoPagoNomina;
import isa.jima.ventas.entity.enums.Periodicidad;

public record NominaResponse(
        Integer idNomina,
        Integer idTrabajador,
        String nombreTrabajador,
        BigDecimal sueldo,
        Periodicidad periodicidad,
        MetodoPagoNomina metodoPago,
        LocalDate fechaPago,
        LocalDate periodoInicio,
        LocalDate periodoFin,
        EstatusPago estatusPago,
        String observaciones,
        Integer idUsuario,
        String nombreUsuario,
        LocalDateTime fechaRegistro) {
}
