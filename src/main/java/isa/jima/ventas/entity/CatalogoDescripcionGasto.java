package isa.jima.ventas.entity;

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
@Table(name = "catalogo_descripcion_gasto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoDescripcionGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo")
    private Integer idCatalogo;

    @Column(nullable = false, unique = true, length = 100)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;
}
