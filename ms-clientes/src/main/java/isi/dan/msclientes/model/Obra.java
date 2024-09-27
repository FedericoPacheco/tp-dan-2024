package isi.dan.msclientes.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "obra", schema = "ms_clientes")
@Data
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obra")
    private Integer id;
    
    private String direccion;
    
    private BigDecimal latitud;
    
    private BigDecimal longitud;
    
    private BigDecimal presupuesto;

    @Enumerated(EnumType.STRING)
    private EstadoObra estado;

    @ManyToOne()
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
