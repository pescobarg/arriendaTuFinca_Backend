package com.proyecto.web.servicios;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropiedadServicio {

    private final PropiedadRepositorio propiedadRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final ModelMapper modelMapper;

    private static final String PROPIETARIO_NO_EXISTE = "El propietario con ID ";
    private static final String AREA_INVALIDA = "El área no puede ser menor o igual a 0";
    private static final String PRECIO_INVALIDO = "El precio no puede ser menor a 0";

    public PropiedadServicio(PropiedadRepositorio propiedadRepo, UsuarioRepositorio usuarioRepo, ModelMapper modelMapper) {
        this.propiedadRepo = propiedadRepo;
        this.usuarioRepo = usuarioRepo;
        this.modelMapper = modelMapper;
    }

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
            throw new IllegalArgumentException(PROPIETARIO_NO_EXISTE + propiedadDTO.getPropietarioId() + " no existe.");
        }

        Propiedad propiedad = convertToEntity(propiedadDTO);
        if (propiedad.getArea() <= 0L) {
            throw new IllegalArgumentException(AREA_INVALIDA);
        }
        if (propiedad.getPrecio() < 0L) {
            throw new IllegalArgumentException(PRECIO_INVALIDO);
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
