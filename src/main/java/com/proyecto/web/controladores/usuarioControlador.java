package com.proyecto.web.controladores;

import com.proyecto.web.dtos.usuarioDTO;
import com.proyecto.web.servicios.usuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class usuarioControlador {

    @Autowired
    private usuarioServicio usuarioServicio;

    @GetMapping
    public List<usuarioDTO> getUsuarios() {
        return usuarioServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<usuarioDTO> getUsuarioPorId(@PathVariable Long id) {
        Optional<usuarioDTO> usuarioDTO = usuarioServicio.findById(id);
        return usuarioDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public usuarioDTO crearUsuario(@RequestBody usuarioDTO usuarioDTO) {
        return usuarioServicio.save(usuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<usuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody usuarioDTO usuarioDTO) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioDTO.setId(id);
            return ResponseEntity.ok(usuarioServicio.save(usuarioDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuarioServicio.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
