package com.proyecto.web.repositorios;

import com.proyecto.web.modelos.alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface alquilerRepositorio extends JpaRepository<alquiler, Long> {
    List<alquiler> findByUsuarioId(Long usuarioId);
    List<alquiler> findByPropiedadId(Long propiedadId);
}
