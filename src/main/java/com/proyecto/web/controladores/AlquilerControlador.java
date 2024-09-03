package com.proyecto.web.controladores;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.AlquilerServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alquileres")
public class AlquilerControlador {

    @Autowired
    private AlquilerServicio alquilerServicio;

    @GetMapping
    public List<AlquilerDTO> getAllAlquileres() {
        return alquilerServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> getAlquilerPorId(@PathVariable Long id) {
        Optional<AlquilerDTO> alquiler = Optional.ofNullable(alquilerServicio.findById(id).orElseThrow(() -> new ResourceNotFound("Not found rent with id = " + id)));
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<AlquilerDTO> getAlquileresPorUsuario(@PathVariable Long usuarioId) {
        return alquilerServicio.findByUsuarioId(usuarioId);
    }

    @GetMapping("/propiedad/{propiedadId}")
    public List<AlquilerDTO> getAlquileresPorPropiedad(@PathVariable Long propiedadId) {
        return alquilerServicio.findByPropiedadId(propiedadId);
    }

    @PostMapping
    public AlquilerDTO crearAlquiler(@RequestBody AlquilerDTO alquilerDTO) {
        return alquilerServicio.save(alquilerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlquilerDTO> actualizarAlquiler(@PathVariable Long id, @RequestBody AlquilerDTO alquilerDTO) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquilerDTO.setId(id);
            return ResponseEntity.ok(alquilerServicio.save(alquilerDTO));
        } else {
            throw new ResourceNotFound("Not found rent with id = " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(@PathVariable Long id) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquilerServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound("Not found rent with id = " + id);
        }
    }
}
