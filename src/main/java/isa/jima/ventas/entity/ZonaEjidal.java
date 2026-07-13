package isa.jima.ventas.entity;

import isa.jima.ventas.validation.anotaciones.UpperCaseListener;
import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zona_ejidal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UpperCaseListener.class)
public class ZonaEjidal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona_ejidal")
    private Integer idZonaEjidal;

    @Column(nullable = false, length = 100)
    @Uppercase
    private String nombre;
}
