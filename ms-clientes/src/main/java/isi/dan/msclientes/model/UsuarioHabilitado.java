package isi.dan.msclientes.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Table(name = "usuario_habilitado", schema = "ms_clientes")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = UsuarioHabilitado.class)
public class UsuarioHabilitado {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_habilitado")
    private Integer id;

    private String nombre;

    private String apellido;

    private Integer dni;

    @Email(message = "El correo electrónico es inválido")
    @Column(name = "correo_electronico")
    private String correoElectronico;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
