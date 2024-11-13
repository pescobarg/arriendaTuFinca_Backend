package com.proyecto.web.controladores;

import com.proyecto.web.dtos.ReseniaPropiedadDTO;
import com.proyecto.web.dtos.ReseniaUsuarioDTO;
import com.proyecto.web.servicios.ReseniaServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1")
@RequestMapping("/api/resenias")
public class ReseniaControlador {

    private final ReseniaServicio reseniaServicio;

    public ReseniaControlador(ReseniaServicio reseniaServicio) {
        this.reseniaServicio = reseniaServicio;
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<ReseniaUsuarioDTO>> obtenerReseniasUsuario(
        @RequestParam(value = "idUsuario", required = false) Long idUsuario) {
        List<ReseniaUsuarioDTO> resenias;
        if (idUsuario != null) {
            resenias = reseniaServicio.obtenerReseniasPorUsuario(idUsuario);
        } else {
            resenias = reseniaServicio.obtenerReseniasUsuario();
        }
        return ResponseEntity.ok(resenias);
    }

    @GetMapping("/propiedad")
public ResponseEntity<List<ReseniaPropiedadDTO>> obtenerReseniasPropiedad(
    @RequestParam(value = "idPropiedad", required = false) Long idPropiedad) {
    
    List<ReseniaPropiedadDTO> resenias;
    if (idPropiedad != null) {
        resenias = reseniaServicio.obtenerReseniasPorPropiedad(idPropiedad);
    } else {
        // Si no se proporciona idPropiedad, obtener todas las reseñas de propiedades
        resenias = reseniaServicio.obtenerReseniasPropiedad();
    }
    return ResponseEntity.ok(resenias);
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

    @PutMapping("/usuario/{id}")
    public ResponseEntity<ReseniaUsuarioDTO> actualizarReseniaUsuario(@PathVariable Long id,
            @RequestBody ReseniaUsuarioDTO nuevaResenia) {
        ReseniaUsuarioDTO reseniaActualizada = reseniaServicio.actualizarReseniaUsuario(id, nuevaResenia);
        return ResponseEntity.ok(reseniaActualizada);
    }

    @PutMapping("/propiedad/{id}")
    public ResponseEntity<ReseniaPropiedadDTO> actualizarReseniaPropiedad(@PathVariable Long id,
            @RequestBody ReseniaPropiedadDTO nuevaResenia) {
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
