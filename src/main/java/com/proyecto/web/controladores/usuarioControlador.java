package com.proyecto.web.controladores;

import com.proyecto.web.modelos.usuario;
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
    public List<usuario> getUsuarios() {
        return usuarioServicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<usuario> getUsuarioPorId(@PathVariable Long id) {
        Optional<usuario> usuario = usuarioServicio.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public usuario crearUsuario(@RequestBody usuario usuario) {
        return usuarioServicio.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<usuario> actualizarUsuario(@PathVariable Long id, @RequestBody usuario usuario) {
        if (usuarioServicio.findById(id).isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioServicio.save(usuario));
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
