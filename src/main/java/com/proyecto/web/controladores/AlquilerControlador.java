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

@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<AlquilerDTO> actualizarAlquiler(Authentication authentication, @PathVariable Long id, @RequestBody AlquilerDTO alquilerDTO) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        // Buscar el alquiler usando su DTO
        AlquilerDTO alquilerExistente = alquilerServicio.findById(id)
            .orElseThrow(() -> new ResourceNotFound("Not found Alquiler with id = " + id));

        boolean haCambiado = false;

        // Compara y actualiza los campos si han cambiado
        if (!alquilerExistente.getEstado().equals(alquilerDTO.getEstado())) {
            alquilerExistente.setEstado(alquilerDTO.getEstado());
            haCambiado = true;
        }

        if (!alquilerExistente.getFechaInicio().equals(alquilerDTO.getFechaInicio())) {
            alquilerExistente.setFechaInicio(alquilerDTO.getFechaInicio());
            haCambiado = true;
        }

        if (!alquilerExistente.getFechaFin().equals(alquilerDTO.getFechaFin())) {
            alquilerExistente.setFechaFin(alquilerDTO.getFechaFin());
            haCambiado = true;
        }

        if ((alquilerExistente.getComentarios() == null && alquilerDTO.getComentarios() != null) ||
            (alquilerExistente.getComentarios() != null && !alquilerExistente.getComentarios().equals(alquilerDTO.getComentarios()))) {
            alquilerExistente.setComentarios(alquilerDTO.getComentarios());
            haCambiado = true;
        }

        // Guardar solo si ha habido cambios
        if (haCambiado) {
            alquilerServicio.update(alquilerExistente);
        }

        // Retornar el alquiler actualizado
        return ResponseEntity.ok(alquilerExistente);
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
}
