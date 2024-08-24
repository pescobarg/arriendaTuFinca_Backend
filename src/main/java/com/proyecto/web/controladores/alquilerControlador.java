package com.proyecto.web.controladores;

import com.proyecto.web.dtos.alquilerDTO;
import com.proyecto.web.servicios.alquilerServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alquileres")
public class alquilerControlador {

    @Autowired
    private alquilerServicio alquilerServicio;

    @GetMapping
    public List<alquilerDTO> getAllAlquileres() {
        return alquilerServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<alquilerDTO> getAlquilerPorId(@PathVariable Long id) {
        Optional<alquilerDTO> alquiler = alquilerServicio.findById(id);
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<alquilerDTO> getAlquileresPorUsuario(@PathVariable Long usuarioId) {
        return alquilerServicio.findByUsuarioId(usuarioId);
    }

    @GetMapping("/propiedad/{propiedadId}")
    public List<alquilerDTO> getAlquileresPorPropiedad(@PathVariable Long propiedadId) {
        return alquilerServicio.findByPropiedadId(propiedadId);
    }

    @PostMapping
    public alquilerDTO crearAlquiler(@RequestBody alquilerDTO alquilerDTO) {
        return alquilerServicio.save(alquilerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<alquilerDTO> actualizarAlquiler(@PathVariable Long id, @RequestBody alquilerDTO alquilerDTO) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquilerDTO.setId(id);
            return ResponseEntity.ok(alquilerServicio.save(alquilerDTO));
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
