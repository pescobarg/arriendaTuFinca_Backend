package com.proyecto.web.servicios;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioDTO;
import com.proyecto.web.modelos.EstadoAlquiler;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.modelos.Usuario;
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

    private Alquiler alquiler;
    private PropiedadDTO propiedadDTO;
    private AlquilerDTO alquilerDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);

        Propiedad propiedad = new Propiedad();
        propiedad.setId(1L);

        propiedadDTO = new PropiedadDTO();
        propiedadDTO.setId(1L);

        alquiler = new Alquiler(1L, usuario, propiedad, LocalDate.now(), LocalDate.now().plusDays(1), EstadoAlquiler.PENDIENTE, "comentarios");

        alquilerDTO = new AlquilerDTO();
        alquilerDTO.setId(1L);
        alquilerDTO.setPropiedad(propiedadDTO);
        alquilerDTO.setUsuarioAsignado(usuarioDTO);
        alquilerDTO.setEstado(EstadoAlquiler.PENDIENTE);
        alquilerDTO.setComentarios("comentarios");
        alquilerDTO.setFechaInicio(LocalDate.now());
        alquilerDTO.setFechaFin(LocalDate.now().plusDays(1));
    }

    @Test
    void testFindAll() {
        when(alquilerRepo.findAll()).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
    void testFindById() {
        when(alquilerRepo.findById(1L)).thenReturn(Optional.of(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        Optional<AlquilerDTO> result = alquilerServicio.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(alquilerDTO, result.get());
    }

    @Test
    void testFindByUsuarioId() {
        when(alquilerRepo.findByUsuarioAsignado_Id(1L)).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findByUsuarioId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
    void testFindByPropiedadId() {
        when(alquilerRepo.findByPropiedad_Id(1L)).thenReturn(Collections.singletonList(alquiler));
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        List<AlquilerDTO> result = alquilerServicio.findByPropiedadId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(alquilerDTO, result.get(0));
    }

    @Test
    void testSave() {
        when(propiedadRepo.existsById(1L)).thenReturn(true);
        when(usuarioRepo.existsById(1L)).thenReturn(true);
        when(modelMapper.map(alquilerDTO, Alquiler.class)).thenReturn(alquiler);
        when(alquilerRepo.save(alquiler)).thenReturn(alquiler);
        when(modelMapper.map(alquiler, AlquilerDTO.class)).thenReturn(alquilerDTO);

        AlquilerDTO result = alquilerServicio.save(alquilerDTO);

        assertNotNull(result);
        assertEquals(alquilerDTO.getId(), result.getId());
        assertEquals(alquilerDTO.getPropiedad(), result.getPropiedad());
        assertEquals(alquilerDTO.getUsuarioAsignado(), result.getUsuarioAsignado());
        assertEquals(alquilerDTO.getEstado(), result.getEstado());
        assertEquals(alquilerDTO.getComentarios(), result.getComentarios());
        assertEquals(alquilerDTO.getFechaInicio(), result.getFechaInicio());
        assertEquals(alquilerDTO.getFechaFin(), result.getFechaFin());
    }

    @Test
    void testSaveWithInvalidPropiedad() {
        when(propiedadRepo.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            alquilerServicio.save(alquilerDTO);
        });

        assertEquals("El ID de la propiedad no existe: 1", exception.getMessage());
    }

    @Test
    void testSaveWithInvalidUsuario() {
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
