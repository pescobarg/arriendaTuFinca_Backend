package com.proyecto.web.controladores;

import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.servicios.UsuarioServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    private static final String NOT_FOUND_MESSAGE = "Not found User with id = ";

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public List<UsuarioDTO> getUsuarios() {
        return usuarioServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = Optional.ofNullable(usuarioServicio.findById(id)
                .orElseThrow(() -> new ResourceNotFound(NOT_FOUND_MESSAGE + id)));
        return usuarioDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioDTO crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioServicio.save(usuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioDTO.setId(id);
            return ResponseEntity.ok(usuarioServicio.save(usuarioDTO));
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFound(NOT_FOUND_MESSAGE + id);
        }
    }
}
