package com.proyecto.web.controladores;

import com.proyecto.web.dtos.ReseniaPropiedadDTO;
import com.proyecto.web.dtos.ReseniaUsuarioDTO;
import com.proyecto.web.servicios.ReseniaServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenias")
public class ReseniaControlador {

    private final ReseniaServicio reseniaServicio;

    public ReseniaControlador(ReseniaServicio reseniaServicio) {
        this.reseniaServicio = reseniaServicio;
    }

    @PostMapping("/usuario")
    public ResponseEntity<ReseniaUsuarioDTO> agregarReseniaUsuario(@RequestBody ReseniaUsuarioDTO reseniaDTO) {
        ReseniaUsuarioDTO nuevaResenia = reseniaServicio.agregarReseniaUsuario(reseniaDTO);
        return ResponseEntity.ok(nuevaResenia);
    }

    @PostMapping("/propiedad")
    public ResponseEntity<ReseniaPropiedadDTO> agregarReseniaPropiedad(@RequestBody ReseniaPropiedadDTO reseniaDTO) {
        ReseniaPropiedadDTO nuevaResenia = reseniaServicio.agregarReseniaPropiedad(reseniaDTO);
        return ResponseEntity.ok(nuevaResenia);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<ReseniaUsuarioDTO>> obtenerReseniasUsuario() {
        List<ReseniaUsuarioDTO> resenias = reseniaServicio.obtenerReseniasUsuario();
        return ResponseEntity.ok(resenias);
    }

    @GetMapping("/propiedad")
    public ResponseEntity<List<ReseniaPropiedadDTO>> obtenerReseniasPropiedad() {
        List<ReseniaPropiedadDTO> resenias = reseniaServicio.obtenerReseniasPropiedad();
        return ResponseEntity.ok(resenias);
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<ReseniaUsuarioDTO> actualizarReseniaUsuario(@PathVariable Long id, @RequestBody ReseniaUsuarioDTO nuevaResenia) {
        ReseniaUsuarioDTO reseniaActualizada = reseniaServicio.actualizarReseniaUsuario(id, nuevaResenia);
        return ResponseEntity.ok(reseniaActualizada);
    }

    @PutMapping("/propiedad/{id}")
    public ResponseEntity<ReseniaPropiedadDTO> actualizarReseniaPropiedad(@PathVariable Long id, @RequestBody ReseniaPropiedadDTO nuevaResenia) {
        ReseniaPropiedadDTO reseniaActualizada = reseniaServicio.actualizarReseniaPropiedad(id, nuevaResenia);
        return ResponseEntity.ok(reseniaActualizada);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminarReseniaUsuario(@PathVariable Long id) {
        reseniaServicio.eliminarReseniaUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/propiedad/{id}")
    public ResponseEntity<Void> eliminarReseniaPropiedad(@PathVariable Long id) {
        reseniaServicio.eliminarReseniaPropiedad(id);
        return ResponseEntity.noContent().build();
    }
}
