package com.proyecto.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.web.modelos.Resenia.Resenia;

import java.util.List;


@Repository
public interface ReseniaRepositorio extends JpaRepository<Resenia, Long> {

    List<Resenia> findByUsuarioCalificadorId_Id(Long idUsuario);

}
