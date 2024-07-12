package isi.dan.ms_productos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ActualizarStockDTO {
    private Integer idProducto;
    private Integer cantidad;
}

