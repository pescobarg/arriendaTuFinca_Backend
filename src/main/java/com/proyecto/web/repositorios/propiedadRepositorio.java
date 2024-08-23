package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.propiedad;
import java.util.List;


@Repository
public interface propiedadRepositorio extends JpaRepository<propiedad, Long> {
    List<propiedad> findByPropietarioId(Long propietarioId);
}
