package com.proyecto.web.servicios;

import com.proyecto.web.dtos.propiedadDTO;
import com.proyecto.web.modelos.TipoPropiedad;
import com.proyecto.web.modelos.propiedad;
import com.proyecto.web.modelos.usuario;
import com.proyecto.web.repositorios.propiedadRepositorio;
import com.proyecto.web.repositorios.usuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropiedadServicioTest {

    @Mock
    private propiedadRepositorio propiedadRepo;

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private propiedadServicio propiedadServicio;

    private propiedad propiedad;
    private propiedadDTO propiedadDTO;
    private usuario usuario;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new usuario();
        usuario.setId(1L);

        propiedad = new propiedad(1L, 100L, 1L, "direccion", 50000.0, true, TipoPropiedad.APARTAMENTO, "descripcion");
        propiedadDTO = new propiedadDTO(1L, 100L, 1L, "direccion", 50000.0, true, TipoPropiedad.APARTAMENTO, "descripcion");
    }

    @Test
     void testGetPropiedades() {
        List<propiedad> propiedades = new ArrayList<>();
        propiedades.add(propiedad);

        when(propiedadRepo.findAll()).thenReturn(propiedades);
        when(modelMapper.map(propiedad, propiedadDTO.class)).thenReturn(propiedadDTO);

        List<propiedadDTO> result = propiedadServicio.getPropiedades();
        assertEquals(1, result.size());
        assertEquals(propiedadDTO, result.get(0));
    }

    @Test
     void testEncontrarPropiedadPorId() {
        when(propiedadRepo.findById(1L)).thenReturn(Optional.of(propiedad));
        when(modelMapper.map(propiedad, propiedadDTO.class)).thenReturn(propiedadDTO);

        Optional<propiedadDTO> result = propiedadServicio.encontrarPropiedadPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(propiedadDTO, result.get());
    }

    @Test
     void testGuardarPropiedad() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(propiedadDTO, propiedad.class)).thenReturn(propiedad);
        when(propiedadRepo.save(propiedad)).thenReturn(propiedad);
        when(modelMapper.map(propiedad, propiedadDTO.class)).thenReturn(propiedadDTO);

        propiedadDTO result = propiedadServicio.guardar(propiedadDTO);
        assertEquals(propiedadDTO, result);
    }

    @Test
     void testGuardarPropiedad_InvalidPropietario() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> propiedadServicio.guardar(propiedadDTO),
            "Expected guardar() to throw, but it didn't"
        );
        assertEquals("El propietario con ID 1 no existe.", thrown.getMessage());
    }


    @Test
     void testGetPropiedadPorUsuario() {
        List<propiedad> propiedades = new ArrayList<>();
        propiedades.add(propiedad);

        when(propiedadRepo.findByPropietarioId(1L)).thenReturn(propiedades);
        when(modelMapper.map(propiedad, propiedadDTO.class)).thenReturn(propiedadDTO);

        List<propiedadDTO> result = propiedadServicio.getPropiedadPorUsuario(1L);
        assertEquals(1, result.size());
        assertEquals(propiedadDTO, result.get(0));
    }

    @Test
     void testDeleteById() {
        doNothing().when(propiedadRepo).deleteById(1L);

        assertDoesNotThrow(() -> propiedadServicio.deleteById(1L));
        verify(propiedadRepo, times(1)).deleteById(1L);
    }
}
