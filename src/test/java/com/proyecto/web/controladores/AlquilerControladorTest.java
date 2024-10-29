package com.proyecto.web.controladores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.proyecto.web.dtos.AlquilerDTO;
import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.errores.ResourceNotFound;
import com.proyecto.web.modelos.EstadoAlquiler;
import com.proyecto.web.servicios.AlquilerServicio;

@WebMvcTest(AlquilerControlador.class)
public class AlquilerControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlquilerServicio alquilerServicio;

    @Test
    void getAllAlquileresTest() throws Exception {
        UsuarioAuxDTO usuarioAuxDTO1 = new UsuarioAuxDTO();
        usuarioAuxDTO1.setId(1L);

        UsuarioAuxDTO usuarioAuxDTO2 = new UsuarioAuxDTO();
        usuarioAuxDTO2.setId(2L);

        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setId(1L);

        PropiedadDTO propiedadDTO2 = new PropiedadDTO();
        propiedadDTO2.setId(2L);

        AlquilerDTO alquiler1 = new AlquilerDTO(1L, usuarioAuxDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(5), EstadoAlquiler.PENDIENTE, "Comentario 1");
        AlquilerDTO alquiler2 = new AlquilerDTO(2L, usuarioAuxDTO2, propiedadDTO2, LocalDate.now(), LocalDate.now().plusDays(3), EstadoAlquiler.APROBADO, "Comentario 2");

        Mockito.when(alquilerServicio.findAll()).thenReturn(Arrays.asList(alquiler1, alquiler2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alquileres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAlquilerPorIdTest() throws Exception {
        UsuarioAuxDTO usuarioAuxDTO1 = new UsuarioAuxDTO();
        usuarioAuxDTO1.setId(1L);
        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setId(1L);

        AlquilerDTO alquiler = new AlquilerDTO(1L, usuarioAuxDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(5), EstadoAlquiler.PENDIENTE, "Comentario 1");

        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(alquiler));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAlquileresPorUsuarioTest() throws Exception {
        UsuarioAuxDTO usuarioDTO1 = new UsuarioAuxDTO();
        usuarioDTO1.setId(1L);

        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setId(1L);

        AlquilerDTO alquiler1 = new AlquilerDTO(1L, usuarioDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(5), EstadoAlquiler.PENDIENTE, "Comentario 1");

        Mockito.when(alquilerServicio.findByUsuarioId(1L)).thenReturn(Arrays.asList(alquiler1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alquileres/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void actualizarAlquilerTest() throws Exception {
        UsuarioAuxDTO usuarioDTO1 = new UsuarioAuxDTO();
        usuarioDTO1.setId(1L);

        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setId(1L);

        AlquilerDTO alquilerExistente = new AlquilerDTO(1L, usuarioDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(5), EstadoAlquiler.PENDIENTE, "Comentario inicial");
        AlquilerDTO alquilerActualizado = new AlquilerDTO(1L, usuarioDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(3), EstadoAlquiler.APROBADO, "Comentario actualizado");

        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(alquilerExistente));
        Mockito.when(alquilerServicio.save(Mockito.any(AlquilerDTO.class))).thenReturn(alquilerActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"APROBADO\",\"fechaInicio\":\"2024-09-01\",\"fechaFin\":\"2024-09-08\",\"comentarios\":\"Comentario actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$.comentarios").value("Comentario actualizado"));
    }

    @Test
    void actualizarAlquilerIdNoExistenteTest() throws Exception {
        Long idNoExistente = 999L;

        Mockito.when(alquilerServicio.findById(idNoExistente)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/alquileres/" + idNoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"PENDIENTE\",\"fechaInicio\":\"2023-09-01\",\"fechaFin\":\"2023-09-10\",\"comentarios\":\"Alquiler de prueba\"}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFound))
                .andExpect(result -> assertEquals("Not found rent with id = " + idNoExistente, result.getResolvedException().getMessage()));
    }

    @Test
    void crearAlquilerTest() throws Exception {
        UsuarioAuxDTO usuarioDTO1 = new UsuarioAuxDTO();
        usuarioDTO1.setId(1L);
        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setId(1L);

        AlquilerDTO alquiler = new AlquilerDTO(1L, usuarioDTO1, propiedadDTO1, LocalDate.now(), LocalDate.now().plusDays(5), EstadoAlquiler.PENDIENTE, "Comentario 1");

        Mockito.when(alquilerServicio.save(Mockito.any(AlquilerDTO.class))).thenReturn(alquiler);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/alquileres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":1,\"propiedadId\":1,\"estado\":\"PENDIENTE\",\"fechaInicio\":\"2024-09-01\",\"fechaFin\":\"2024-09-06\",\"comentarios\":\"Comentario 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteAlquilerTest() throws Exception {
        Mockito.when(alquilerServicio.findById(1L)).thenReturn(Optional.of(new AlquilerDTO()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/alquileres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Mockito.verify(alquilerServicio, Mockito.times(1)).deleteById(1L);

        Mockito.when(alquilerServicio.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/alquileres/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(alquilerServicio, Mockito.never()).deleteById(99L);
    }
}
