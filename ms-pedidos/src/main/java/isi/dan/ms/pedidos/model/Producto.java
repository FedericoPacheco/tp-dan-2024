package isi.dan.ms.pedidos.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
//@Data
// Ignorar campos no están en esta clase pero que son parte de la respuesta de la API de productos
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Producto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private BigDecimal descuentoPromocional;
}
