package isi.dan.msclientes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.model.UsuarioHabilitado;

@Repository
public interface UsuarioHabilitadoRepository extends JpaRepository<UsuarioHabilitado, Integer> {

}
