package isa.jima.ventas.entity;

import isa.jima.ventas.validation.anotaciones.UpperCaseListener;
import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UpperCaseListener.class)
public class TipoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_servicio")
    private Integer idTipoServicio;

    @Column(nullable = false, length = 50)
    @Uppercase
    private String nombre;
}
