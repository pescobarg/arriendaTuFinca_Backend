package com.proyecto.web.servicios;

import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.modelos.Alquiler;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.modelos.Usuario;
import com.proyecto.web.repositorios.AlquilerRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepo;
    private final PropiedadRepositorio propiedadRepo; 
    private final AlquilerRepositorio alquilerRepo;
    private final ModelMapper modelMapper;

    private static final String EDAD_INVALIDA = "La edad no puede ser menor a 18";

    public UsuarioServicio(UsuarioRepositorio usuarioRepo, PropiedadRepositorio propiedadRepo, AlquilerRepositorio alquilerRepo, ModelMapper modelMapper) {
        this.usuarioRepo = usuarioRepo;
        this.propiedadRepo = propiedadRepo;
        this.alquilerRepo = alquilerRepo;
        this.modelMapper = modelMapper;
    }


    public List<UsuarioDTO> findAll() {
        return usuarioRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepo.findById(id)
                .map(this::convertToDto);
    }

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        if (usuario.getEdad() < 18) {
            throw new IllegalArgumentException(EDAD_INVALIDA);
        }
        Usuario savedUsuario = usuarioRepo.save(usuario);
        return convertToDto(savedUsuario);
    }

    public void deleteById(Long id) {
        List<Alquiler> alquileres = alquilerRepo.findByUsuarioAsignado_Id(id);
        alquilerRepo.deleteAll(alquileres);

        List<Propiedad> propiedades = propiedadRepo.findByPropietarioId(id);
        propiedadRepo.deleteAll(propiedades);
        
        usuarioRepo.deleteById(id);
    }

    private UsuarioDTO convertToDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }


}
