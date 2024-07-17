package isi.dan.ms.pedidos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrdenCompraDTO {
    private Integer idProducto;
    private Integer cantidad;
}