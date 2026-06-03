package isa.jima.ventas.entity;

import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingeniero")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
