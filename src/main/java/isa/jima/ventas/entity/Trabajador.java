package isa.jima.ventas.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import isa.jima.ventas.validation.anotaciones.UpperCaseListener;
import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trabajador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UpperCaseListener.class)
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private Integer idTrabajador;

    @Column(nullable = false, length = 50)
    @Uppercase
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 50)
    @Uppercase
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    @Uppercase
    private String apellidoMaterno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area", nullable = false)
    private Area area;

    @Column(length = 13)
    private String rfc;

    @Column(length = 18)
    @Uppercase
    private String curp;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Column(name = "motivo_baja", length = 200)
    private String motivoBaja;

    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
