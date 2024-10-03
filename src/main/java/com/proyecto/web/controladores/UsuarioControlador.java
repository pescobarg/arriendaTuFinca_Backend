package com.proyecto.web.controladores;

import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.UsuarioServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public List<UsuarioAuxDTO> getUsuarios() {
        return usuarioServicio.findAll().stream()
            .map(this::convertirAUsuarioAuxDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAuxDTO> getUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = Optional.ofNullable(usuarioServicio.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Not found User with id = " + id)));
        return usuarioDTO.map(dto -> ResponseEntity.ok(convertirAUsuarioAuxDTO(dto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioAuxDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCreado = usuarioServicio.save(usuarioDTO);
        return ResponseEntity.ok(convertirAUsuarioAuxDTO(usuarioCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAuxDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioExistente = usuarioServicio.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Not found User with id = " + id));

        boolean haCambiado = false;

        // Compara y actualiza solo los campos que han cambiado
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

        if (usuarioExistente.getEdad() == usuarioDTO.getEdad()) {
            usuarioExistente.setEdad(usuarioDTO.getEdad());
            haCambiado = true;
        }

        if (!usuarioExistente.getTipoUsuario().equals(usuarioDTO.getTipoUsuario())) {
            usuarioExistente.setTipoUsuario(usuarioDTO.getTipoUsuario());
            haCambiado = true;
        }

        if (!usuarioExistente.getComentarios().equals(usuarioDTO.getComentarios())) {
            usuarioExistente.setComentarios(usuarioDTO.getComentarios());
            haCambiado = true;
        }

        // Guardar cambios solo si ha habido cambios
        if (haCambiado) {
            usuarioServicio.save(usuarioExistente);
        }

        // Convertir a DTO auxiliar para la respuesta
        return ResponseEntity.ok(convertirAUsuarioAuxDTO(usuarioExistente));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound("Not found User with id = " + id);
        }
    }

    private UsuarioAuxDTO convertirAUsuarioAuxDTO(UsuarioDTO usuarioDTO) {
        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(usuarioDTO.getId());
        usuarioAuxDTO.setNombre(usuarioDTO.getNombre());
        usuarioAuxDTO.setApellido(usuarioDTO.getApellido());
        usuarioAuxDTO.setCorreo(usuarioDTO.getCorreo());
        usuarioAuxDTO.setEdad(usuarioDTO.getEdad());
        usuarioAuxDTO.setTipoUsuario(usuarioDTO.getTipoUsuario());
        usuarioAuxDTO.setComentarios(usuarioDTO.getComentarios());
        return usuarioAuxDTO;
    }
}
