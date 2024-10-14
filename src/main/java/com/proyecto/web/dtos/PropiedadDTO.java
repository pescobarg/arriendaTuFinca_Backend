package com.proyecto.web.dtos;

import com.proyecto.web.modelos.TipoIngreso;
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
}
