package isi.dan.ms.pedidos.dto;

import java.util.LinkedList;
import java.util.List;

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
public class PedidoDTO {

    private Integer idCliente;
    private Integer idObra;
    private Integer idUsuario;
    private String observaciones;
    List<OrdenCompraDTO> productos = new LinkedList<>();

    public PedidoDTO(Integer idCliente, Integer idObra, Integer idUsuario, String observaciones) {
        this.idCliente = idCliente;
        this.idObra = idObra;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
    }
}
