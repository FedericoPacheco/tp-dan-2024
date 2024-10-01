package isi.dan.ms.clientes.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente", schema = "ms_clientes")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    private String nombre;

    @Email(message = "El correo electrónico es inválido")
    @Column(name = "correo_electronico")
    private String correoElectronico;

    private String cuit;

    @Value("${ms-clientes.default-maximo-descubierto}")
    @Min(value = 1, message = "El maximo descubierto debe ser mayor que 0")
    @Column(name = "maximo_descubierto")
    private BigDecimal maximoDescubierto;

    @Column(name = "maxima_cantidad_obras_en_ejecucion")
    private Integer maximaCantidadObrasEnEjecucion;

    @Column(name = "obras_asignadas")
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Obra> obrasAsignadas = new ArrayList<>();

    @Column(name = "usuarios_habilitados")
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<UsuarioHabilitado> usuariosHabilitados = new ArrayList<>();

    public BigDecimal getDescubierto() {
        BigDecimal descubierto = new BigDecimal(0.0);
        for (Obra obra: obrasAsignadas)
            if (obra.getPresupuesto() != null)
                descubierto.add(obra.getPresupuesto());
        return descubierto;
    }

    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nombre=" + nombre + ", correoElectronico=" + correoElectronico + ", cuit="
                + cuit + ", maximoDescubierto=" + maximoDescubierto + ", maximaCantidadObrasEnEjecucion="
                + maximaCantidadObrasEnEjecucion + "]";
    }
}
