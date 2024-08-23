package com.proyecto.web.servicios;

import com.proyecto.web.modelos.solicitudAlquiler;
import com.proyecto.web.repositorios.solicitudAlquilerRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class solicitudAlquilerServicio {

    @Autowired
    private solicitudAlquilerRepositorio solicitudAlquilerRepo;

    public List<solicitudAlquiler> findAll() {
        return solicitudAlquilerRepo.findAll();
    }

    public Optional<solicitudAlquiler> findById(Long id) {
        return solicitudAlquilerRepo.findById(id);
    }

    public List<solicitudAlquiler> findByUsuarioId(Long usuarioId) {
        return solicitudAlquilerRepo.findByUsuarioId(usuarioId);
    }

    public List<solicitudAlquiler> findByPropiedadId(Long propiedadId) {
        return solicitudAlquilerRepo.findByPropiedadId(propiedadId);
    }

    public solicitudAlquiler save(solicitudAlquiler solicitudAlquiler) {
        return solicitudAlquilerRepo.save(solicitudAlquiler);
    }

    public void deleteById(Long id) {
        solicitudAlquilerRepo.deleteById(id);
    }
}
