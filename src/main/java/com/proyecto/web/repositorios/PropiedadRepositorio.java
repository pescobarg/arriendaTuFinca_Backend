package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.Propiedad;
import java.util.List;


@Repository
public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {
    List<Propiedad> findByPropietarioId(Long propietarioId);
    
    @Query("SELECT p FROM Propiedad p WHERE p.disponible = true AND p.id NOT IN (SELECT a.propiedad.id FROM Alquiler a WHERE a.estado = 'APROBADO')")
    List<Propiedad> findAllPropiedadesNoAprobadas();
    
}
