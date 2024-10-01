package isi.dan.ms.clientes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.ms.clientes.model.Cliente;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

        Optional<Cliente> findByCorreoElectronico(String email);
        Optional<Cliente> findByCuit(String cuit);
}
