package com.proyecto.web.controladores;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.modelos.TipoPropiedad;
import com.proyecto.web.servicios.PropiedadServicio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropiedadControlador.class)
public class PropiedadControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropiedadServicio propiedadServicio;

    @Test
    void getPropiedadesTest() throws Exception {

        UsuarioAuxDTO usuarioAuxDTO1 = new UsuarioAuxDTO();
        usuarioAuxDTO1.setId(1L);
        
        UsuarioAuxDTO usuarioAuxDTO2 = new UsuarioAuxDTO();
        usuarioAuxDTO2.setId(2L);

        PropiedadDTO propiedad1 = new PropiedadDTO(1L, 500L, usuarioAuxDTO1, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");
        PropiedadDTO propiedad2 = new PropiedadDTO(2L, 300L, usuarioAuxDTO2, "Calle 456", 800.0, false, TipoPropiedad.CASA, "Casa acogedora");

        Mockito.when(propiedadServicio.getPropiedades()).thenReturn(Arrays.asList(propiedad1, propiedad2));

        mockMvc.perform(MockMvcRequestBuilders.get("/propiedades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getPropiedadPorIdTest() throws Exception {

        UsuarioAuxDTO usuarioDTO = new UsuarioAuxDTO();
        usuarioDTO.setId(1L);

        PropiedadDTO propiedad = new PropiedadDTO(1L, 500L, usuarioDTO, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(1L)).thenReturn(Optional.of(propiedad));

        mockMvc.perform(MockMvcRequestBuilders.get("/propiedades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void crearPropiedadTest() throws Exception {

        UsuarioAuxDTO usuarioDTO1 = new UsuarioAuxDTO();
        usuarioDTO1.setId(1L);

    
        PropiedadDTO propiedad = new PropiedadDTO(1L, 500L, usuarioDTO1, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");

        Mockito.when(propiedadServicio.guardar(Mockito.any(PropiedadDTO.class))).thenReturn(propiedad);

        mockMvc.perform(MockMvcRequestBuilders.post("/propiedades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"area\":500,\"propietarioId\":1,\"direccion\":\"Calle 123\",\"precio\":1000.0,\"disponible\":true,\"tipoPropiedad\":\"FINCA\",\"descripcion\":\"Hermosa finca\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void actualizarPropiedadTest() throws Exception {
        Long propiedadId = 1L;

        UsuarioAuxDTO usuarioDTO = new UsuarioAuxDTO();
        usuarioDTO.setId(1L);

        PropiedadDTO propiedadActualizada = new PropiedadDTO(propiedadId, 600L, usuarioDTO, "Calle 789", 1200.0, true, TipoPropiedad.FINCA, "Finca actualizada");

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.of(new PropiedadDTO()));
        Mockito.when(propiedadServicio.guardar(Mockito.any(PropiedadDTO.class))).thenReturn(propiedadActualizada);

        mockMvc.perform(MockMvcRequestBuilders.put("/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"area\":600,\"propietarioId\":1,\"direccion\":\"Calle 789\",\"precio\":1200.0,\"disponible\":true,\"tipoPropiedad\":\"FINCA\",\"descripcion\":\"Finca actualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propiedadId))
                .andExpect(jsonPath("$.area").value(600L));
    }

    @Test
    void actualizarPropiedadNotFoundTest() throws Exception {
        Long propiedadId = 1L;

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"area\":600,\"propietarioId\":1,\"direccion\":\"Calle 789\",\"precio\":1200.0,\"disponible\":true,\"tipoPropiedad\":\"FINCA\",\"descripcion\":\"Finca actualizada\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPropiedadPorUsuarioTest() throws Exception {
        Long propietarioId = 1L;

        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(1L);
        
        PropiedadDTO propiedad1 = new PropiedadDTO(1L, 500L, usuarioAuxDTO, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");
        PropiedadDTO propiedad2 = new PropiedadDTO(2L, 300L, usuarioAuxDTO, "Calle 456", 800.0, false, TipoPropiedad.CASA, "Casa acogedora");

        Mockito.when(propiedadServicio.getPropiedadPorUsuario(propietarioId)).thenReturn(Arrays.asList(propiedad1, propiedad2));

        mockMvc.perform(MockMvcRequestBuilders.get("/propiedades/usuario/{propietarioId}", propietarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }


    @Test
    void deletePropiedadTest() throws Exception {
        Long propiedadId = 1L;

        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(1L);
    
        PropiedadDTO propiedad = new PropiedadDTO(1L, 500L, usuarioAuxDTO, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");
    
        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.of(propiedad));
        Mockito.doNothing().when(propiedadServicio).deleteById(propiedadId);
    
        mockMvc.perform(MockMvcRequestBuilders.delete("/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePropiedadNotFoundTest() throws Exception {
        Long propiedadId = 1L;

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    

}
