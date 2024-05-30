package isi.dan.ms_productos.model;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ms_productos_producto")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    private String nombre;

    private String descripcion;

    @Column(name ="stock_actual")
    private int stockActual;

    @Column(name ="stock_minimo")
    private int stockMinimo;

    private BigDecimal precio;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
}
