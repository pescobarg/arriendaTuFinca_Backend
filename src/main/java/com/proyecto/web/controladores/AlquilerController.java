package com.proyecto.web.controladores;

import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.servicios.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public List<alquiler> getAllAlquileres() {
        return alquilerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<alquiler> getAlquilerById(@PathVariable Long id) {
        Optional<alquiler> alquiler = alquilerService.findById(id);
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public alquiler createAlquiler(@RequestBody alquiler alquiler) {
        return alquilerService.save(alquiler);
    }

    @PutMapping("/{id}")
    public ResponseEntity<alquiler> updateAlquiler(@PathVariable Long id, @RequestBody alquiler alquiler) {
        if (alquilerService.findById(id).isPresent()) {
            alquiler.setId(id);
            return ResponseEntity.ok(alquilerService.save(alquiler));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(@PathVariable Long id) {
        if (alquilerService.findById(id).isPresent()) {
            alquilerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
