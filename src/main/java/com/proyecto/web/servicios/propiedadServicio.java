package com.proyecto.web.servicios;

import com.proyecto.web.dtos.propiedadDTO;
import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.repositorios.propiedadRepositorio;
import com.proyecto.web.repositorios.usuarioRepositorio;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class propiedadServicio {

    @Autowired
    private propiedadRepositorio propiedadRepo;


    @Autowired
    private usuarioRepositorio usuarioRepo;


    @Autowired
    private ModelMapper modelMapper;

    public List<propiedadDTO> getPropiedades() {
        return propiedadRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<propiedadDTO> encontrarPropiedadPorId(Long id) {
        return propiedadRepo.findById(id)
                .map(this::convertToDto);
    }

    public propiedadDTO guardar(propiedadDTO propiedadDTO) {
        if (!usuarioRepo.findById(propiedadDTO.getPropietarioId()).isPresent()) {
            throw new IllegalArgumentException("El propietario con ID " + propiedadDTO.getPropietarioId() + " no existe.");
        }
        
        propiedad propiedad = convertToEntity(propiedadDTO);
        if (propiedad.getArea() <= 0) {
            throw new IllegalArgumentException("El área no puede ser menor o igual a 0");
        }
        if (propiedad.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser menor a 0");
        }
        propiedad savedPropiedad = propiedadRepo.save(propiedad);
        return convertToDto(savedPropiedad);
    }
    public List<propiedadDTO> getPropiedadPorUsuario(Long propietarioId) {
        return propiedadRepo.findByPropietarioId(propietarioId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        propiedadRepo.deleteById(id);
    }

    private propiedadDTO convertToDto(propiedad propiedad) {
        return modelMapper.map(propiedad, propiedadDTO.class);
    }

    private propiedad convertToEntity(propiedadDTO propiedadDTO) {
        return modelMapper.map(propiedadDTO, propiedad.class);
    }
}
