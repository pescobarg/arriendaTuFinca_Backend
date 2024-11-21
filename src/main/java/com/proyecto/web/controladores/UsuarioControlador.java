package com.proyecto.web.controladores;

import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.UsuarioServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://arriendatufinca.com")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioAuxDTO>> getUsuarios(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        // Opcional: Imprime el nombre del usuario autenticado
        System.out.println("Usuario autenticado: " + authentication.getName());

        // Continúa con la lógica actual si está autenticado
        List<UsuarioAuxDTO> usuarios = usuarioServicio.findAll().stream()
            .map(usuarioServicio::convertirAUsuarioAuxDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }    


    @GetMapping("/usuarioActual")
    public ResponseEntity<UsuarioAuxDTO> getUsuarioActualPorId(Authentication authentication) throws Exception {
        // Llamamos al servicio de autenticación para validar el usuario
        UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
    
        if (usuarioAutenticado == null) {
            // Si el usuario no está autenticado, retornamos un error 401 Unauthorized
            return ResponseEntity.status(401).build();
        }

        System.out.println("Usuario autenticado: " + authentication.getName());

        // Si el usuario está autenticado, buscamos el usuario por ID
        Optional<UsuarioDTO> usuarioDTO = Optional.ofNullable(
                usuarioServicio.findById(usuarioAutenticado.getId()).orElseThrow(() -> new ResourceNotFound("No hay un usuario con id = " + usuarioAutenticado.getId()))
        );
    
        // Convertimos y retornamos el usuario encontrado
        return usuarioDTO.map(dto -> ResponseEntity.ok(usuarioServicio.convertirAUsuarioAuxDTO(dto)))
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAuxDTO> getUsuarioPorId(Authentication authentication , Long id) throws Exception {
        
        // Llamamos al servicio de autenticación para validar el usuario
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        // Si el usuario está autenticado, buscamos el usuario por ID
        Optional<UsuarioDTO> usuarioDTO = Optional.ofNullable(
                usuarioServicio.findById(id).orElseThrow(() -> new ResourceNotFound("No hay un usuario con id = " + id))
        );
    
        // Convertimos y retornamos el usuario encontrado
        return usuarioDTO.map(dto -> ResponseEntity.ok(usuarioServicio.convertirAUsuarioAuxDTO(dto)))
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/id")
    public ResponseEntity<UsuarioAuxDTO> actualizarUsuarioActual(Authentication authentication, @RequestBody UsuarioDTO usuarioDTO) throws Exception {
        
        UsuarioDTO usuarioAutenticado = usuarioServicio.autorizacion(authentication);
    
        if (usuarioAutenticado == null) {
            // Si el usuario no está autenticado, retornamos un error 401 Unauthorized
            return ResponseEntity.status(401).build();
        }
        
        UsuarioDTO usuarioExistente = usuarioServicio.findById(usuarioAutenticado.getId())
                .orElseThrow(() -> new ResourceNotFound("No hay un usuario con id = " + usuarioAutenticado.getId()));

        boolean haCambiado = false;

        if (!usuarioExistente.getNombre().equals(usuarioDTO.getNombre())) {
            usuarioExistente.setNombre(usuarioDTO.getNombre());
            haCambiado = true;
        }

        if (!usuarioExistente.getApellido().equals(usuarioDTO.getApellido())) {
            usuarioExistente.setApellido(usuarioDTO.getApellido());
            haCambiado = true;
        }

        if (!usuarioExistente.getCorreo().equals(usuarioDTO.getCorreo())) {
            usuarioExistente.setCorreo(usuarioDTO.getCorreo());
            haCambiado = true;
        }

        if (usuarioExistente.getEdad() != usuarioDTO.getEdad()) {
            usuarioExistente.setEdad(usuarioDTO.getEdad());
            haCambiado = true;
        }

        if (haCambiado) {
            usuarioServicio.save(usuarioExistente);
        }

        return ResponseEntity.ok(usuarioServicio.convertirAUsuarioAuxDTO(usuarioExistente));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(Authentication authentication, @PathVariable Long id) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retorna una respuesta de error si el usuario no está autenticado
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound("No hay un usuario con id = " + id);
        }
    }


}
