package isi.dan.msclientes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(ClienteController.class);

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
    @GetMapping("/searchByEmail")
    public ResponseEntity<Cliente> getByEmail(@RequestParam String email) {
        Optional<Cliente> optionalCliente = clienteService.findByEmail(email);
        if (optionalCliente.isPresent())
            return ResponseEntity.ok(optionalCliente.get());
        else
            return ResponseEntity.notFound().build();
    }
    @GetMapping("/searchByCuit")
    public ResponseEntity<Cliente> getByCuit(@RequestParam String cuit) {
        Optional<Cliente> optionalCliente = clienteService.findByCuit(cuit);
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
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idCliente}/obra/{idObra}/usuario/{idUsuario}")
    public ResponseEntity<Cliente> putObra(@PathVariable Integer idCliente, @PathVariable Integer idObra, @PathVariable Integer idUsuario) {
        try {
            Cliente cliente = obraService.asignarCliente(idUsuario, idCliente, idObra);
            log.info("Obra asignada exitosamente!");
            return ResponseEntity.ok(cliente);
            
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

