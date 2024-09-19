package com.proyecto.web.servicios;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.modelos.Usuario;
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
        // Validar que el propietario exista
        Usuario propietario = usuarioRepo.findById(propiedadDTO.getPropietario().getId())
            .orElseThrow(() -> new IllegalArgumentException(PROPIETARIO_NO_EXISTE + propiedadDTO.getPropietario().getId()));
    
        // Convertir DTO a entidad
        Propiedad propiedad = convertToEntity(propiedadDTO);
        propiedad.setPropietario(propietario); // Asignar el propietario
    
        // Validar el área y precio
        if (propiedad.getArea() <= 0L) {
            throw new IllegalArgumentException(AREA_INVALIDA);
        }
        if (propiedad.getPrecio() < 0L) {
            throw new IllegalArgumentException(PRECIO_INVALIDO);
        }
    
        // Guardar la propiedad
        Propiedad savedPropiedad = propiedadRepo.save(propiedad);
        return convertToDto(savedPropiedad); // Devolver DTO
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
        PropiedadDTO dto = modelMapper.map(propiedad, PropiedadDTO.class);
        dto.setPropietario(convertirAUsuarioAuxDTO(propiedad.getPropietario())); // Usar el método de conversión aquí
        return dto;
    }

    // Método de conversión para el propietario
    private UsuarioAuxDTO convertirAUsuarioAuxDTO(Usuario usuario) {
        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(usuario.getId());
        usuarioAuxDTO.setNombre(usuario.getNombre());
        usuarioAuxDTO.setApellido(usuario.getApellido());
        usuarioAuxDTO.setCorreo(usuario.getCorreo());
        usuarioAuxDTO.setEdad(usuario.getEdad());
        usuarioAuxDTO.setTipoUsuario(usuario.getTipoUsuario());
        usuarioAuxDTO.setComentarios(usuario.getComentarios());
        return usuarioAuxDTO;
    }

    private Propiedad convertToEntity(PropiedadDTO propiedadDTO) {
        return modelMapper.map(propiedadDTO, Propiedad.class);
    }
}
