package isi.dan.ms.productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms.productos.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}

