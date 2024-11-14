package com.proyecto.web.dtos.Alquiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.proyecto.web.dtos.Propiedad.PropiedadDTO;
import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.modelos.Alquiler.EstadoAlquiler;

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
