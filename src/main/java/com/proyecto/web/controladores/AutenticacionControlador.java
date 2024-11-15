package com.proyecto.web.controladores;

import com.proyecto.web.dtos.Usuario.LoginDTO;
import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.servicios.UsuarioServicio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AutenticacionControlador {

    private final UsuarioServicio usuarioServicio;

    public AutenticacionControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody LoginDTO loginRequest) {

        String email = loginRequest.getCorreo();
        String password = loginRequest.getContrasenia();
        String token = usuarioServicio.login(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<UsuarioAuxDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCreado = usuarioServicio.save(usuarioDTO);
        return ResponseEntity.ok(usuarioServicio.convertirAUsuarioAuxDTO(usuarioCreado));
    }

 



}
