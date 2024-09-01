package com.proyecto.web.controladores;

import com.proyecto.web.dtos.propiedadDTO;
import com.proyecto.web.modelos.TipoPropiedad;
import com.proyecto.web.servicios.propiedadServicio;
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

@WebMvcTest(propiedadControlador.class)
public class propiedadControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private propiedadServicio propiedadServicio;

    @Test
    void getPropiedadesTest() throws Exception {
        propiedadDTO propiedad1 = new propiedadDTO(1L, 500L, 1L, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");
        propiedadDTO propiedad2 = new propiedadDTO(2L, 300L, 2L, "Calle 456", 800.0, false, TipoPropiedad.CASA, "Casa acogedora");

        Mockito.when(propiedadServicio.getPropiedades()).thenReturn(Arrays.asList(propiedad1, propiedad2));

        mockMvc.perform(MockMvcRequestBuilders.get("/propiedades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getPropiedadPorIdTest() throws Exception {
        propiedadDTO propiedad = new propiedadDTO(1L, 500L, 1L, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(1L)).thenReturn(Optional.of(propiedad));

        mockMvc.perform(MockMvcRequestBuilders.get("/propiedades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void crearPropiedadTest() throws Exception {
        propiedadDTO propiedad = new propiedadDTO(1L, 500L, 1L, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");

        Mockito.when(propiedadServicio.guardar(Mockito.any(propiedadDTO.class))).thenReturn(propiedad);

        mockMvc.perform(MockMvcRequestBuilders.post("/propiedades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"area\":500,\"propietarioId\":1,\"direccion\":\"Calle 123\",\"precio\":1000.0,\"disponible\":true,\"tipoPropiedad\":\"FINCA\",\"descripcion\":\"Hermosa finca\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deletePropiedadTest() throws Exception {
        Long propiedadId = 1L;
    
        propiedadDTO propiedad = new propiedadDTO(1L, 500L, 1L, "Calle 123", 1000.0, true, TipoPropiedad.FINCA, "Hermosa finca");
    
        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.of(propiedad));
        Mockito.doNothing().when(propiedadServicio).deleteById(propiedadId);
    
        mockMvc.perform(MockMvcRequestBuilders.delete("/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    

}
