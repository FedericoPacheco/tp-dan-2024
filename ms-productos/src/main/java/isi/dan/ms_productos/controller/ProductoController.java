package isi.dan.ms_productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.model.Categoria;
import isi.dan.ms_productos.model.Producto;
import isi.dan.ms_productos.service.CategoriaService;
import isi.dan.ms_productos.service.ProductoService;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    //Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Producto> create(@Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.save(producto));
    }

    @GetMapping
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Integer id) {
        Optional<Producto> optionalProducto = productoService.findById(id);
        if (optionalProducto.isPresent()) {
            return ResponseEntity.ok(optionalProducto.get());
        }
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Producto> update(@Valid @RequestBody Producto producto) {
        Optional<Producto> optionalProducto = productoService.findById(producto.getId());
        if (optionalProducto.isPresent()) {
            productoService.update(producto);
            return ResponseEntity.ok(producto);
        }
        else return ResponseEntity.notFound().build();    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{idProducto}/categoria/{idCategoria}")
    public ResponseEntity<Producto> getById(@PathVariable Integer idProducto, @PathVariable Integer idCategoria) {
        Optional<Producto> optionalProducto = productoService.findById(idProducto);
        Optional<Categoria> optionalCategoria = categoriaService.findById(idCategoria);
        if (optionalProducto.isPresent() && optionalCategoria.isPresent()) {
            optionalProducto.get().setCategoria(optionalCategoria.get());
            productoService.update(optionalProducto.get());
            categoriaService.update(optionalCategoria.get());
        
            return ResponseEntity.ok(optionalProducto.get());
        }
        else return ResponseEntity.notFound().build(); 
    }

    @PutMapping("/{idProducto}/descuento-promocional")
    public ResponseEntity<Void> update(@PathVariable Integer idProducto, @RequestBody BigDecimal descuento) {
        Optional<Producto> optionalProducto = productoService.findById(idProducto);
        if (optionalProducto.isPresent()) {
            optionalProducto.get().setDescuentoPromocional(descuento);
            productoService.update(optionalProducto.get());
            return ResponseEntity.noContent().build();  
        }
        else return ResponseEntity.notFound().build(); 
    }
}

