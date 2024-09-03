package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.Propiedad;
import java.util.List;


@Repository
public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {
    List<Propiedad> findByPropietarioId(Long propietarioId);
}
