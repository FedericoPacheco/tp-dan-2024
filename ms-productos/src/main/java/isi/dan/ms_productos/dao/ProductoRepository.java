package isi.dan.ms_productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms_productos.modelo.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

