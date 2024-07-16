package isi.dan.ms_productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms_productos.dto.OrdenProvisionDTO;
import isi.dan.ms_productos.service.ProductoService;

@RestController
@RequestMapping("/api/ordenes-provision")
public class OrdenesProvisionController {

    @Autowired
    private ProductoService productoService;

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody OrdenProvisionDTO dto) {
        productoService.incrementarStock(dto);
        return ResponseEntity.noContent().build();  
    }
}

