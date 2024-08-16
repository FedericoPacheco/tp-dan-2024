package isi.dan.ms.clientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.clientes.model.UsuarioHabilitado;
import isi.dan.ms.clientes.service.UsuarioHabilitadoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios-habilitados")
public class UsuarioHabilitadoController {

    @Autowired
    private UsuarioHabilitadoService usuarioHabilitadoService;

    @GetMapping
    public List<UsuarioHabilitado> getAll() {
        return usuarioHabilitadoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioHabilitado> getById(@PathVariable Integer id) {
        Optional<UsuarioHabilitado> optionalUsuarioHabilitado = usuarioHabilitadoService.findById(id);
        if (optionalUsuarioHabilitado.isPresent())
            return ResponseEntity.ok(optionalUsuarioHabilitado.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public UsuarioHabilitado create(@Valid @RequestBody UsuarioHabilitado usuarioHabilitado) {
        return usuarioHabilitadoService.save(usuarioHabilitado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioHabilitado> update(@Valid @RequestBody UsuarioHabilitado usuarioHabilitado) {
        Optional<UsuarioHabilitado> optionalUsuarioHabilitado = usuarioHabilitadoService.findById(usuarioHabilitado.getId());
        if (optionalUsuarioHabilitado.isPresent()) {
            usuarioHabilitadoService.update(usuarioHabilitado);
            return ResponseEntity.ok(usuarioHabilitado);
        }
        else return ResponseEntity.notFound().build();    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (usuarioHabilitadoService.findById(id).isPresent()) {
            usuarioHabilitadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.notFound().build();
    }
}
