package isi.dan.ms.pedidos.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.model.Pedido;
import isi.dan.ms.pedidos.services.PedidoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
 
    @PostMapping
    public ResponseEntity<Pedido> create(@RequestBody PedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.save(dto));
    }

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @PutMapping
    public ResponseEntity<Pedido> update(@Valid @RequestBody Pedido pedido) {
        Optional<Pedido> optionalPedido = pedidoService.findById(pedido.getId());
        if (optionalPedido.isPresent()) {
            pedidoService.update(pedido);
            return ResponseEntity.ok(pedido);
        }
        else return ResponseEntity.notFound().build();    
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable String id) {
        Optional<Pedido> optionalPedido = pedidoService.findById(id);
        if (optionalPedido.isPresent())
            return ResponseEntity.ok(optionalPedido.get());
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        Optional<Pedido> optionalPedido = pedidoService.findById(id);
        if (optionalPedido.isPresent()) {
            pedidoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }
}

