package isi.dan.msclientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.service.ClienteService;
import isi.dan.msclientes.service.ObraService;
import isi.dan.msclientes.service.UsuarioHabilitadoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ObraService obraService;

    @Autowired
    private UsuarioHabilitadoService usuarioHabilitadoService;

    @GetMapping
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Integer id) {
        Optional<Cliente> optionalCliente = clienteService.findById(id);
        if (optionalCliente.isPresent())
            return ResponseEntity.ok(optionalCliente.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Cliente create(@Valid @RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@Valid @RequestBody Cliente cliente) {
        Optional<Cliente> optionalCliente = clienteService.findById(cliente.getId());
        if (optionalCliente.isPresent()) {
            clienteService.update(cliente);
            return ResponseEntity.ok(cliente);
        }
        else return ResponseEntity.notFound().build();    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (clienteService.findById(id).isPresent()) {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{idCliente}/usuario/{idUsuario}")
    public ResponseEntity<Cliente> putUsuario(@PathVariable Integer idCliente, @PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.ok(usuarioHabilitadoService.asignarCliente(idCliente, idUsuario)); 
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idCliente}/obra/{idObra}/usuario/{idUsuario}")
    public ResponseEntity<Cliente> putObra(@PathVariable Integer idCliente, @PathVariable Integer idObra, @PathVariable Integer idUsuario) {
        try {
            Cliente cliente = obraService.asignarCliente(idUsuario, idCliente, idObra);
            return ResponseEntity.ok(cliente);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

