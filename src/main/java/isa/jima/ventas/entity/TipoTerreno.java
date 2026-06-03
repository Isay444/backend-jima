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
@Table(name = "tipo_terreno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoTerreno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_terreno")
    private Integer idTipoTerreno;

    @Column(nullable = false, length = 50)
    @Uppercase
    private String nombre;
}
