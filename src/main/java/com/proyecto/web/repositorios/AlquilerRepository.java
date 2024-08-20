package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.web.modelos.alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<alquiler, Long> {
}
