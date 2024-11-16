package com.proyecto.web.servicios;

import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.dtos.Usuario.UsuarioDTO;
import com.proyecto.web.modelos.Alquiler.Alquiler;
import com.proyecto.web.modelos.Propiedad.Propiedad;
import com.proyecto.web.modelos.Usuario.Usuario;
import com.proyecto.web.repositorios.AlquilerRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import com.proyecto.web.seguridad.CustomUserDetailService;
import com.proyecto.web.seguridad.jwt.JWTUtil;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepo;
    private final PropiedadRepositorio propiedadRepo; 
    private final AlquilerRepositorio alquilerRepo;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;



    private static final String EDAD_INVALIDA = "La edad no puede ser menor a 18";

    public UsuarioServicio(UsuarioRepositorio usuarioRepo, 
                            PropiedadRepositorio propiedadRepo, 
                            AlquilerRepositorio alquilerRepo, 
                            ModelMapper modelMapper, 
                            AuthenticationManager authenticationManager, 
                            JWTUtil jwtUtil, 
                            CustomUserDetailService customUserDetailService
                            ) {
        this.usuarioRepo = usuarioRepo;
        this.propiedadRepo = propiedadRepo;
        this.alquilerRepo = alquilerRepo;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = customUserDetailService;
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

    public String login(String correo, String contrasenia) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, contrasenia)
        );
    
        if (authentication.isAuthenticated()) {

            return jwtUtil.generateToken(
                    customUserDetailService.getUserDetail().getId(),
                    customUserDetailService.getUserDetail().getCorreo()
            );
        }
        throw new BadCredentialsException("Credenciales invalidas");  
    }


    public UsuarioDTO autorizacion(Authentication authentication) throws Exception {
        System.out.println("-------COMPROBANDO AUTORIZACION----------------");
        System.out.println(authentication.getName());
    
        // Buscar el usuario en la base de datos
        Optional<Usuario> optionalUsuario = usuarioRepo.findByCorreo(authentication.getName());
    
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
    
            // Convertir el usuario en un DTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellido(usuario.getApellido());
            usuarioDTO.setCorreo(usuario.getCorreo());
            usuarioDTO.setEdad(usuario.getEdad());
            usuarioDTO.setId(usuario.getId());
    
            System.out.println("--------------------USUARIO OBTENIDO------------------------"); 
            System.out.println("NOMBRE " + usuarioDTO.getNombre());
            System.out.println("APELLIDO " + usuarioDTO.getApellido()); 
            System.out.println("CORREO " + usuarioDTO.getCorreo()); 
            System.out.println("EDAD " + usuarioDTO.getEdad()); 
            System.out.println("ID " + usuarioDTO.getId()); 
            System.out.println("-----------------------------------------------"); 
    
            return usuarioDTO;
        } else {
            System.out.println("Usuario no encontrado.");
            return null; 
            
        }
    }
    
    




    private UsuarioDTO convertToDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }
    
    public UsuarioAuxDTO convertirAUsuarioAuxDTO(UsuarioDTO usuarioDTO) {
        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(usuarioDTO.getId());
        usuarioAuxDTO.setNombre(usuarioDTO.getNombre());
        usuarioAuxDTO.setApellido(usuarioDTO.getApellido());
        usuarioAuxDTO.setCorreo(usuarioDTO.getCorreo());
        usuarioAuxDTO.setEdad(usuarioDTO.getEdad());
        return usuarioAuxDTO;
    }

    public UsuarioAuxDTO convertirAUsuarioAuxDTO(Usuario usuario) {
        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(usuario.getId());
        usuarioAuxDTO.setNombre(usuario.getNombre());
        usuarioAuxDTO.setApellido(usuario.getApellido());
        usuarioAuxDTO.setCorreo(usuario.getCorreo());
        usuarioAuxDTO.setEdad(usuario.getEdad());
        return usuarioAuxDTO;
    }
}
