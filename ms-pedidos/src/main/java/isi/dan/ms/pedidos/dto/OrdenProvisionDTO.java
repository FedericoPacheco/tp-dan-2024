package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrdenProvisionDTO {
    private Integer idProducto;
    private Integer cantidad;
    private BigDecimal precio;
}