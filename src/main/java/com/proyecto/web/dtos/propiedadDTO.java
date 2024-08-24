package com.proyecto.web.dtos;

import com.proyecto.web.modelos.TipoPropiedad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class propiedadDTO {

    private Long id;
    private long area;
    private Long propietarioId;
    private String direccion;
    private double precio;
    private boolean disponible;
    private TipoPropiedad tipoPropiedad;
    private String descripcion;
}
