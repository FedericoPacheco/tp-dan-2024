package isi.dan.ms_productos.dto;

import lombok.Data;

@Data
public class StockUpdateDTO {
    private Long idProducto;
    private Integer cantidad;
}
