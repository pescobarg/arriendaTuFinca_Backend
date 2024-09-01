package com.proyecto.web.controladores;

import com.proyecto.web.dtos.usuarioDTO;
import com.proyecto.web.modelos.TipoUsuario;
import com.proyecto.web.servicios.usuarioServicio;
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

@WebMvcTest(usuarioControlador.class)
public class usuarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usuarioServicio usuarioServicio;

    @Test
    void getUsuariosTest() throws Exception {
        usuarioDTO usuario1 = new usuarioDTO(1L, "Juan", "Perez", "juan@example.com", "password123", 30, TipoUsuario.ARRENDADOR, "Comentario 1");
        usuarioDTO usuario2 = new usuarioDTO(2L, "Maria", "Lopez", "maria@example.com", "password456", 25, TipoUsuario.ARRENDATARIO, "Comentario 2");

        Mockito.when(usuarioServicio.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getUsuarioPorIdTest() throws Exception {
        usuarioDTO usuario = new usuarioDTO(1L, "Juan", "Perez", "juan@example.com", "password123", 30, TipoUsuario.ARRENDADOR, "Comentario 1");

        Mockito.when(usuarioServicio.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

    @Test
    void crearUsuarioTest() throws Exception {
        usuarioDTO usuario = new usuarioDTO(1L, "Juan", "Perez", "juan@example.com", "password123", 30, TipoUsuario.ARRENDADOR, "Comentario 1");

        Mockito.when(usuarioServicio.save(Mockito.any(usuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"correo\":\"juan@example.com\",\"contrasenia\":\"password123\",\"edad\":30,\"tipoUsuario\":\"ARRENDADOR\",\"comentarios\":\"Comentario 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }

    @Test
    void eliminarUsuarioTest() throws Exception {
        usuarioDTO usuario = new usuarioDTO(1L, "Juan", "Perez", "juan@example.com", "password123", 30, TipoUsuario.ARRENDADOR, "Comentario 1");
    
        Mockito.when(usuarioServicio.findById(1L)).thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(usuarioServicio).deleteById(1L);
                mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        Mockito.verify(usuarioServicio, Mockito.times(1)).deleteById(1L);
}


}
