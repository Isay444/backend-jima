package isa.jima.ventas.entity;

import isa.jima.ventas.validation.anotaciones.UpperCaseListener;
import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingeniero")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UpperCaseListener.class)
public class Ingeniero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingeniero")
    private Integer idIngeniero;

    @Column(nullable = false, length = 100)
    @Uppercase
    private String nombre;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(nullable = false, length = 15)
    private String telefono;
}
