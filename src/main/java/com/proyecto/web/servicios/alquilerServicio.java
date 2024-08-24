package com.proyecto.web.servicios;

import com.proyecto.web.dtos.alquilerDTO;
import com.proyecto.web.modelos.alquiler;
import com.proyecto.web.repositorios.alquilerRepositorio;
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
        alquiler alquiler = modelMapper.map(alquilerDTO, alquiler.class);
        alquiler savedAlquiler = alquilerRepo.save(alquiler);
        return modelMapper.map(savedAlquiler, alquilerDTO.class);
    }

    public void deleteById(Long id) {
        alquilerRepo.deleteById(id);
    }
}
