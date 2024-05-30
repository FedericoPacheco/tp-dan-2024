package isi.dan.ms.pedidos.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import isi.dan.ms.pedidos.model.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
}

