package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.UsuarioHabilitado;
import isi.dan.msclientes.service.ClienteService;
import isi.dan.msclientes.service.ObraService;
import isi.dan.msclientes.service.UsuarioHabilitadoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ObraService obraService;

    @MockBean
    private UsuarioHabilitadoService usuarioHabilitadoService;

    private Cliente cliente;
    private UsuarioHabilitado usuario;
    private Obra obra;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Test Cliente");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("12998887776");
        cliente.setMaximaCantidadObrasEnEjecucion(Integer.MAX_VALUE);
        cliente.setMaximoDescubierto(BigDecimal.valueOf(Integer.MAX_VALUE));

        usuario = new UsuarioHabilitado();
        usuario.setId(1);
        cliente.setUsuariosHabilitados(Collections.singletonList(usuario));
        usuario.setCliente(cliente);

        obra = new Obra();
        obra.setId(1);
        cliente.setObrasAsignadas(Collections.singletonList(obra));
        obra.setCliente(cliente);
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(clienteService.findAll()).thenReturn(Collections.singletonList(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Test Cliente"));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Test Cliente"))
                .andExpect(jsonPath("$.cuit").value("12998887776"));
    }
    @Test
    void getByIdNotFound() throws Exception {
        Mockito.when(clienteService.findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Mockito.when(clienteService.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Cliente"));
    }

    @Test
    void update() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));
        Mockito.when(clienteService.update(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Cliente"));
    }

    @Test
    void deleteById() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));
        Mockito.doNothing().when(clienteService).deleteById(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }



    @Test
    void putUsuario() throws Exception {
        Mockito.when(usuarioHabilitadoService.asignarCliente(1, 1)).thenReturn(cliente);
        mockMvc.perform(put("/api/clientes/1/usuario/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.usuariosHabilitados[0].id").value(usuario.getId()));

        Mockito.when(usuarioHabilitadoService.asignarCliente(2, 2)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(put("/api/clientes/2/usuario/2"))
               .andExpect(status().isNotFound());
    }

    @Test
    void putObra() throws Exception {
        Mockito.when(obraService.asignarCliente(1, 1, 1)).thenReturn(cliente);
        mockMvc.perform(put("/api/clientes/1/obra/1/usuario/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.obrasAsignadas[0].id").value(obra.getId()));

        Mockito.when(obraService.asignarCliente(2, 2, 2)).thenThrow(Exception.class);
        mockMvc.perform(put("/api/clientes/2/obra/2/usuario/2"))
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

