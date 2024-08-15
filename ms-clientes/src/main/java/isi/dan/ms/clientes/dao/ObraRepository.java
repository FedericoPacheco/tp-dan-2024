package isi.dan.ms.clientes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.ms.clientes.model.EstadoObra;
import isi.dan.ms.clientes.model.Obra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer> {
    List<Obra> findByEstado(EstadoObra estado);
}

