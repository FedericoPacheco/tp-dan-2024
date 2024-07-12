package isi.dan.msclientes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.UsuarioHabilitado;

@ActiveProfiles("db")
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
//@AutoConfigureWebMvc
//@WebMvcTest(MsClientesApplicationTests.class)
class MsClientesApplicationTests {

	@Container
    @ServiceConnection
    private static MySQLContainer<?> container = new MySQLContainer<>("mysql:latest");

	@Autowired
	private MockMvc mockMvc;

	private Cliente cliente;
	private UsuarioHabilitado usuario;
	private Obra obra;

	@BeforeAll
    static void beforeAll() {
        container.start();
    }

	@BeforeEach
	void beforeEach() {
		cliente = new Cliente();
		cliente.setId(1);
		cliente.setNombre("TP DAN");
		cliente.setCorreoElectronico("tp-dan@test.com");
		cliente.setCuit("123456789");
		cliente.setMaximoDescubierto(BigDecimal.valueOf(Integer.MAX_VALUE));
		cliente.setMaximaCantidadObrasEnEjecucion(Integer.MAX_VALUE);

		obra = new Obra();
		obra.setId(1);
		obra.setDireccion("Lavaisse 610");
		obra.setLatitud(BigDecimal.valueOf(-31.616769));
		obra.setLongitud(BigDecimal.valueOf(-60.675446));
		obra.setPresupuesto(BigDecimal.valueOf(Integer.MAX_VALUE));

		usuario = new UsuarioHabilitado();
		usuario.setId(1);
		usuario.setNombre("Federico");
		usuario.setApellido("Pacheco");
		usuario.setDni(42870781);
		usuario.setCorreoElectronico("fpachecopilan@frsf.utn.edu.ar");
	}

	@AfterAll
    static void afterAll() {
        container.stop();
    }

	@Test
	void casoGeneral() throws Exception {
		// Crear cliente
		mockMvc.perform(post("/api/clientes")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(cliente)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.nombre").value(cliente.getNombre()));

		// Crear obra
		mockMvc.perform(post("/api/obras")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(obra)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.direccion").value(obra.getDireccion()));

		// Crear usuario habilitado
		mockMvc.perform(post("/api/usuarios-habilitados")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(usuario)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.nombre").value(usuario.getNombre()));

		// Habilitar usuario para un cliente
		usuario.setCliente(cliente);
		cliente.setUsuariosHabilitados(Collections.singletonList(usuario));
		mockMvc.perform(put("/api/clientes/" + cliente.getId() + "/usuario/" + usuario.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.usuariosHabilitados[0].id").value(usuario.getId()));

		// Asignar obra a un cliente
		cliente.setObrasAsignadas(Collections.singletonList(obra));
		obra.setCliente(cliente);
		mockMvc.perform(put("/api/clientes/" + cliente.getId() + "/obra/" + obra.getId() + "/usuario/" + usuario.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.obrasAsignadas[0].id").value(obra.getId()));
	
		// Cambiar de estado una obra
		obra.setEstado(EstadoObra.HABILITADA);
		mockMvc.perform(put("/api/obras/" + obra.getId() + "/estado/" + obra.getEstado() + "/usuario/" + usuario.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value(obra.getEstado().toString()));
	}

	private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
