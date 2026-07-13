package isa.jima.ventas.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import isa.jima.ventas.entity.enums.CategoriaGasto;
import isa.jima.ventas.entity.enums.MetodoPagoGasto;

import isa.jima.ventas.validation.anotaciones.UpperCaseListener;
import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gastos_diarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UpperCaseListener.class)
public class GastoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto")
    private Integer idGasto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 100)
    @Uppercase
    private String descripcion;

    @Column(name = "es_materia_prima", nullable = false)
    private Boolean esMateriaPrima;

    @Column(name = "tipo_materia_prima", length = 50)
    private String tipoMateriaPrima;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPagoGasto metodoPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "tiene_recibo", nullable = false)
    private Boolean tieneRecibo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaGasto categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
