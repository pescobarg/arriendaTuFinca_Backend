package com.proyecto.web.servicios;

import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.repositorios.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    public List<alquiler> findAll() {
        return alquilerRepository.findAll();
    }

    public Optional<alquiler> findById(Long id) {
        return alquilerRepository.findById(id);
    }

    public alquiler save(alquiler alquiler) {
        return alquilerRepository.save(alquiler);
    }

    public void deleteById(Long id) {
        alquilerRepository.deleteById(id);
    }
}
