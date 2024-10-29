/*package com.proyecto.web.controladores;

import com.proyecto.web.dtos.PropiedadDTO;
import com.proyecto.web.dtos.UsuarioAuxDTO;
import com.proyecto.web.modelos.TipoIngreso;
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

        PropiedadDTO propiedad1 = new PropiedadDTO(1L, "Propiedad 1", 500L, usuarioAuxDTO1, "Ciudad1", TipoIngreso.Municipio, true, TipoPropiedad.FINCA, "Hermosa finca", 3, 2, true, true, false, 150.0, 1);
        PropiedadDTO propiedad2 = new PropiedadDTO(2L, "Propiedad 2", 300L, usuarioAuxDTO2, "Ciudad2", TipoIngreso.Principal, false, TipoPropiedad.CASA, "Casa acogedora", 2, 1, false, false, true, 120.0, 1);

        Mockito.when(propiedadServicio.getPropiedades()).thenReturn(Arrays.asList(propiedad1, propiedad2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/propiedades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getPropiedadPorIdTest() throws Exception {

        UsuarioAuxDTO usuarioDTO = new UsuarioAuxDTO();
        usuarioDTO.setId(1L);

        PropiedadDTO propiedad = new PropiedadDTO(1L, "Propiedad 1", 500L, usuarioDTO, "Ciudad1", TipoIngreso.Municipio, true, TipoPropiedad.FINCA, "Hermosa finca", 3, 2, true, true, false, 150.0, 1);

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(1L)).thenReturn(Optional.of(propiedad));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/propiedades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void crearPropiedadTest() throws Exception {
        // Creación del propietario para la propiedad
        UsuarioAuxDTO usuarioDTO1 = new UsuarioAuxDTO();
        usuarioDTO1.setId(1L);
    
        // Creación de la propiedad con los atributos necesarios
        PropiedadDTO propiedad = new PropiedadDTO(1L, "Propiedad 1", 500L, usuarioDTO1, "Ciudad1",
                TipoIngreso.Municipio, true, TipoPropiedad.FINCA, "Hermosa finca", 3, 2, true, true, false, 150.0, 1);
    
        // Configuración del mock para que al guardar, retorne la propiedad creada
        Mockito.when(propiedadServicio.guardar(Mockito.any(PropiedadDTO.class))).thenReturn(propiedad);
    
        // JSON de la solicitud para crear la propiedad
        String propiedadJson = """
                {
                    "nombre": "Propiedad 1",
                    "area": 500,
                    "propietario": { "id": 1 },
                    "ciudad": "Ciudad1",
                    "tipoIngreso": "ALQUILER",
                    "disponible": true,
                    "tipoPropiedad": "FINCA",
                    "descripcion": "Hermosa finca",
                    "cantidadHabitaciones": 3,
                    "cantidadBanios": 2,
                    "aceptaMascotas": true,
                    "tienePiscina": true,
                    "tieneAsador": false,
                    "valorNoche": 150.0,
                    "status": 1
                }
                """;
    
        // Simulación de la solicitud POST y verificación de la respuesta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/propiedades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(propiedadJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Propiedad 1"))
                .andExpect(jsonPath("$.area").value(500))
                .andExpect(jsonPath("$.propietario.id").value(1))
                .andExpect(jsonPath("$.ciudad").value("Ciudad1"))
                .andExpect(jsonPath("$.tipoIngreso").value("ALQUILER"))
                .andExpect(jsonPath("$.disponible").value(true))
                .andExpect(jsonPath("$.tipoPropiedad").value("FINCA"))
                .andExpect(jsonPath("$.descripcion").value("Hermosa finca"))
                .andExpect(jsonPath("$.cantidadHabitaciones").value(3))
                .andExpect(jsonPath("$.cantidadBanios").value(2))
                .andExpect(jsonPath("$.aceptaMascotas").value(true))
                .andExpect(jsonPath("$.tienePiscina").value(true))
                .andExpect(jsonPath("$.tieneAsador").value(false))
                .andExpect(jsonPath("$.valorNoche").value(150.0))
                .andExpect(jsonPath("$.status").value(1));
    }
    

    @Test
    void actualizarPropiedadTest() throws Exception {
        Long propiedadId = 1L;

        UsuarioAuxDTO usuarioDTO = new UsuarioAuxDTO();
        usuarioDTO.setId(1L);

        PropiedadDTO propiedadActualizada = new PropiedadDTO(propiedadId, "Propiedad Actualizada", 600L, usuarioDTO, "CiudadActualizada", TipoIngreso.Municipio, true, TipoPropiedad.FINCA, "Finca actualizada", 4, 3, false, false, true, 200.0, 1);

        Mockito.when(propiedadServicio.encontrarPropiedadPorId(propiedadId)).thenReturn(Optional.of(new PropiedadDTO()));
        Mockito.when(propiedadServicio.guardar(Mockito.any(PropiedadDTO.class))).thenReturn(propiedadActualizada);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/propiedades/{id}", propiedadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Propiedad Actualizada\",\"area\":600,\"propietario\":{\"id\":1},\"ciudad\":\"CiudadActualizada\",\"tipoIngreso\":\"ALQUILER\",\"disponible\":true,\"tipoPropiedad\":\"FINCA\",\"descripcion\":\"Finca actualizada\",\"cantidadHabitaciones\":4,\"cantidadBanios\":3,\"aceptaMascotas\":false,\"tienePiscina\":false,\"tieneAsador\":true,\"valorNoche\":200.0,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propiedadId))
                .andExpect(jsonPath("$.area").value(600L));
    }

    @Test
    void getPropiedadPorUsuarioTest() throws Exception {
        Long propietarioId = 1L;

        UsuarioAuxDTO usuarioAuxDTO = new UsuarioAuxDTO();
        usuarioAuxDTO.setId(1L);
        
        PropiedadDTO propiedad1 = new PropiedadDTO(1L, "Propiedad 1", 500L, usuarioAuxDTO, "Ciudad1", TipoIngreso.Principal, true, TipoPropiedad.FINCA, "Hermosa finca", 3, 2, true, true, false, 150.0, 1);
        PropiedadDTO propiedad2 = new PropiedadDTO(2L, "Propiedad 2", 300L, usuarioAuxDTO, "Ciudad2", TipoIngreso.Municipio, false, TipoPropiedad.CASA, "Casa acogedora", 2, 1, false, false, true, 120.0, 1);

        Mockito.when(propiedadServicio.getPropiedadPorUsuario(propietarioId)).thenReturn(Arrays.asList(propiedad1, propiedad2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/propiedades/usuario/{propietarioId}", propietarioId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}
 */