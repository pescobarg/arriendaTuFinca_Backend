package com.proyecto.web.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.web.modelos.Usuario.Usuario;
import com.proyecto.web.repositorios.UsuarioRepositorio;

import lombok.Getter;

import java.util.Collections;
import java.util.Optional;

@Getter
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Usuario userDetail;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepositorio.findByCorreo(email);

        if (user.isEmpty())
            throw new UsernameNotFoundException("Usuario no encontrado");

        userDetail = user.get();
        return new org.springframework.security.core.userdetails.User(userDetail.getCorreo(), userDetail.getContrasenia(),  Collections.emptyList()  );
    }

    
}
