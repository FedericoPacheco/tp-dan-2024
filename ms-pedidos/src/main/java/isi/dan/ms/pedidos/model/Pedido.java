package isi.dan.ms.pedidos.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import isi.dan.ms.pedidos.dto.OrdenCompraDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pedidos")
//@Data
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Pedido {
    
    @Id
    private String id;
    
    private Instant fecha;
    // Para qué un número de pedido si ya tenés el id?
    //private Integer numeroPedido;
    private String observaciones;
    private BigDecimal total;
    
    @Field("historial_estados")
    private List<HistorialPedido> historialEstados;
    private List<DetallePedido> detalles;

    // private Cliente cliente;
    private Integer idCliente;
    private Integer IdObra;
    private Integer idUsuario;

    public Pedido(PedidoDTO dto) {
        this.idCliente = dto.getIdCliente();
        this.IdObra = dto.getIdObra();
        this.idUsuario = dto.getIdUsuario();
        this.observaciones = dto.getObservaciones();
        this.fecha = Instant.now();
        this.total = BigDecimal.valueOf(0);
        this.historialEstados = new ArrayList<>();
        this.historialEstados.add(new HistorialPedido());
        
        this.detalles = new ArrayList<>();
        for (OrdenCompraDTO ordenCompra : dto.getProductos()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(ordenCompra.getIdProducto());
            detalle.setCantidad(ordenCompra.getCantidad());
            detalles.add(detalle);
        }
    }

    public EstadoPedido getEstado() {
        return this.historialEstados.getLast().getEstado();
    }

    public void setEstado(EstadoPedido estado) {
        this.historialEstados.add(new HistorialPedido(estado));
    }
}

