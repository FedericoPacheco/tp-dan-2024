package isi.dan.ms_productos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dto.OrdenCompraDTO;
import isi.dan.ms_productos.dto.OrdenProvisionDTO;
import isi.dan.ms_productos.model.Categoria;
import isi.dan.ms_productos.model.Producto;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = {RabbitMQConfig.class})
class MsProductosApplicationTests {

	@Container
	@ServiceConnection
   	private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine"); 
 
	@Container
    //@ServiceConnection
	private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:alpine"); //"rabbitmq:latest");

	/* Se usa este metodo porque @ServiceConnection no setea correctamente los parámetros por algún motivo,
	 y luego el @Bean de ConnectionFactory de rabbitTemplate falla.
	*/
	 @DynamicPropertySource
	static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
		registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
	}

    @Autowired
    private RabbitTemplate rabbitTemplate;

	@Autowired
	private MockMvc mockMvc;

	private Categoria categoria;
	private Producto producto;
	private OrdenProvisionDTO ordenProvisionDTO;
	private OrdenCompraDTO ordenCompraDTO;

	//Logger log = LoggerFactory.getLogger(MsProductosApplicationTests.class);

	@BeforeAll 
	static void beforeAll() {
		postgresContainer.start();
		rabbitMQContainer.start();
	}

	@BeforeEach
	void beforeEach() {
		categoria = new Categoria();
		categoria.setId(1);
		categoria.setNombre("Placas");

		producto = new Producto();
		producto.setId(1);
		producto.setNombre("Placa de porcelanato");
		producto.setDescripcion("Es muy linda");
		producto.setPrecio(BigDecimal.valueOf(10000));
		producto.setStockMinimo(10);
		producto.setStockActual(25);

		ordenProvisionDTO = new OrdenProvisionDTO();
		ordenProvisionDTO.setIdProducto(producto.getId());
		ordenProvisionDTO.setCantidad(10);
		ordenProvisionDTO.setPrecio(BigDecimal.valueOf(11000));

		ordenCompraDTO = new OrdenCompraDTO();
		ordenCompraDTO.setIdProducto(producto.getId());
		ordenCompraDTO.setCantidad(5);
	}

	@AfterAll
    static void afterAll() {
        postgresContainer.stop();
		rabbitMQContainer.stop();
    }

	@Test
	void casoGeneral() throws Exception {

		// Guardar categoria
		mockMvc.perform(post("/api/categorias")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(categoria)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.nombre").value(categoria.getNombre()));
			   
		// Guardar producto
		mockMvc.perform(post("/api/productos")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(producto)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.nombre").value(producto.getNombre()));

		// Asignar producto a categoria
		producto.setCategoria(categoria); 
		mockMvc.perform(put("/api/productos/" + producto.getId() + "/categoria/" + categoria.getId())
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.categoria.id").value(categoria.getId()));

		// Incrementar stock por orden de provision
		mockMvc.perform(put("/api/ordenes/provision")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(ordenProvisionDTO))) 
			   .andExpect(status().isNoContent());
			   
		mockMvc.perform(get("/api/productos/" + producto.getId()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.stockActual").value(producto.getStockActual() + ordenProvisionDTO.getCantidad()));

		// Cambiar descuento promocional
		producto.setDescuentoPromocional(BigDecimal.valueOf(0.1));
		mockMvc.perform(put("/api/productos/" + producto.getId() + "/descuento-promocional")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(producto.getDescuentoPromocional()))) 
			   .andExpect(status().isNoContent());

		mockMvc.perform(get("/api/productos/" + producto.getId()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.descuentoPromocional").value(producto.getDescuentoPromocional()));
		
		// Reducir stock por orden de compra
		assertDoesNotThrow(
			() -> rabbitTemplate.convertAndSend(RabbitMQConfig.ORDENES_EXCHANGE, RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY, ordenCompraDTO));

		/* Esperar un poco para pegarle a la API. Si no ProductoController retorna más rapido
		   su respuesta que lo que el mensaje de rabbit es recibido, procesado, reenviado y procesado
		   por ProductoService; y esto hace el test falle.
		*/
		Thread.sleep(2500); 
		mockMvc.perform(get("/api/productos/" + producto.getId()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.stockActual").value(producto.getStockActual() + ordenProvisionDTO.getCantidad() - ordenCompraDTO.getCantidad()));
	}

	private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
