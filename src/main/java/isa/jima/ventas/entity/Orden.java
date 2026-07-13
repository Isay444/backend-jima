package isa.jima.ventas.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import isa.jima.ventas.entity.enums.EstadoPlano;
import isa.jima.ventas.entity.enums.EstatusOrden;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;

    @Column(nullable = false, unique = true, length = 20)
    private String folio;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatusOrden estatus;

    @Column(name = "fecha_entrega_plano")
    private LocalDate fechaEntregaPlano;

    @Column(name = "fecha_levantamiento")
    private LocalDate fechaLevantamiento;

    @Column(name = "monto_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "solicita_factura", nullable = false, columnDefinition = "bit(1)")
    private Boolean solicitaFactura;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_plano")
    private EstadoPlano estadoPlano;

    @Column(name = "requiere_plano", nullable = false, columnDefinition = "bit(1)")
    private Boolean requierePlano;

    @Column(name = "ruta_plano")
    private String rutaPlano;

    @Column(length = 100)
    private String comisionista;

    @Column(name = "monto_comision", precision = 10, scale = 2)
    private BigDecimal montoComision;

    @Column(name = "porcentaje_comision", precision = 5, scale = 2)
    private BigDecimal porcentajeComision;

    @Column(name = "tiene_comision", columnDefinition = "bit(1)")
    private Boolean tieneComision;

    @Column(name = "saldo_restante", precision = 10, scale = 2)
    private BigDecimal saldoRestante;

    @Column(name = "calc_fecha_levantamiento_auto", nullable = false, columnDefinition = "tinyint(1)")
    private Boolean calcFechaLevantamientoAuto;

    @Column(name = "calc_fecha_entrega_auto", nullable = false, columnDefinition = "tinyint(1)")
    private Boolean calcFechaEntregaAuto;

    @Column(name = "dias_entrega_plano", nullable = false)
    private Integer diasEntregaPlano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clientes", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subtipo_terreno", nullable = false)
    private SubtipoTerreno subtipoTerreno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipios", nullable = false)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona_ejidal", nullable = false)
    private ZonaEjidal zonaEjidal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingeniero", nullable = false)
    private Ingeniero ingeniero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", fetch = FetchType.LAZY)
    private List<Recibo> recibos = new ArrayList<>();
}
