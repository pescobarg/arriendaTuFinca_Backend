package com.proyecto.web.controladores;

import com.proyecto.web.dtos.propiedadDTO;
import com.proyecto.web.servicios.propiedadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/propiedades")
public class propiedadControlador {

    @Autowired
    private propiedadServicio propiedadServicio;

    @GetMapping
    public List<propiedadDTO> getPropiedades() {
        return propiedadServicio.getPropiedades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<propiedadDTO> getPropiedadPorId(@PathVariable Long id) {
        Optional<propiedadDTO> propiedadDTO = propiedadServicio.encontrarPropiedadPorId(id);
        return propiedadDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public propiedadDTO crearPropiedad(@RequestBody propiedadDTO propiedadDTO) {
        return propiedadServicio.guardar(propiedadDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<propiedadDTO> actualizarPropiedad(@PathVariable Long id, @RequestBody propiedadDTO propiedadDTO) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadDTO.setId(id);
            return ResponseEntity.ok(propiedadServicio.guardar(propiedadDTO));
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
    public List<propiedadDTO> getPropiedadPorUsuario(@PathVariable Long propietarioId) {
        return propiedadServicio.getPropiedadPorUsuario(propietarioId);
    }
}
