package isi.dan.ms.pedidos.dto;

import lombok.Data;

@Data
public class StockUpdateDTO {
    private Long idProducto;
    private Integer cantidad;
}
