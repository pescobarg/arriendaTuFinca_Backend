package com.proyecto.web.servicios;

import com.proyecto.web.dtos.alquilerDTO;
import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.repositorios.alquilerRepositorio;
import com.proyecto.web.repositorios.propiedadRepositorio;
import com.proyecto.web.repositorios.usuarioRepositorio;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class alquilerServicio {

    @Autowired
    private alquilerRepositorio alquilerRepo;

    @Autowired
    private propiedadRepositorio propiedadRepo;

    @Autowired
    private usuarioRepositorio usuarioRepo;



    @Autowired
    private ModelMapper modelMapper;

    public List<alquilerDTO> findAll() {
        List<alquiler> alquileres = alquilerRepo.findAll();
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, alquilerDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<alquilerDTO> findById(Long id) {
        Optional<alquiler> alquiler = alquilerRepo.findById(id);
        return alquiler.map(alq -> modelMapper.map(alq, alquilerDTO.class));
    }

    public List<alquilerDTO> findByUsuarioId(Long usuarioId) {
        List<alquiler> alquileres = alquilerRepo.findByUsuarioId(usuarioId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, alquilerDTO.class))
                .collect(Collectors.toList());
    }

    public List<alquilerDTO> findByPropiedadId(Long propiedadId) {
        List<alquiler> alquileres = alquilerRepo.findByPropiedadId(propiedadId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, alquilerDTO.class))
                .collect(Collectors.toList());
    }

    public alquilerDTO save(alquilerDTO alquilerDTO) {

        if (!propiedadRepo.existsById(alquilerDTO.getPropiedadId())) {
            throw new IllegalArgumentException("El ID de la propiedad no existe: " + alquilerDTO.getPropiedadId());
        }

        if (!usuarioRepo.existsById(alquilerDTO.getUsuarioId())) {
            throw new IllegalArgumentException("El ID del usuario no existe: " + alquilerDTO.getUsuarioId());
        }

        alquiler alquiler = modelMapper.map(alquilerDTO, alquiler.class);
        alquiler savedAlquiler = alquilerRepo.save(alquiler);
        return modelMapper.map(savedAlquiler, alquilerDTO.class);
    }

    public void deleteById(Long id) {
        alquilerRepo.deleteById(id);
    }
}
