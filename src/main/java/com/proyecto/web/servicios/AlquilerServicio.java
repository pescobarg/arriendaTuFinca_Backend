package com.proyecto.web.servicios;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.modelos.Alquiler;
import com.proyecto.web.repositorios.AlquilerRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlquilerServicio {

    private final AlquilerRepositorio alquilerRepo;
    private final PropiedadRepositorio propiedadRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final ModelMapper modelMapper;




    
    public AlquilerServicio(AlquilerRepositorio alquilerRepo, 
                            PropiedadRepositorio propiedadRepo, 
                            UsuarioRepositorio usuarioRepo, 
                            ModelMapper modelMapper) {
        this.alquilerRepo = alquilerRepo;
        this.propiedadRepo = propiedadRepo;
        this.usuarioRepo = usuarioRepo;
        this.modelMapper = modelMapper;
    }

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
        List<Alquiler> alquileres = alquilerRepo.findByUsuarioAsignado_Id(usuarioId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, AlquilerDTO.class))
                .collect(Collectors.toList());
    }

    public List<AlquilerDTO> findByPropiedadId(Long propiedadId) {
        List<Alquiler> alquileres = alquilerRepo.findByPropiedad_Id(propiedadId);
        return alquileres.stream()
                .map(alquiler -> modelMapper.map(alquiler, AlquilerDTO.class))
                .collect(Collectors.toList());
    }

    public AlquilerDTO save(AlquilerDTO alquilerDTO) {
        if (!propiedadRepo.existsById(alquilerDTO.getPropiedad().getId())) {
            throw new IllegalArgumentException("El ID de la propiedad no existe: " + alquilerDTO.getPropiedad().getId());
        }
        if (!usuarioRepo.existsById(alquilerDTO.getUsuarioAsignado().getId())) {
            throw new IllegalArgumentException("El ID del usuario no existe: " + alquilerDTO.getUsuarioAsignado().getId());
        }
    
        Alquiler alquiler = modelMapper.map(alquilerDTO, Alquiler.class);
        Alquiler savedAlquiler = alquilerRepo.save(alquiler);
        return modelMapper.map(savedAlquiler, AlquilerDTO.class);
    }
    


    public void deleteById(Long id) {
        alquilerRepo.deleteById(id);
    }
}
