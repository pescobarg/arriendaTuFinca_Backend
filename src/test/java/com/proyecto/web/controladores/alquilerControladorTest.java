package com.proyecto.web.controladores;

import com.proyecto.web.dtos.alquilerDTO;
import com.proyecto.web.modelos.EstadoAlquiler;
import com.proyecto.web.servicios.alquilerServicio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(alquilerControlador.class)
public class alquilerControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private alquilerServicio alquilerServicio;

    @Test
    void getAllAlquileresTest() throws Exception {
        alquilerDTO alquiler1 = new alquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");
        alquilerDTO alquiler2 = new alquilerDTO(2L, 2L, 2L, EstadoAlquiler.APROBADO, LocalDate.now(), LocalDate.now().plusDays(3), "Comentario 2");

        Mockito.when(alquilerServicio.findAll()).thenReturn(Arrays.asList(alquiler1, alquiler2));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAlquilerPorIdTest() throws Exception {
        alquilerDTO alquiler = new alquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");

        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(alquiler));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void crearAlquilerTest() throws Exception {
        alquilerDTO alquiler = new alquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");

        Mockito.when(alquilerServicio.save(Mockito.any(alquilerDTO.class))).thenReturn(alquiler);

        mockMvc.perform(MockMvcRequestBuilders.post("/alquileres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"PENDIENTE\",\"fechaInicio\":\"2024-09-01\",\"fechaFin\":\"2024-09-06\",\"comentarios\":\"Comentario 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteAlquilerTest() throws Exception {
        // Escenario 1: El alquiler con id 1 existe y se elimina correctamente
        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(new alquilerDTO()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Mockito.verify(alquilerServicio, Mockito.times(1)).deleteById(1L);

        // Escenario 2: El alquiler con id 99 no existe
        Mockito.when(alquilerServicio.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/alquileres/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(alquilerServicio, Mockito.never()).deleteById(99L);
    }

}
