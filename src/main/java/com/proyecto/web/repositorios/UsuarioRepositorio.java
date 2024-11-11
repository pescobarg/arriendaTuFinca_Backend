package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoAndContrasenia(String correo, String contrasenia);
}
