package com.proyecto.web.controladores;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.servicios.PropiedadServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "http://localhost:4200")
public class PropiedadControlador {

    private final PropiedadServicio propiedadServicio;

    private static final String NOT_FOUND_MESSAGE = "Not found property with id = ";

    public PropiedadControlador(PropiedadServicio propiedadServicio) {
        this.propiedadServicio = propiedadServicio;
    }

    @GetMapping
    public List<PropiedadDTO> getPropiedades() {
        return propiedadServicio.getPropiedades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDTO> getPropiedadPorId(@PathVariable Long id) {
        Optional<PropiedadDTO> propiedadDTO = Optional.ofNullable(propiedadServicio.encontrarPropiedadPorId(id)
                .orElseThrow(() -> new ResourceNotFound(NOT_FOUND_MESSAGE + id)));
        return propiedadDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PropiedadDTO crearPropiedad(@RequestBody PropiedadDTO propiedadDTO) {
        return propiedadServicio.guardar(propiedadDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadDTO> actualizarPropiedad(@PathVariable Long id, @RequestBody PropiedadDTO propiedadDTO) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadDTO.setId(id);
            return ResponseEntity.ok(propiedadServicio.guardar(propiedadDTO));
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropiedad(@PathVariable Long id) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    @GetMapping("/usuario/{propietarioId}")
    public List<PropiedadDTO> getPropiedadPorUsuario(@PathVariable Long propietarioId) {
        return propiedadServicio.getPropiedadPorUsuario(propietarioId);
    }

    @GetMapping("/usuario/{propietarioId}/sin-alquiler-aprobado")
    public List<Propiedad> obtenerPropiedadesSinAlquilerAprobado(@PathVariable Long propietarioId) {
        return propiedadServicio.obtenerPropiedadesSinAlquilerAprobadoPorPropietario(propietarioId);
    }
    
}
