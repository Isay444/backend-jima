package isa.jima.ventas.dto.request;

import isa.jima.ventas.entity.enums.EstadoPlano;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrdenRequest (
        @NotNull(message = "El cliente es obligatorio")
        Integer idCliente,
        @NotNull(message = "El servicio es obligatorio")
        Integer idServicio,
        @NotNull(message = "El terreno es obligatorio")
        Integer idSubtipoTerreno,
        @NotNull(message = "El municipio es obligatorio")
        Integer idMunicipio,
        @NotNull(message = "La localidad es obligatorio")
        Integer idZonaEjidal,
        @NotNull(message = "El ingeniero es obligatorio")
        Integer idIngeniero,
        @NotNull(message = "El usuario es obligatorio")
        Integer idUsuario,
        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        String observaciones,

        @NotNull(message = "El monto total es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto total debe ser mayor a 0")
        BigDecimal montoTotal,
        @NotNull(message = "Debe especificar si solicita factura")
        Boolean solicitaFactura,
        @NotNull(message = "Debe especificar si requiere plano")
        Boolean requierePlano,
        EstadoPlano estadoPlano,
        @NotNull(message = "Debe especificar si tiene comisión")
        Boolean tieneComision,
        // Opcionales dependiendo de tieneComision
        String comisionista,
        BigDecimal porcentajeComision,
        @NotNull(message = "Debe especificar si el calculo de Fecha Levantamiento es automático")
        Boolean calcFechaLevantamientoAuto,
        @NotNull(message = "Debe especificar si el calculo de Fecha Entrega es automático")
        Boolean calcFechaEntregaAuto,
        @NotNull(message = "Los dias de entrega del plano son obligatorios")
        Integer diasEntregaPlano,
        // Opcionales dependiendo de los flags auto
        LocalDate fechaLevantamiento,
        LocalDate fechaEntregaPlano
        ) {
}
