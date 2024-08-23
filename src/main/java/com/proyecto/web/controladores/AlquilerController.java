package com.proyecto.web.controladores;

import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.servicios.alquilerServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alquileres")
public class alquilerController {

    @Autowired
    private alquilerServicio alquilerServicio;

    @GetMapping
    public List<alquiler> getAllAlquileres() {
        return alquilerServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<alquiler> getAlquilerPorId(@PathVariable Long id) {
        Optional<alquiler> alquiler = alquilerServicio.findById(id);
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<alquiler> getAlquileresPorUsuario(@PathVariable Long usuarioId) {
        return alquilerServicio.findByUsuarioId(usuarioId);
    }

    @GetMapping("/propiedad/{propiedadId}")
    public List<alquiler> getAlquileresPorPropiedad(@PathVariable Long propiedadId) {
        return alquilerServicio.findByPropiedadId(propiedadId);
    }

    @PostMapping
    public alquiler crearAlquiler(@RequestBody alquiler alquiler) {
        return alquilerServicio.save(alquiler);
    }

    @PutMapping("/{id}")
    public ResponseEntity<alquiler> actualizarAlquiler(@PathVariable Long id, @RequestBody alquiler alquiler) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquiler.setId(id);
            return ResponseEntity.ok(alquilerServicio.save(alquiler));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(@PathVariable Long id) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquilerServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
