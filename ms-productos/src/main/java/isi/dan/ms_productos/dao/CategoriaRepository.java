package isi.dan.ms_productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms_productos.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}

