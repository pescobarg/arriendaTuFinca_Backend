package com.proyecto.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaPropiedadDTO {

    private Long id;
    private UsuarioAuxDTO usuarioCalificadorId; 
    private PropiedadDTO propiedadObjetivoId; 
    private int calificacion;
    private String comentario;

}
