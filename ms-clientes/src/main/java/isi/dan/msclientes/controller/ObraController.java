package isi.dan.msclientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.service.ObraService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @GetMapping
    public List<Obra> getAll() {
        return obraService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Obra> getById(@PathVariable Integer id) {
        Optional<Obra> optionalObra = obraService.findById(id);
        if (optionalObra.isPresent())
            return ResponseEntity.ok(optionalObra.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Obra create(@RequestBody Obra obra) {
        return obraService.save(obra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obra> update(@RequestBody Obra obra) {
        Optional<Obra> optionalObra = obraService.findById(obra.getId());
        if (optionalObra.isPresent()) {
            obraService.update(obra);
            return ResponseEntity.ok(obra);
        }
        else return ResponseEntity.notFound().build();    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (obraService.findById(id).isPresent()) {
            obraService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{idObra}/estado/{nuevoEstado}/usuario/{idUsuario}")
    public ResponseEntity<Obra> updateEstado(@PathVariable Integer idObra, @PathVariable EstadoObra nuevoEstado, @PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.ok(obraService.cambiarEstado(idUsuario, idObra, nuevoEstado));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    } 
}
