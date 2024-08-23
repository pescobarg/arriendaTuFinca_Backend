package com.proyecto.web.servicios;

import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.repositorios.propiedadRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class propiedadServicio {

    @Autowired
    private propiedadRepositorio propiedadRepo;

    public List<propiedad> getPropiedades() {
        return propiedadRepo.findAll();
    }

    public Optional<propiedad> encontrarPropiedadPorId(Long id) {
        return propiedadRepo.findById(id);
    }

    public propiedad guardar(propiedad propiedad) {
        return propiedadRepo.save(propiedad);
    }

    public List<propiedad> getPropiedadPorUsuario(Long propietarioId) {
        return propiedadRepo.findByPropietarioId(propietarioId);
    }

    public void deleteById(Long id) {
        propiedadRepo.deleteById(id);
    }
}
