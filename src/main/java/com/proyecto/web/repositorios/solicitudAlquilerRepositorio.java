package com.proyecto.web.repositorios;

import com.proyecto.web.modelos.solicitudAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface solicitudAlquilerRepositorio extends JpaRepository<solicitudAlquiler, Long> {
    List<solicitudAlquiler> findByUsuarioId(Long usuarioId);
    List<solicitudAlquiler> findByPropiedadId(Long propiedadId);
}
