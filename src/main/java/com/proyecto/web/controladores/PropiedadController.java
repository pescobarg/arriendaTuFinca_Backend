package com.proyecto.web.controladores;

import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.servicios.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public List<propiedad> getAllPropiedades() {
        return propiedadService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<propiedad> getPropiedadById(@PathVariable Long id) {
        Optional<propiedad> propiedad = propiedadService.findById(id);
        return propiedad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public propiedad createPropiedad(@RequestBody propiedad propiedad) {
        return propiedadService.save(propiedad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<propiedad> updatePropiedad(@PathVariable Long id, @RequestBody propiedad propiedad) {
        if (propiedadService.findById(id).isPresent()) {
            propiedad.setId(id);
            return ResponseEntity.ok(propiedadService.save(propiedad));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropiedad(@PathVariable Long id) {
        if (propiedadService.findById(id).isPresent()) {
            propiedadService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
