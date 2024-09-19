package com.proyecto.web.dtos;

import com.proyecto.web.modelos.EstadoAlquiler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlquilerDTO {

    private Long id;
    private UsuarioAuxDTO usuarioAsignado; 
    private PropiedadDTO propiedad; 
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoAlquiler estado;
    private String comentarios;
}
