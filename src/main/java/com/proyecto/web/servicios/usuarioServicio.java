package com.proyecto.web.servicios;

import com.proyecto.web.dtos.usuarioDTO;
import com.proyecto.web.modelos.usuario;
import com.proyecto.web.repositorios.usuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class usuarioServicio {

    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<usuarioDTO> findAll() {
        return usuarioRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<usuarioDTO> findById(Long id) {
        return usuarioRepo.findById(id)
                .map(this::convertToDto);
    }

    public usuarioDTO save(usuarioDTO usuarioDTO) {
        usuario usuario = convertToEntity(usuarioDTO);
        if(usuario.getEdad()< 18){
            throw new IllegalArgumentException("La edad no puede ser menor a 18");
        }
        usuario savedUsuario = usuarioRepo.save(usuario);
        return convertToDto(savedUsuario);
    }

    public void deleteById(Long id) {
        usuarioRepo.deleteById(id);
    }

    private usuarioDTO convertToDto(usuario usuario) {
        return modelMapper.map(usuario, usuarioDTO.class);
    }

    private usuario convertToEntity(usuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, usuario.class);
    }
}
