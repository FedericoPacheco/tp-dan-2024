package isi.dan.ms.productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms.productos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}

