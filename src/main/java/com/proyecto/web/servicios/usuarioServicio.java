package com.proyecto.web.servicios;

import com.proyecto.web.modelos.usuario;
import com.proyecto.web.repositorios.usuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class usuarioServicio {

    @Autowired
    private usuarioRepositorio usuarioRepo;

    public List<usuario> findAll() {
        return usuarioRepo.findAll();
    }

    public Optional<usuario> findById(Long id) {
        return usuarioRepo.findById(id);
    }

    public usuario save(usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepo.deleteById(id);
    }
}
