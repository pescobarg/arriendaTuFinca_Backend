package com.proyecto.web.repositorios;

import com.proyecto.web.modelos.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ReseniaRepositorio extends JpaRepository<Resenia, Long> {

    List<Resenia> findByUsuarioCalificadorId_Id(Long idUsuario);

}
