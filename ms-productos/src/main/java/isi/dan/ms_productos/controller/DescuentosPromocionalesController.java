package isi.dan.ms_productos.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.model.Producto;
import isi.dan.ms_productos.service.ProductoService;

@RestController
@RequestMapping("/api/descuentos-promocionales")
public class DescuentosPromocionalesController {

    @Autowired
    private ProductoService productoService;

    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> update(@PathVariable Integer idProducto, @RequestBody BigDecimal descuento) {
        Optional<Producto> optionalProducto = productoService.findById(idProducto);
        if (optionalProducto.isPresent()) {
            optionalProducto.get().setDescuentoPromocional(descuento);
            productoService.update(optionalProducto.get());
            return ResponseEntity.ok(optionalProducto.get());
        }
        else return ResponseEntity.notFound().build(); 
    }
}

