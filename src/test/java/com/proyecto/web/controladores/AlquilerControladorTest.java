package com.proyecto.web.controladores;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.modelos.EstadoAlquiler;
import com.proyecto.web.servicios.AlquilerServicio;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlquilerControlador.class)
public class AlquilerControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlquilerServicio alquilerServicio;

    @Test
    void getAllAlquileresTest() throws Exception {
        AlquilerDTO alquiler1 = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");
        AlquilerDTO alquiler2 = new AlquilerDTO(2L, 2L, 2L, EstadoAlquiler.APROBADO, LocalDate.now(), LocalDate.now().plusDays(3), "Comentario 2");

        Mockito.when(alquilerServicio.findAll()).thenReturn(Arrays.asList(alquiler1, alquiler2));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAlquilerPorIdTest() throws Exception {
        AlquilerDTO alquiler = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");

        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(alquiler));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAlquileresPorUsuarioTest() throws Exception {
        AlquilerDTO alquiler1 = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");
        AlquilerDTO alquiler2 = new AlquilerDTO(2L, 1L, 2L, EstadoAlquiler.APROBADO, LocalDate.now(), LocalDate.now().plusDays(3), "Comentario 2");

        Mockito.when(alquilerServicio.findByUsuarioId(1L)).thenReturn(Arrays.asList(alquiler1, alquiler2));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }


    @Test
    void getAlquileresPorPropiedadTest() throws Exception {
        AlquilerDTO alquiler1 = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");
        AlquilerDTO alquiler2 = new AlquilerDTO(2L, 2L, 1L, EstadoAlquiler.APROBADO, LocalDate.now(), LocalDate.now().plusDays(3), "Comentario 2");

        Mockito.when(alquilerServicio.findByPropiedadId(1L)).thenReturn(Arrays.asList(alquiler1, alquiler2));

        mockMvc.perform(MockMvcRequestBuilders.get("/alquileres/propiedad/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }


    @Test
    void actualizarAlquilerTest() throws Exception {
        AlquilerDTO alquilerExistente = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");
        AlquilerDTO alquilerActualizado = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.APROBADO, LocalDate.now(), LocalDate.now().plusDays(7), "Comentario actualizado");

        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(alquilerExistente));
        Mockito.when(alquilerServicio.save(Mockito.any(AlquilerDTO.class))).thenReturn(alquilerActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"APROBADO\",\"fechaInicio\":\"2024-09-01\",\"fechaFin\":\"2024-09-08\",\"comentarios\":\"Comentario actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$.comentarios").value("Comentario actualizado"));
    }

    @Test
    void actualizarAlquilerIdNoExistenteTest() throws Exception {
        Long idNoExistente = 999L;

        // Simular que el alquiler con el ID no existe
        Mockito.when(alquilerServicio.findById(idNoExistente)).thenReturn(Optional.empty());

        // Realizar la solicitud PUT
        mockMvc.perform(MockMvcRequestBuilders.put("/alquileres/" + idNoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"PENDIENTE\",\"fechaInicio\":\"2023-09-01\",\"fechaFin\":\"2023-09-10\",\"comentarios\":\"Alquiler de prueba\"}"))
                .andExpect(status().isNotFound()) // Esperar un estado 404 Not Found
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFound)) // Verificar que se lanzó la excepción ResourceNotFound
                .andExpect(result -> assertEquals("Not found rent with id = " + idNoExistente, result.getResolvedException().getMessage())); // Verificar el mensaje de error
    }



    @Test
    void crearAlquilerTest() throws Exception {
        AlquilerDTO alquiler = new AlquilerDTO(1L, 1L, 1L, EstadoAlquiler.PENDIENTE, LocalDate.now(), LocalDate.now().plusDays(5), "Comentario 1");

        Mockito.when(alquilerServicio.save(Mockito.any(AlquilerDTO.class))).thenReturn(alquiler);

        mockMvc.perform(MockMvcRequestBuilders.post("/alquileres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"PENDIENTE\",\"fechaInicio\":\"2024-09-01\",\"fechaFin\":\"2024-09-06\",\"comentarios\":\"Comentario 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteAlquilerTest() throws Exception {
        // Escenario 1: El alquiler con id 1 existe y se elimina correctamente
        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(new AlquilerDTO()));

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
