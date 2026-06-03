package isa.jima.ventas.entity;

import isa.jima.ventas.entity.enums.Referenciado;
import isa.jima.ventas.entity.enums.TipoCliente;

import isa.jima.ventas.validation.anotaciones.Uppercase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clientes")
    private Integer idClientes;

    @Column(name = "nombre_s", length = 100)
    @Uppercase
    private String nombreS;

    @Column(name = "apellido_paterno", length = 50)
    @Uppercase
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    @Uppercase
    private String apellidoMaterno;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(length = 100)
    @Uppercase
    private String localidad;

    @Column(length = 100)
    @Uppercase
    private String colonia;

    @Column(name = "calle_numero", length = 200)
    @Uppercase
    private String calleNumero;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipio")
    private Municipio municipio;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipo;

    @Enumerated(EnumType.STRING)
    private Referenciado referenciado;

    @PrePersist
    @PreUpdate
    private void formatearYGenerarDireccion(){
        //2. Generar la direccion concatenada
        StringBuilder direccionaArmada = new StringBuilder();
        agregarParte(direccionaArmada, this.calleNumero);
        agregarParte(direccionaArmada, this.colonia);
        agregarParte(direccionaArmada, this.localidad);

        // obtener el nombre del municipio si existe la relacion
        if (this.municipio != null){
            agregarParte(direccionaArmada, this.municipio.getNombre());
        }
        agregarParte(direccionaArmada, this.codigoPostal);

        String resultadoFinal = direccionaArmada.toString().trim();

        //Fallback por si el usuario no lleno ningun campo de direccion (en BD es NOT NULL)
        this.direccion = resultadoFinal.isEmpty() ? "SIN DIRECCION ESPECIFICADA" : resultadoFinal;
    }

    private String toUpperCase(String valor){
        return valor != null ? valor.trim().toUpperCase() : null;
    }

    private void agregarParte(StringBuilder builder, String parte){
        if (parte != null && !parte.trim().isEmpty()){
            builder.append(parte.trim()).append(", ");
        }
    }
}
