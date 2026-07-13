package isa.jima.ventas.dto.response;

import isa.jima.ventas.entity.enums.EstadoPlano;
import isa.jima.ventas.entity.enums.EstatusOrden;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrdenResponse(
        Integer id,
        String folio,
        EstatusOrden estatus,
        BigDecimal montoTotal,
        BigDecimal saldoRestante,
        BigDecimal montoComision,
        Boolean solicitaFactura,
        Boolean requierePlano,
        EstadoPlano estadoPlano,
        Boolean tieneComision,
        String comisionista,
        BigDecimal porcentajeComision,
        Boolean calcFechaLevantamientoAuto,
        Boolean calcFechaEntregaAuto,
        Integer diasEntregaPlano,
        LocalDate fecha,
        LocalDate fechaLevantamiento,
        LocalDate fechaEntregaPlano,
        String rutaPlano,
        String observaciones,


        // Relaciones resumidas para optimizar el JSON de salida
        ClienteInfo cliente,
        IngenieroInfo ingeniero,
        ServicioInfo servicio,
        SubtipoTerrenoInfo subtipoTerreno,
        MunicipioInfo municipio,
        ZonaEjidalInfo zonaEjidal,
        UsuarioInfo usuario
) {
    // Records anidados para los resúmenes de las entidades relacionadas
    public static record ClienteInfo(Integer id, String nombreCompleto) {}
    public record ServicioInfo(Integer id, String nombre) {}
    public record SubtipoTerrenoInfo(Integer id, String nombre) {}
    public record MunicipioInfo(Integer id, String nombre) {}
    public record ZonaEjidalInfo(Integer id, String nombre) {}
    public record IngenieroInfo(Integer id, String nombre) {}
    public record UsuarioInfo(Integer id, String nombre) {}
}
