package com.proyecto.web.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.web.dtos.Propiedad.PropiedadDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.PropiedadServicio;
import com.proyecto.web.servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "http://localhost:4200")
public class PropiedadControlador {

    private final PropiedadServicio propiedadServicio;

    private final UsuarioServicio usuarioServicio;

    private static final String NOT_FOUND_MESSAGE = "No se encontró la propiedad con id = ";

    public PropiedadControlador(PropiedadServicio propiedadServicio,
                                UsuarioServicio usuarioServicio) {
                                
        this.propiedadServicio = propiedadServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<PropiedadDTO>> getPropiedades(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        try {
            List<PropiedadDTO> propiedades = propiedadServicio.getPropiedades();
            return ResponseEntity.ok(propiedades); // 200 OK con la lista de propiedades
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDTO> getPropiedadPorId(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        Optional<PropiedadDTO> propiedadDTO = Optional.ofNullable(propiedadServicio.encontrarPropiedadPorId(id)
                .orElseThrow(() -> new ResourceNotFound(NOT_FOUND_MESSAGE + id)));
        return propiedadDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PropiedadDTO> crearPropiedad(Authentication authentication, @RequestBody PropiedadDTO propiedadDTO) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        try {
            PropiedadDTO nuevaPropiedad = propiedadServicio.guardar(propiedadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPropiedad); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadDTO> actualizarPropiedad(Authentication authentication, @PathVariable Long id, @RequestBody PropiedadDTO propiedadDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadDTO.setId(id);
            return ResponseEntity.ok(propiedadServicio.guardar(propiedadDTO));
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    // Endpoint para realizar soft-delete (desactivar propiedad)
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPropiedad(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        if (propiedadServicio.encontrarPropiedadPorId(id).isPresent()) {
            propiedadServicio.eliminarPropiedad(id);  // Soft-delete (cambia status a 0)
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    @GetMapping("/usuario/propietario")
    public ResponseEntity<List<PropiedadDTO>> getPropiedadPorUsuarioActual(Authentication authentication) {
        try {
            // Obtener al usuario autenticado
            UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
            
            if (usuarioAutenticado == null) {
                // Retorna 401 Unauthorized si no hay un usuario autenticado
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            // Obtener las propiedades asociadas al usuario autenticado
            List<PropiedadDTO> propiedades = propiedadServicio.getPropiedadPorUsuario(usuarioAutenticado.getId());
            return ResponseEntity.ok(propiedades); // 200 OK con la lista de propiedades
    
        } catch (Exception e) {
            // Manejo de excepciones y respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    
    @GetMapping("/usuario/propietario/sin-alquiler-aprobado")
    public ResponseEntity<List<PropiedadDTO>> obtenerPropiedadesSinAlquilerAprobado(Authentication authentication) {
        try {
            // Obtener al usuario autenticado
            UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
    
            if (usuarioAutenticado == null) {
                // Retorna 401 Unauthorized si no hay un usuario autenticado
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            // Obtener propiedades sin alquiler aprobado para el propietario autenticado
            List<PropiedadDTO> propiedades = propiedadServicio.obtenerPropiedadesSinAlquilerAprobadoPorPropietario(usuarioAutenticado.getId());
    
            return ResponseEntity.ok(propiedades); // 200 OK con la lista de propiedades
    
        } catch (Exception e) {
            // Manejo de errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    
}


