package com.proyecto.web.dtos.Resenia;

import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaUsuarioDTO {

    private Long id;
    private UsuarioAuxDTO usuarioCalificadorId; 
    private UsuarioAuxDTO usuarioObjetivoId; 
    private int calificacion;
    private String comentario;

}
