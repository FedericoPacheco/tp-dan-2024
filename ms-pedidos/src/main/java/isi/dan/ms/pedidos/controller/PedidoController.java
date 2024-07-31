package isi.dan.ms.pedidos.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.model.EstadoPedido;
import isi.dan.ms.pedidos.model.Pedido;
import isi.dan.ms.pedidos.service.PedidoService;
import lombok.extern.slf4j.Slf4j;

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

    @PutMapping("/estado/{id}")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable String id, @RequestBody EstadoPedido estado) {
        pedidoService.actualizarEstado(id, estado);
        return ResponseEntity.noContent().build();    
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable String id) {
        Optional<Pedido> optionalPedido = pedidoService.findById(id);
        if (optionalPedido.isPresent())
            return ResponseEntity.ok(optionalPedido.get());
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Pedido>> findByIdCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.findByIdCliente(id));
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

