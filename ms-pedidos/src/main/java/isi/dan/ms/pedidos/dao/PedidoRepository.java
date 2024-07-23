package isi.dan.ms.pedidos.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import isi.dan.ms.pedidos.model.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    public List<Pedido> findByIdCliente(Integer id);
}

