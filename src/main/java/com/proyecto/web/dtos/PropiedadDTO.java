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
public class PropiedadDTO {

    private Long id;
    private long area;
    private UsuarioDTO propietario; 
    private String direccion;
    private double precio;
    private boolean disponible;
    private TipoPropiedad tipoPropiedad;
    private String descripcion;
}
