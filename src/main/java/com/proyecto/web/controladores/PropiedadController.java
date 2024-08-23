package com.proyecto.web.controladores;

import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.servicios.propiedadServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/propiedades")
public class propiedadController {

    @Autowired
    private propiedadServicio propiedadServicio;

    @GetMapping
    public List<propiedad> getPropiedades() {
        return propiedadServicio.getPropiedades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<propiedad> getPropiedadPorId(@PathVariable Long id) {
        Optional<propiedad> propiedad = propiedadServicio.encontrarPropiedadPorId(id);
        return propiedad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public propiedad crearPropiedad(@RequestBody propiedad propiedad) {
        return propiedadServicio.guardar(propiedad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<propiedad> actualizarPropiedad(@PathVariable Long id, @RequestBody propiedad propiedad) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedad.setId(id);
            return ResponseEntity.ok(propiedadServicio.guardar(propiedad));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropiedad(@PathVariable Long id) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{propietarioId}")
    public List<propiedad> getPropiedadPorUsuario(@PathVariable Long propietarioId) {
        return propiedadServicio.getPropiedadPorUsuario(propietarioId);
    }
}
