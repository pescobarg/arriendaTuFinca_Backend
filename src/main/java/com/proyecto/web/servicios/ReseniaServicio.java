package com.proyecto.web.servicios;

import com.proyecto.web.dtos.Resenia.ReseniaPropiedadDTO;
import com.proyecto.web.dtos.Resenia.ReseniaUsuarioDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.modelos.Resenia.Resenia;
import com.proyecto.web.repositorios.ReseniaRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReseniaServicio {

    private final ReseniaRepositorio reseniaRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final PropiedadRepositorio propiedadRepo;
    private final ModelMapper modelMapper;

    public ReseniaServicio(ReseniaRepositorio reseniaRepo, UsuarioRepositorio usuarioRepo, 
                           PropiedadRepositorio propiedadRepo, ModelMapper modelMapper) {
        this.reseniaRepo = reseniaRepo;
        this.usuarioRepo = usuarioRepo;
        this.propiedadRepo = propiedadRepo;
        this.modelMapper = modelMapper;
    }

    public ReseniaUsuarioDTO agregarReseniaUsuario(ReseniaUsuarioDTO reseniaDTO) {
        Resenia resenia = convertToEntity(reseniaDTO);
        resenia.setUsuarioCalificadorId(usuarioRepo.findById(reseniaDTO.getUsuarioCalificadorId().getId())
                .orElseThrow(() -> new ResourceNotFound("Usuario calificador no encontrado.")));
        resenia.setUsuarioObjetivoId(usuarioRepo.findById(reseniaDTO.getUsuarioObjetivoId().getId())
                .orElseThrow(() -> new ResourceNotFound("Usuario objetivo no encontrado.")));
        Resenia savedResenia = reseniaRepo.save(resenia);
        return convertToUsuarioDto(savedResenia);
    }

    public ReseniaPropiedadDTO agregarReseniaPropiedad(ReseniaPropiedadDTO reseniaDTO) {
        if (reseniaDTO.getUsuarioCalificadorId() == null || reseniaDTO.getUsuarioCalificadorId().getId() == null) {
            throw new IllegalArgumentException("El usuario calificador no puede ser nulo.");
        }
        if (reseniaDTO.getPropiedadObjetivoId() == null || reseniaDTO.getPropiedadObjetivoId().getId() == null) {
            throw new IllegalArgumentException("La propiedad objetivo no puede ser nula.");
        }
    
        Resenia resenia = convertToEntity(reseniaDTO);
        resenia.setUsuarioCalificadorId(usuarioRepo.findById(reseniaDTO.getUsuarioCalificadorId().getId())
                .orElseThrow(() -> new ResourceNotFound("Usuario calificador no encontrado.")));
        resenia.setPropiedadObjetivoId(propiedadRepo.findById(reseniaDTO.getPropiedadObjetivoId().getId())
                .orElseThrow(() -> new ResourceNotFound("Propiedad objetivo no encontrada.")));
        Resenia savedResenia = reseniaRepo.save(resenia);
        return convertToPropiedadDto(savedResenia);
    }
    

    public List<ReseniaUsuarioDTO> obtenerReseniasUsuario() {
        return reseniaRepo.findAll().stream()
                .filter(resenia -> resenia.getUsuarioObjetivoId() != null) // Reseñas para usuarios
                .map(this::convertToUsuarioDto)
                .collect(Collectors.toList());
    }

    public List<ReseniaUsuarioDTO> obtenerReseniasPorUsuario(Long idUsuario) {
        return reseniaRepo.findByUsuarioCalificadorId_Id(idUsuario).stream()
            .map(this::convertToUsuarioDto)
            .collect(Collectors.toList());
    }
    

    public List<ReseniaPropiedadDTO> obtenerReseniasPropiedad() {
        return reseniaRepo.findAll().stream()
                .filter(resenia -> resenia.getPropiedadObjetivoId() != null) // Reseñas para propiedades
                .map(this::convertToPropiedadDto)
                .collect(Collectors.toList());
    }

    public List<ReseniaPropiedadDTO> obtenerReseniasPorPropiedad(Long idPropiedad) {
        List<Resenia> resenias = reseniaRepo.findByPropiedadObjetivoId_Id(idPropiedad);
    
        return resenias.stream()
            .map(resenia -> modelMapper.map(resenia, ReseniaPropiedadDTO.class))
            .collect(Collectors.toList());
    }
    

    public ReseniaUsuarioDTO actualizarReseniaUsuario(Long id, ReseniaUsuarioDTO nuevaResenia) {
        Resenia resenia = reseniaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Reseña de usuario no encontrada con id = " + id));
        resenia.setCalificacion(nuevaResenia.getCalificacion());
        resenia.setComentario(nuevaResenia.getComentario());
        Resenia updatedResenia = reseniaRepo.save(resenia);
        return convertToUsuarioDto(updatedResenia);
    }

    public ReseniaPropiedadDTO actualizarReseniaPropiedad(Long id, ReseniaPropiedadDTO nuevaResenia) {
        Resenia resenia = reseniaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Reseña de propiedad no encontrada con id = " + id));
        resenia.setCalificacion(nuevaResenia.getCalificacion());
        resenia.setComentario(nuevaResenia.getComentario());
        Resenia updatedResenia = reseniaRepo.save(resenia);
        return convertToPropiedadDto(updatedResenia);
    }

    public void eliminarReseniaUsuario(Long id) {
        if (!reseniaRepo.existsById(id)) {
            throw new ResourceNotFound("Reseña de usuario no encontrada con id = " + id);
        }
        reseniaRepo.deleteById(id);
    }

    public void eliminarReseniaPropiedad(Long id) {
        if (!reseniaRepo.existsById(id)) {
            throw new ResourceNotFound("Reseña de propiedad no encontrada con id = " + id);
        }
        reseniaRepo.deleteById(id);
    }

    private Resenia convertToEntity(ReseniaUsuarioDTO reseniaDTO) {
        return modelMapper.map(reseniaDTO, Resenia.class);
    }

    private Resenia convertToEntity(ReseniaPropiedadDTO reseniaDTO) {
        return modelMapper.map(reseniaDTO, Resenia.class);
    }

    private ReseniaUsuarioDTO convertToUsuarioDto(Resenia resenia) {
        return modelMapper.map(resenia, ReseniaUsuarioDTO.class);
    }

    private ReseniaPropiedadDTO convertToPropiedadDto(Resenia resenia) {
        return modelMapper.map(resenia, ReseniaPropiedadDTO.class);
    }
}
