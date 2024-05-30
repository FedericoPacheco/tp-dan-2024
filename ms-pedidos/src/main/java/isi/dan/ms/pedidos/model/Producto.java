package isi.dan.ms.pedidos.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;

}
