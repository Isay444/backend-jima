package isa.jima.ventas.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import isa.jima.ventas.entity.enums.CategoriaGastoFijo;
import isa.jima.ventas.entity.enums.EstatusGastoFijo;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gasto_fijo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto_fijo")
    private Integer idGastoFijo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaGastoFijo categoria;

    @Column(name = "monto_estimado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoEstimado;

    @Column(name = "monto_real", precision = 10, scale = 2)
    private BigDecimal montoReal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal provision;

    @Column(insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal ahorro;

    @Column(insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal excedente;

    @Column(insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal resto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatusGastoFijo estatus;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer anio;

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
