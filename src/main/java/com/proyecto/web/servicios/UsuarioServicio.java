package com.proyecto.web.servicios;

import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.modelos.Usuario;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Autowired
    private ModelMapper modelMapper;

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
        if(usuario.getEdad()< 18){
            throw new IllegalArgumentException("La edad no puede ser menor a 18");
        }
        Usuario savedUsuario = usuarioRepo.save(usuario);
        return convertToDto(savedUsuario);
    }

    public void deleteById(Long id) {
        usuarioRepo.deleteById(id);
    }

    private UsuarioDTO convertToDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }
}
