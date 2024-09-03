package com.proyecto.web.servicios;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.modelos.Alquiler;
import com.proyecto.web.repositorios.AlquilerRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlquilerServicio {

    @Autowired
    private AlquilerRepositorio alquilerRepo;

    @Autowired
    private PropiedadRepositorio propiedadRepo;

    @Autowired
    private UsuarioRepositorio usuarioRepo;



    @Autowired
    private ModelMapper modelMapper;

    public List<AlquilerDTO> findAll() {
        List<Alquiler> alquileres = alquilerRepo.findAll();
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, AlquilerDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<AlquilerDTO> findById(Long id) {
        Optional<Alquiler> alquiler = alquilerRepo.findById(id);
        return alquiler.map(alq -> modelMapper.map(alq, AlquilerDTO.class));
    }

    public List<AlquilerDTO> findByUsuarioId(Long usuarioId) {
        List<Alquiler> alquileres = alquilerRepo.findByUsuarioId(usuarioId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, AlquilerDTO.class))
                .collect(Collectors.toList());
    }

    public List<AlquilerDTO> findByPropiedadId(Long propiedadId) {
        List<Alquiler> alquileres = alquilerRepo.findByPropiedadId(propiedadId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, AlquilerDTO.class))
                .collect(Collectors.toList());
    }

    public AlquilerDTO save(AlquilerDTO alquilerDTO) {

        if (!propiedadRepo.existsById(alquilerDTO.getPropiedadId())) {
            throw new IllegalArgumentException("El ID de la propiedad no existe: " + alquilerDTO.getPropiedadId());
        }

        if (!usuarioRepo.existsById(alquilerDTO.getUsuarioId())) {
            throw new IllegalArgumentException("El ID del usuario no existe: " + alquilerDTO.getUsuarioId());
        }

        Alquiler alquiler = modelMapper.map(alquilerDTO, Alquiler.class);
        Alquiler savedAlquiler = alquilerRepo.save(alquiler);
        return modelMapper.map(savedAlquiler, AlquilerDTO.class);
    }

    public void deleteById(Long id) {
        alquilerRepo.deleteById(id);
    }
}
