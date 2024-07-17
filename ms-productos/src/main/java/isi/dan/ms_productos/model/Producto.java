package isi.dan.ms_productos.model;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "producto", schema = "ms_productos")
//@Data
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Producto.class)
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    private String nombre;

    private String descripcion;

    @Column(name ="stock_actual")
    @Min(value = 0, message = "El stock actual debe ser mayor o igual que 0")
    private Integer stockActual = 0;

    @Column(name ="stock_minimo")
    @Min(value = 0, message = "El stock minimo debe ser mayor o igual que 0")
    private Integer stockMinimo = 0;

    private BigDecimal precio;

    private BigDecimal descuentoPromocional = new BigDecimal(0.0);
    
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    /*
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(64)")
    private Categoria categoria;
    */

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", stockActual="
                + stockActual + ", stockMinimo=" + stockMinimo + ", precio=" + precio + ", descuentoPromocional="
                + descuentoPromocional + ", categoria=" + categoria + "]";
    }
}
