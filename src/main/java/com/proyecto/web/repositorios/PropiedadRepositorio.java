package com.proyecto.web.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.web.modelos.Propiedad.Propiedad;


@Repository
public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {
    List<Propiedad> findByPropietarioId(Long propietarioId);
    
    @Query("SELECT p FROM Propiedad p WHERE  p.disponible = true  AND p.status = 1 AND p.propietario.id != :propietarioId AND p.id NOT IN (SELECT a.propiedad.id FROM Alquiler a WHERE a.estado = 'APROBADO')")
    List<Propiedad> findAllPropiedadesNoAprobadasPorPropietario(@Param("propietarioId") Long propietarioId);
}
