package com.proyecto.web.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.web.dtos.Propiedad.PropiedadDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.PropiedadServicio;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "http://127.0.0.1")
public class PropiedadControlador {

    private final PropiedadServicio propiedadServicio;

    private static final String NOT_FOUND_MESSAGE = "No se encontró la propiedad con id = ";

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

    // Endpoint para realizar soft-delete (desactivar propiedad)
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPropiedad(@PathVariable Long id) {
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadServicio.eliminarPropiedad(id);  // Soft-delete (cambia status a 0)
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
    public List<PropiedadDTO> obtenerPropiedadesSinAlquilerAprobado(@PathVariable Long propietarioId) {
        return propiedadServicio.obtenerPropiedadesSinAlquilerAprobadoPorPropietario(propietarioId);
    }
}


