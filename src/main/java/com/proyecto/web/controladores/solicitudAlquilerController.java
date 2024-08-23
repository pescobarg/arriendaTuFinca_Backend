package com.proyecto.web.controladores;

import com.proyecto.web.modelos.solicitudAlquiler;
import com.proyecto.web.servicios.solicitudAlquilerServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/solicitudes-alquiler")
public class solicitudAlquilerController {

    @Autowired
    private solicitudAlquilerServicio solicitudAlquilerServicio;

    @GetMapping
    public List<solicitudAlquiler> getAllSolicitudes() {
        return solicitudAlquilerServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<solicitudAlquiler> getSolicitudPorId(@PathVariable Long id) {
        Optional<solicitudAlquiler> solicitudAlquiler = solicitudAlquilerServicio.findById(id);
        return solicitudAlquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<solicitudAlquiler> getSolicitudesPorUsuario(@PathVariable Long usuarioId) {
        return solicitudAlquilerServicio.findByUsuarioId(usuarioId);
    }

    @GetMapping("/propiedad/{propiedadId}")
    public List<solicitudAlquiler> getSolicitudesPorPropiedad(@PathVariable Long propiedadId) {
        return solicitudAlquilerServicio.findByPropiedadId(propiedadId);
    }

    @PostMapping
    public solicitudAlquiler crearSolicitud(@RequestBody solicitudAlquiler solicitudAlquiler) {
        return solicitudAlquilerServicio.save(solicitudAlquiler);
    }

    @PutMapping("/{id}")
    public ResponseEntity<solicitudAlquiler> actualizarSolicitud(@PathVariable Long id, @RequestBody solicitudAlquiler solicitudAlquiler) {
        if (solicitudAlquilerServicio.findById(id).isPresent()) {
            solicitudAlquiler.setId(id);
            return ResponseEntity.ok(solicitudAlquilerServicio.save(solicitudAlquiler));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        if (solicitudAlquilerServicio.findById(id).isPresent()) {
            solicitudAlquilerServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
