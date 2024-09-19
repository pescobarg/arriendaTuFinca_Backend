package com.proyecto.web.servicios;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO; 
import com.proyecto.web.modelos.TipoPropiedad;
import com.proyecto.web.modelos.Propiedad;
import com.proyecto.web.modelos.Usuario;
import com.proyecto.web.repositorios.PropiedadRepositorio;
import com.proyecto.web.repositorios.UsuarioRepositorio;
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
    private PropiedadRepositorio propiedadRepo;

    @Mock
    private UsuarioRepositorio usuarioRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropiedadServicio propiedadServicio;

    private Propiedad propiedad;
    private PropiedadDTO propiedadDTO;
    private UsuarioAuxDTO usuarioAuxDTO; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(1L);

        propiedad = new Propiedad(1L, 100L, usuario, "direccion", 50000.0, true, TipoPropiedad.APARTAMENTO, "descripcion");

        propiedadDTO = new PropiedadDTO();
        propiedadDTO.setId(1L);
        propiedadDTO.setArea(100L);
        propiedadDTO.setPropietario(usuarioAuxDTO); // Usa UsuarioDTO
        propiedadDTO.setDireccion("direccion");
        propiedadDTO.setPrecio(50000.0);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setTipoPropiedad(TipoPropiedad.APARTAMENTO);
        propiedadDTO.setDescripcion("descripcion");
    }

    @Test
    void testGetPropiedades() {
        List<Propiedad> propiedades = new ArrayList<>();
        propiedades.add(propiedad);

        when(propiedadRepo.findAll()).thenReturn(propiedades);
        when(modelMapper.map(propiedad, PropiedadDTO.class)).thenReturn(propiedadDTO);

        List<PropiedadDTO> result = propiedadServicio.getPropiedades();
        assertEquals(1, result.size());
        assertEquals(propiedadDTO, result.get(0));
    }

    @Test
    void testEncontrarPropiedadPorId() {
        when(propiedadRepo.findById(1L)).thenReturn(Optional.of(propiedad));
        when(modelMapper.map(propiedad, PropiedadDTO.class)).thenReturn(propiedadDTO);

        Optional<PropiedadDTO> result = propiedadServicio.encontrarPropiedadPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(propiedadDTO, result.get());
    }

    @Test
    void testGuardarPropiedad() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(modelMapper.map(propiedadDTO, Propiedad.class)).thenReturn(propiedad);
        when(propiedadRepo.save(propiedad)).thenReturn(propiedad);
        when(modelMapper.map(propiedad, PropiedadDTO.class)).thenReturn(propiedadDTO);

        PropiedadDTO result = propiedadServicio.guardar(propiedadDTO);
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
        assertEquals("El propietario con ID 1", thrown.getMessage());
    }


    @Test
    void testGetPropiedadPorUsuario() {
        List<Propiedad> propiedades = new ArrayList<>();
        propiedades.add(propiedad);

        when(propiedadRepo.findByPropietarioId(1L)).thenReturn(propiedades);
        when(modelMapper.map(propiedad, PropiedadDTO.class)).thenReturn(propiedadDTO);

        List<PropiedadDTO> result = propiedadServicio.getPropiedadPorUsuario(1L);
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
