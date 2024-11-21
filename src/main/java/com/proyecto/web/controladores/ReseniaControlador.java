package com.proyecto.web.controladores;

import com.proyecto.web.dtos.Resenia.ReseniaPropiedadDTO;
import com.proyecto.web.dtos.Resenia.ReseniaUsuarioDTO;
import com.proyecto.web.servicios.ReseniaServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://arriendatufinca.com")
@RequestMapping("/api/resenias")
public class ReseniaControlador {

    private final ReseniaServicio reseniaServicio;

    public ReseniaControlador(ReseniaServicio reseniaServicio) {
        this.reseniaServicio = reseniaServicio;
    }

    @PostMapping("/usuario")
    public ResponseEntity<ReseniaUsuarioDTO> agregarReseniaUsuario(Authentication authentication, @RequestBody ReseniaUsuarioDTO reseniaDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        ReseniaUsuarioDTO nuevaResenia = reseniaServicio.agregarReseniaUsuario(reseniaDTO);
        return ResponseEntity.ok(nuevaResenia);
    }

    @PostMapping("/propiedad")
    public ResponseEntity<ReseniaPropiedadDTO> agregarReseniaPropiedad(Authentication authentication,@RequestBody ReseniaPropiedadDTO reseniaDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        ReseniaPropiedadDTO nuevaResenia = reseniaServicio.agregarReseniaPropiedad(reseniaDTO);
        return ResponseEntity.ok(nuevaResenia);
    }


    @GetMapping("/usuario")
    public ResponseEntity<List<ReseniaUsuarioDTO>> obtenerReseniasUsuario(
        @RequestParam(value = "idUsuario", required = false) Long idUsuario, Authentication authentication) {
        List<ReseniaUsuarioDTO> resenias;

        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        if (idUsuario != null) {
            resenias = reseniaServicio.obtenerReseniasPorUsuario(idUsuario);
        } else {
            resenias = reseniaServicio.obtenerReseniasUsuario();
        }
        return ResponseEntity.ok(resenias);
    }

    @GetMapping("/propiedad")
    public ResponseEntity<List<ReseniaPropiedadDTO>> obtenerReseniasPropiedad(
        @RequestParam(value = "idPropiedad", required = false) Long idPropiedad, Authentication authentication) {
            
            if (authentication == null || !authentication.isAuthenticated()) {
                // Retorna una respuesta de error si el usuario no está autenticado
                return ResponseEntity.status(401).build(); // 401 Unauthorized
            }

        
        List<ReseniaPropiedadDTO> resenias;
        if (idPropiedad != null) {
            resenias = reseniaServicio.obtenerReseniasPorPropiedad(idPropiedad);
        } else {
            // Si no se proporciona idPropiedad, obtener todas las reseñas de propiedades
            resenias = reseniaServicio.obtenerReseniasPropiedad();
        }
        return ResponseEntity.ok(resenias);
    }

    

    @PutMapping("/usuario/{id}")
    public ResponseEntity<ReseniaUsuarioDTO> actualizarReseniaUsuario(@PathVariable Long id,
            @RequestBody ReseniaUsuarioDTO nuevaResenia, Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        ReseniaUsuarioDTO reseniaActualizada = reseniaServicio.actualizarReseniaUsuario(id, nuevaResenia);
        return ResponseEntity.ok(reseniaActualizada);
    }

    @PutMapping("/propiedad/{id}")
    public ResponseEntity<ReseniaPropiedadDTO> actualizarReseniaPropiedad(@PathVariable Long id,
            @RequestBody ReseniaPropiedadDTO nuevaResenia, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        ReseniaPropiedadDTO reseniaActualizada = reseniaServicio.actualizarReseniaPropiedad(id, nuevaResenia);
        return ResponseEntity.ok(reseniaActualizada);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminarReseniaUsuario(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        reseniaServicio.eliminarReseniaUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/propiedad/{id}")
    public ResponseEntity<Void> eliminarReseniaPropiedad(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        reseniaServicio.eliminarReseniaPropiedad(id);
        return ResponseEntity.noContent().build();
    }
}
