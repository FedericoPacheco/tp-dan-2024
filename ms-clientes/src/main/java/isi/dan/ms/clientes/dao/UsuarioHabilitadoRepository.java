package isi.dan.ms.clientes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.ms.clientes.model.UsuarioHabilitado;

@Repository
public interface UsuarioHabilitadoRepository extends JpaRepository<UsuarioHabilitado, Integer> {

}
