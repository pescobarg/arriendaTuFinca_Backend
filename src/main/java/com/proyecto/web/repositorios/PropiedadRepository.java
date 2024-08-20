package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<propiedad, Long> {
}
