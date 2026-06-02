package isa.jima.ventas.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import isa.jima.ventas.entity.enums.CategoriaGastoFijo;
import isa.jima.ventas.entity.enums.EstatusGastoFijo;

public record GastoFijoResponse(
        Integer idGastoFijo,
        String descripcion,
        CategoriaGastoFijo categoria,
        BigDecimal montoEstimado,
        BigDecimal montoReal,
        BigDecimal provision,
        BigDecimal ahorro,
        BigDecimal excedente,
        BigDecimal resto,
        EstatusGastoFijo estatus,
        Integer mes,
        Integer anio,
        LocalDateTime fechaRegistro,
        LocalDateTime fechaActualizacion,
        Integer idUsuario) {
}
