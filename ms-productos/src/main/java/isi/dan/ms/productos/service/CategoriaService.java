package isi.dan.ms.productos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms.productos.dao.CategoriaRepository;
import isi.dan.ms.productos.model.Categoria;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria update(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }
}

