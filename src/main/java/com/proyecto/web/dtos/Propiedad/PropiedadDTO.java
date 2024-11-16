package com.proyecto.web.dtos.Propiedad;

import com.proyecto.web.dtos.Usuario.UsuarioAuxDTO;
import com.proyecto.web.modelos.Propiedad.TipoIngreso;
import com.proyecto.web.modelos.Propiedad.TipoPropiedad;

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
    private String nombre;
    private long area;
    private UsuarioAuxDTO propietario; 
    private String ciudad;
    private TipoIngreso tipoIngreso;
    private boolean disponible;
    private TipoPropiedad tipoPropiedad;
    private String descripcion;
    private long cantidadHabitaciones;
    private long cantidadBanios;
    private boolean aceptaMascotas;
    private boolean tienePiscina;
    private boolean tieneAsador;
    private double valorNoche;
    protected int status;
}
