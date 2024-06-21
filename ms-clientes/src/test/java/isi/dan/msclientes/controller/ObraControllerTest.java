package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.service.ObraService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObraController.class)
public class ObraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObraService obraService;

    private Obra obra;

    @BeforeEach
    void setUp() {
        obra = new Obra();
        obra.setId(1);
        obra.setDireccion("Direccion Test Obra");
        obra.setEstado(EstadoObra.HABILITADA);
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(obraService.findAll()).thenReturn(Collections.singletonList(obra));

        mockMvc.perform(get("/api/obras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].direccion").value("Direccion Test Obra"));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));

        mockMvc.perform(get("/api/obras/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void create() throws Exception {
        Mockito.when(obraService.save(Mockito.any(Obra.class))).thenReturn(obra);

        mockMvc.perform(post("/api/obras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void update() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));
        Mockito.when(obraService.update(Mockito.any(Obra.class))).thenReturn(obra);

        mockMvc.perform(put("/api/obras/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void deleteById() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));
        Mockito.doNothing().when(obraService).deleteById(1);

        mockMvc.perform(delete("/api/obras/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateEstado() throws Exception {
        Mockito.when(obraService.cambiarEstado(1, 1, EstadoObra.HABILITADA)).thenReturn(obra);
        mockMvc.perform(put("/api/obras/1/estado/HABILITADA/usuario/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value("HABILITADA"));

        Mockito.when(obraService.cambiarEstado(2, 2, EstadoObra.HABILITADA)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(put("/api/obras/2/estado/HABILITADA/usuario/2"))
                .andExpect(status().isNotFound());

        Mockito.when(obraService.cambiarEstado(3, 3, EstadoObra.HABILITADA)).thenThrow(Exception.class);
        mockMvc.perform(put("/api/obras/3/estado/HABILITADA/usuario/3"))
                .andExpect(status().isBadRequest());
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

