package isi.dan.msclientes.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "obra", schema = "ms_clientes")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Obra.class)
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obra")
    private Integer id;
    
    private String direccion;

    // No se porque el proyecto trae este atributo
    //@Column(name = "es_remodelacion")
    //private Boolean esRemodelacion;
    
    private BigDecimal latitud;
    
    private BigDecimal longitud;
    
    private BigDecimal presupuesto;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(16)")
    //@Type(PostgreSQLEnumType.class)
    //@JdbcType(PostgreSQLEnumJdbcType.class)
    //@JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoObra estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
