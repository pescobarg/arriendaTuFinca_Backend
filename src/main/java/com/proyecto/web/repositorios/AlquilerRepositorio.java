package com.proyecto.web.repositorios;

import com.proyecto.web.modelos.Alquiler.Alquiler;
import com.proyecto.web.modelos.Propiedad.Propiedad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlquilerRepositorio extends JpaRepository<Alquiler, Long> {

    List<Alquiler> findByUsuarioAsignado_Id(Long usuarioId);
    List<Alquiler> findByPropiedad_Id(Long propiedadId);
    List<Alquiler> findByPropiedadIn(List<Propiedad> propiedades);
}
