package com.proyecto.web.servicios;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropiedadServicio {

    @Autowired
    private PropiedadRepositorio propiedadRepo;


    @Autowired
    private UsuarioRepositorio usuarioRepo;


    @Autowired
    private ModelMapper modelMapper;

    public List<PropiedadDTO> getPropiedades() {
        return propiedadRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PropiedadDTO> encontrarPropiedadPorId(Long id) {
        return propiedadRepo.findById(id)
                .map(this::convertToDto);
    }

    public PropiedadDTO guardar(PropiedadDTO propiedadDTO) {
        if (!usuarioRepo.findById(propiedadDTO.getPropietarioId()).isPresent()) {
            throw new IllegalArgumentException("El propietario con ID " + propiedadDTO.getPropietarioId() + " no existe.");
        }
        
        Propiedad propiedad = convertToEntity(propiedadDTO);
        if (propiedad.getArea() <= 0) {
            throw new IllegalArgumentException("El área no puede ser menor o igual a 0");
        }
        if (propiedad.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser menor a 0");
        }
        Propiedad savedPropiedad = propiedadRepo.save(propiedad);
        return convertToDto(savedPropiedad);
    }
    public List<PropiedadDTO> getPropiedadPorUsuario(Long propietarioId) {
        return propiedadRepo.findByPropietarioId(propietarioId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        propiedadRepo.deleteById(id);
    }

    private PropiedadDTO convertToDto(Propiedad propiedad) {
        return modelMapper.map(propiedad, PropiedadDTO.class);
    }

    private Propiedad convertToEntity(PropiedadDTO propiedadDTO) {
        return modelMapper.map(propiedadDTO, Propiedad.class);
    }
}
