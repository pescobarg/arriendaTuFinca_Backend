package com.proyecto.web.servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.modelos.Usuario;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;

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
        Usuario propietario = usuarioRepo.findById(propiedadDTO.getPropietario().getId())
            .orElseThrow(() -> new IllegalArgumentException(PROPIETARIO_NO_EXISTE + propiedadDTO.getPropietario().getId()));
    
        Propiedad propiedad = convertToEntity(propiedadDTO);
        propiedad.setPropietario(propietario); 
    
        if (propiedad.getArea() <= 0L) {
            throw new IllegalArgumentException(AREA_INVALIDA);
        }
        if (propiedad.getValorNoche() < 0L) {
            throw new IllegalArgumentException(PRECIO_INVALIDO);
        }
    
        Propiedad savedPropiedad = propiedadRepo.save(propiedad);
        return convertToDto(savedPropiedad); 
    }

    public void eliminarPropiedad(Long id) {
        Propiedad propiedad = propiedadRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada con ID: " + id));
        propiedad.setStatus(0); // Realiza el soft-delete cambiando el status a 0
        propiedadRepo.save(propiedad);
    }

    public List<PropiedadDTO> getPropiedadPorUsuario(Long propietarioId) {
        return propiedadRepo.findByPropietarioId(propietarioId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PropiedadDTO> obtenerPropiedadesSinAlquilerAprobadoPorPropietario(Long propietarioId) {
        return propiedadRepo.findAllPropiedadesNoAprobadasPorPropietario(propietarioId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PropiedadDTO convertToDto(Propiedad propiedad) {
        PropiedadDTO dto = modelMapper.map(propiedad, PropiedadDTO.class);
        dto.setPropietario(convertirAUsuarioAuxDTO(propiedad.getPropietario())); 
        return dto;
    }

    private UsuarioAuxDTO convertirAUsuarioAuxDTO(Usuario usuario) {
        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(usuario.getId());
        usuarioAuxDTO.setNombre(usuario.getNombre());
        usuarioAuxDTO.setApellido(usuario.getApellido());
        usuarioAuxDTO.setCorreo(usuario.getCorreo());
        usuarioAuxDTO.setEdad(usuario.getEdad());
        return usuarioAuxDTO;
    }

    private Propiedad convertToEntity(PropiedadDTO propiedadDTO) {
        return modelMapper.map(propiedadDTO, Propiedad.class);
    }
}

