package com.proyecto.web.dtos;

import com.proyecto.web.modelos.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;
    private int edad;
    private TipoUsuario tipoUsuario;
    private String comentarios;
}
