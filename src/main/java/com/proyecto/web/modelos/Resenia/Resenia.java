package com.proyecto.web.modelos.Resenia;

import com.proyecto.web.modelos.Propiedad.Propiedad;
import com.proyecto.web.modelos.Usuario.Usuario;

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
public class Resenia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_calificador_id", nullable = false)
    private Usuario usuarioCalificadorId;

    @ManyToOne
    @JoinColumn(name = "usuario_objetivo_id")
    private Usuario usuarioObjetivoId; // Puede ser null si la reseña es para una propiedad

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedadObjetivoId; // Puede ser null si la reseña es para un usuario

    private int calificacion;
    private String comentario;

}
