package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Data
// Ignorar campos no están en esta clase pero que son parte de la respuesta de la API de productos
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ProductoDTO {

    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private BigDecimal descuentoPromocional;
}
