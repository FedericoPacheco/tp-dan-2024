package isi.dan.ms_productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.dto.OrdenCompraDTO;
import isi.dan.ms_productos.dto.OrdenProvisionDTO;
import isi.dan.ms_productos.service.ProductoService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenesController {

    @Autowired
    private ProductoService productoService;

    @PutMapping("/provision")
    public ResponseEntity<Void> update(@RequestBody OrdenProvisionDTO dto) {
        productoService.incrementarStock(dto);
        return ResponseEntity.noContent().build();  
    }

    // Se lo agrega por completitud / "simetría"
    @PutMapping("/compra")
    public ResponseEntity<Void> update(@RequestBody OrdenCompraDTO dto) {
        productoService.reducirStock(dto);
        return ResponseEntity.noContent().build();  
    }
}

