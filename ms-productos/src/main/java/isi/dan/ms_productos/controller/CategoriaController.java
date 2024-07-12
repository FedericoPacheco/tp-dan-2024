package isi.dan.ms_productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.model.Categoria;
import isi.dan.ms_productos.service.CategoriaService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria producto) {
        return ResponseEntity.ok(categoriaService.save(producto));
    }

    @GetMapping
    public List<Categoria> getAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Integer id) {
        Optional<Categoria> optionalCategoria = categoriaService.findById(id);
        if (optionalCategoria.isPresent())
            return ResponseEntity.ok(optionalCategoria.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Categoria> update(@Valid @RequestBody Categoria producto) {
        Optional<Categoria> optionalCategoria = categoriaService.findById(producto.getId());
        if (optionalCategoria.isPresent()) {
            categoriaService.update(producto);
            return ResponseEntity.ok(producto);
        }
        else return ResponseEntity.notFound().build();    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (categoriaService.findById(id).isPresent()) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }
}

