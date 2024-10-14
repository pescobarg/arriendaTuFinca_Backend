package com.proyecto.web.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private long area;

    @ManyToOne
    @JoinColumn(name = "propietario", nullable = false)
    private Usuario propietario;

    @Column(nullable = false)
    private String ciudad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoIngreso tipoIngreso;

    @Column(nullable = false)
    private boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPropiedad tipoPropiedad;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private long cantidadHabitaciones;

    @Column(nullable = false)
    private long cantidadBanios;

    @Column(nullable = false)
    private boolean aceptaMascotas;

    @Column(nullable = false)
    private boolean tienePiscina;

    @Column(nullable = false)
    private boolean tieneAsador;

    @Column(nullable = false)
    private double valorNoche;
}
