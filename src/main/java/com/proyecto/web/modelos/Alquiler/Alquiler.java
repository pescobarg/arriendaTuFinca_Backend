package com.proyecto.web.modelos.Alquiler;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.proyecto.web.modelos.Propiedad.Propiedad;
import com.proyecto.web.modelos.Usuario.Usuario;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuarioAsignado", nullable = false)
    private Usuario usuarioAsignado;

    @ManyToOne
    @JoinColumn(name = "propiedad", nullable = false)
    private Propiedad propiedad;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlquiler estado;

    @Column(nullable = true)
    private String comentarios;
}
