package com.proyecto.web.servicios;

import com.proyecto.web.modelos.usuario;
import com.proyecto.web.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public usuario save(usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}
