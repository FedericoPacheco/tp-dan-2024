package isi.dan.ms.pedidos.model;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    // No sé qué es el "userEstado" que menciona el enunciado

    public HistorialPedido() {
        this.estado = EstadoPedido.INICIADO;
        this.fecha = Instant.now();
    }
}
