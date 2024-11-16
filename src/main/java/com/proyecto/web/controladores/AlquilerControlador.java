package com.proyecto.web.controladores;

import com.proyecto.web.dtos.Alquiler.AlquilerDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.AlquilerServicio;
import com.proyecto.web.servicios.UsuarioServicio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1")
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerControlador {

    private final AlquilerServicio alquilerServicio;

    private final UsuarioServicio usuarioServicio;

    private static final String NOT_FOUND_MESSAGE = "Not found rent with id = ";

    public AlquilerControlador(AlquilerServicio alquilerServicio,
                                UsuarioServicio usuarioServicio) {
        this.alquilerServicio = alquilerServicio;
        this.usuarioServicio = usuarioServicio;
    }


    @GetMapping
    public ResponseEntity<List<AlquilerDTO>> getAllAlquileres(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta 401 Unauthorized si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<AlquilerDTO> alquileres = alquilerServicio.findAll();
            return ResponseEntity.ok(alquileres); // Retorna 200 OK con la lista de alquileres
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }   

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> getAlquilerPorId(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        Optional<AlquilerDTO> alquiler = Optional.ofNullable(alquilerServicio.findById(id).orElseThrow(() -> new ResourceNotFound(NOT_FOUND_MESSAGE + id)));
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/usuarioActual")
    public ResponseEntity<List<AlquilerDTO>> getAlquileresPorUsuario(Authentication authentication)  throws Exception {
        UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
    
        if (usuarioAutenticado == null) {
            // Si el usuario no está autenticado, retorna un error 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        try {
            // Obtener alquileres por ID del usuario autenticado
            List<AlquilerDTO> alquileres = alquilerServicio.findByUsuarioId(usuarioAutenticado.getId());
            return ResponseEntity.ok(alquileres); // Retorna 200 OK con la lista de alquileres
        } catch (Exception e) {
            // Manejo de errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    

    @GetMapping("/propiedad/{propiedadId}")
    public ResponseEntity<List<AlquilerDTO>> getAlquileresPorPropiedad(Authentication authentication, @PathVariable Long propiedadId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        try {
            List<AlquilerDTO> alquileres = alquilerServicio.findByPropiedadId(propiedadId);
            
            if (alquileres.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
                return ResponseEntity.ok(alquileres);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    

    @GetMapping("/usuario/usuarioActual/solicitudes")
    public ResponseEntity<List<AlquilerDTO>> getSolicitudesPorPropietario(Authentication authentication) throws Exception {
    
        UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
    
        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        try {
            List<AlquilerDTO> solicitudes = alquilerServicio.findAlquileresPorPropietario(usuarioAutenticado.getId());

            // Retornar las solicitudes de alquiler con un 200 OK
            return ResponseEntity.ok(solicitudes);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    

    @PostMapping
    public ResponseEntity<AlquilerDTO> crearAlquiler(Authentication authentication, @RequestBody AlquilerDTO alquilerDTO) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    
        AlquilerDTO alquilerGuardado = alquilerServicio.save(alquilerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(alquilerGuardado); // 201 Created
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<AlquilerDTO> actualizarAlquiler(Authentication authentication, 
                                                          @PathVariable Long id, 
                                                          @RequestBody AlquilerDTO alquilerDTO) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // No autenticado
        }
    
        // Actualizar el alquiler y obtener el DTO actualizado
        try {
            AlquilerDTO alquilerActualizado = alquilerServicio.update(alquilerDTO);
            return ResponseEntity.ok(alquilerActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null); // Bad Request con cuerpo nulo
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(404).body(null); // Not Found con cuerpo nulo
        }
    }
    


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(Authentication authentication, @PathVariable Long id) {

        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        if (alquilerServicio.findById(id).isPresent()) {
            alquilerServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE  + id);
        }
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<AlquilerDTO> aprobarAlquiler(
            Authentication authentication, 
            @PathVariable Long id, 
            @RequestBody AlquilerDTO alquilerDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            AlquilerDTO alquilerActualizado = alquilerServicio.aprobarAlquiler(id);
            return ResponseEntity.ok(alquilerActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<AlquilerDTO> rechazarAlquiler(
            Authentication authentication, 
            @PathVariable Long id, 
            @RequestBody AlquilerDTO alquilerDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            AlquilerDTO alquilerActualizado = alquilerServicio.rechazarAlquiler(id);
            return ResponseEntity.ok(alquilerActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
