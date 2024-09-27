package isi.dan.msclientes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.model.UsuarioHabilitado;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioHabilitadoRepository extends JpaRepository<UsuarioHabilitado, Integer> {
        Optional<UsuarioHabilitado> findByCorreoElectronico(String email);

        List<UsuarioHabilitado> findUsuarioHabilitadosByClienteId(Integer idCliente);
}
