package isi.dan.ms.pedidos.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class HistorialPedido {
    
    EstadoPedido estado;
    Instant fecha;
    String detalle;
    
    public HistorialPedido(EstadoPedido estado) {
        this.estado = estado;
        this.fecha = Instant.now().minus(Pedido.GMT_ARGENTINA, ChronoUnit.HOURS);
        this.detalle = "";
    }
}
