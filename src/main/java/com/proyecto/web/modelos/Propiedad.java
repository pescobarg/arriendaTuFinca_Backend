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
    private long area;

    @ManyToOne
    @JoinColumn(name = "propietario", nullable = false)
    private Usuario propietario;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPropiedad tipoPropiedad;

    @Column
    private String descripcion;
}
