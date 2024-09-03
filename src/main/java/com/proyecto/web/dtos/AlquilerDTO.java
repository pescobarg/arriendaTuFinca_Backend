package com.proyecto.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.proyecto.web.modelos.EstadoAlquiler;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlquilerDTO {
    private Long id;
    private Long usuarioId;
    private Long propiedadId;
    private EstadoAlquiler estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String comentarios;
}
