package com.proyecto.web.controladores;

import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.UsuarioServicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://127.0.0.1")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public List<UsuarioAuxDTO> getUsuarios() {
        return usuarioServicio.findAll().stream()
            .map(usuarioServicio::convertirAUsuarioAuxDTO)
            .collect(Collectors.toList());
    }    

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAuxDTO> getUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = Optional.ofNullable(usuarioServicio.findById(id)
                .orElseThrow(() -> new ResourceNotFound("No hay un usuario con id = " + id)));
        return usuarioDTO.map(dto -> ResponseEntity.ok(usuarioServicio.convertirAUsuarioAuxDTO(dto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAuxDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioExistente = usuarioServicio.findById(id)
                .orElseThrow(() -> new ResourceNotFound("No hay un usuario con id = " + id));

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
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound("No hay un usuario con id = " + id);
        }
    }


}
