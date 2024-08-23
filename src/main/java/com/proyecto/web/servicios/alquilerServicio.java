package com.proyecto.web.servicios;

import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.repositorios.alquilerRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class alquilerServicio {

    @Autowired
    private alquilerRepositorio alquilerRepo;

    public List<alquiler> findAll() {
        return alquilerRepo.findAll();
    }

    public Optional<alquiler> findById(Long id) {
        return alquilerRepo.findById(id);
    }

    public List<alquiler> findByUsuarioId(Long usuarioId) {
        return alquilerRepo.findByUsuarioId(usuarioId);
    }

    public List<alquiler> findByPropiedadId(Long propiedadId) {
        return alquilerRepo.findByPropiedadId(propiedadId);
    }

    public alquiler save(alquiler alquiler) {
        return alquilerRepo.save(alquiler);
    }

    public void deleteById(Long id) {
        alquilerRepo.deleteById(id);
    }
}
