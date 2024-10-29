package com.proyecto.web.controladores;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.AlquilerServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerControlador {

    private final AlquilerServicio alquilerServicio;

    private static final String NOT_FOUND_MESSAGE = "Not found rent with id = ";

    public AlquilerControlador(AlquilerServicio alquilerServicio) {
        this.alquilerServicio = alquilerServicio;
    }


    @GetMapping
    public List<AlquilerDTO> getAllAlquileres() {
        return alquilerServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> getAlquilerPorId(@PathVariable Long id) {
        Optional<AlquilerDTO> alquiler = Optional.ofNullable(alquilerServicio.findById(id).orElseThrow(() -> new ResourceNotFound(NOT_FOUND_MESSAGE + id)));
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

    @GetMapping("/usuario/{usuarioId}/solicitudes")
    public ResponseEntity<List<AlquilerDTO>> getSolicitudesPorPropietario(@PathVariable Long usuarioId) {
        List<AlquilerDTO> solicitudes = alquilerServicio.findAlquileresPorPropietario(usuarioId);
        return ResponseEntity.ok(solicitudes);
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
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(@PathVariable Long id) {
        if (alquilerServicio.findById(id).isPresent()) {
            alquilerServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE  + id);
        }
    }
}
