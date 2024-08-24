package com.proyecto.web.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasenia;

    @Column(nullable = false)
    private int edad;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false)
    private String comentarios;
}
