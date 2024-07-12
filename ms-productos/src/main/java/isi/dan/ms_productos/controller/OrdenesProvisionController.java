package isi.dan.ms_productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.dto.ActualizarStockDTO;
import isi.dan.ms_productos.service.ProductoService;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ordenes-provision")
public class OrdenesProvisionController {

    @Autowired
    private ProductoService productoService;

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ActualizarStockDTO dto, @RequestBody BigDecimal precio) {
        productoService.incrementarStock(dto, precio);
        return ResponseEntity.noContent().build();  
    }
}

