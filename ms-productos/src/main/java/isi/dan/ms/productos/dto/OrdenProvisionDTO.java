package isi.dan.ms.productos.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrdenProvisionDTO {
    private Integer idProducto;
    private Integer cantidad;
    private BigDecimal precio;
}