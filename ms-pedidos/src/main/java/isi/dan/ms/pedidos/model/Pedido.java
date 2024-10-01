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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Document(collection = "pedidos")
public class Pedido {

    // Solución sencilla al problema de la hora. Si no, pone la hora del meridiano de Greenwich
    public static final Integer GMT_ARGENTINA = -3;
    
    @Id
    private String id;
    
    private Instant fecha;
    private String observaciones;
    private BigDecimal total;
    
    @Field("historial_estados")
    private List<HistorialPedido> historialEstados;
    private EstadoPedido estado;
    private List<DetallePedido> detalles;

    private Integer idCliente;
    private Integer IdObra;
    private Integer idUsuario;

    public Pedido(PedidoDTO dto) {
        this.idCliente = dto.getIdCliente();
        this.IdObra = dto.getIdObra();
        this.idUsuario = dto.getIdUsuario();
        this.observaciones = dto.getObservaciones();
        this.fecha = Instant.now().plus(GMT_ARGENTINA, ChronoUnit.HOURS);
        this.total = BigDecimal.valueOf(0);
        this.historialEstados = new ArrayList<>();
        this.setEstado(EstadoPedido.INICIADO);
        
        this.detalles = new ArrayList<>();

        for (OrdenCompraDTO ordenCompra : dto.getProductos()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(ordenCompra.getIdProducto());
            detalle.setCantidad(ordenCompra.getCantidad());
            detalles.add(detalle);
        }
    }

    public EstadoPedido getEstado() {
        return this.estado;
        //return this.historialEstados.getLast().getEstado();
    }

    public void setEstado(EstadoPedido estado) {
        this.historialEstados.add(new HistorialPedido(estado));
        this.estado = estado;
    }
}

