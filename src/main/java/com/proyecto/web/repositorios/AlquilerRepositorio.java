package com.proyecto.web.repositorios;

import com.proyecto.web.modelos.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlquilerRepositorio extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findByUsuarioId(Long usuarioId);
    List<Alquiler> findByPropiedadId(Long propiedadId);
}
