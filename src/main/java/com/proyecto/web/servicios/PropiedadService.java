package com.proyecto.web.servicios;

import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.repositorios.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    public List<propiedad> findAll() {
        return propiedadRepository.findAll();
    }

    public Optional<propiedad> findById(Long id) {
        return propiedadRepository.findById(id);
    }

    public propiedad save(propiedad propiedad) {
        return propiedadRepository.save(propiedad);
    }

    public void deleteById(Long id) {
        propiedadRepository.deleteById(id);
    }
}
