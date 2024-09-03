package com.proyecto.web.servicios;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.modelos.EstadoAlquiler;
import com.proyecto.web.modelos.Alquiler;
import com.proyecto.web.repositorios.AlquilerRepositorio;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class AlquilerServicioTest {

    @Mock
    private AlquilerRepositorio alquilerRepo;

    @Mock
    private PropiedadRepositorio propiedadRepo;

    @Mock
    private UsuarioRepositorio usuarioRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AlquilerServicio alquilerServicio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testFindAll() {
        Alquiler alquiler = new Alquiler(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");
        
        when(alquilerRepo.findAll()).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
     void testFindById() {
        Alquiler alquiler = new Alquiler(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");

        when(alquilerRepo.findById(1L)).thenReturn(Optional.of(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        Optional<AlquilerDTO> result = alquilerServicio.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(alquilerDTO, result.get());
    }

    @Test
     void testFindByUsuarioId() {
        Alquiler alquiler = new Alquiler(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");

        when(alquilerRepo.findByUsuarioId(1L)).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findByUsuarioId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
     void testFindByPropiedadId() {
        Alquiler alquiler = new Alquiler(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");

        when(alquilerRepo.findByPropiedadId(1L)).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findByPropiedadId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
     void testSave() {
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");
        Alquiler alquiler = new Alquiler(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");
        
        when(propiedadRepo.existsById(1L)).thenReturn(true);
        when(usuarioRepo.existsById(1L)).thenReturn(true);
        when(modelMapper.map(alquilerDTO, Alquiler.class)).thenReturn(alquiler);
        when(alquilerRepo.save(alquiler)).thenReturn(alquiler);
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        AlquilerDTO result = alquilerServicio.save(alquilerDTO);

        assertNotNull(result);
        assertEquals(alquilerDTO, result);
    }

    @Test
     void testSaveWithInvalidPropiedad() {
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");

        when(propiedadRepo.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            alquilerServicio.save(alquilerDTO);
        });

        assertEquals("El ID de la propiedad no existe: 1", exception.getMessage());
    }

    @Test
     void testSaveWithInvalidUsuario() {
        AlquilerDTO alquilerDTO = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(1), "comentarios");

        when(propiedadRepo.existsById(1L)).thenReturn(true);
        when(usuarioRepo.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            alquilerServicio.save(alquilerDTO);
        });

        assertEquals("El ID del usuario no existe: 1", exception.getMessage());
    }

    @Test
     void testDeleteById() {
        doNothing().when(alquilerRepo).deleteById(1L);

        alquilerServicio.deleteById(1L);

        verify(alquilerRepo, times(1)).deleteById(1L);
    }
}
