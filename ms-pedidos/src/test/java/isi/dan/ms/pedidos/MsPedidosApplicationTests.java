package isi.dan.ms.pedidos;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.OrdenCompraDTO;
import isi.dan.ms.pedidos.dto.OrdenProvisionDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.dto.ProductoDTO;
import isi.dan.ms.pedidos.model.EstadoPedido;
import isi.dan.ms.pedidos.service.PedidoService;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MsPedidosApplicationTests {

	@Container
	@ServiceConnection
   	private static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:latest"); 
 
	@Container
    //@ServiceConnection
	private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:alpine");

	@DynamicPropertySource
	static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
		registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
	}

	@Autowired
	private MockMvc mockMvc;

	@MockBean // No queda otra que mockear ms-clientes y ms-productos
    private RestTemplate restTemplate;

	ClienteDTO cliente;
	PedidoDTO pedidoDTO;


	@BeforeAll 
	static void beforeAll() {
		mongoContainer.start();
		rabbitMQContainer.start();
	}

	@BeforeEach
	void beforeEach() {
		// Mocks ms-productos
		pedidoDTO = new PedidoDTO(1,1,1,"");
		BigDecimal total = BigDecimal.ZERO;
        for (int i = 1; i <= 3; i++) {
            ProductoDTO producto = new ProductoDTO(i,"",BigDecimal.valueOf(i * 10), BigDecimal.valueOf(i / 10.0));
            OrdenCompraDTO ordenCompra = new OrdenCompraDTO(i, i);
            pedidoDTO.getProductos().add(ordenCompra); 

			total = total.add( 													// total    +
				BigDecimal.valueOf(i).multiply(									// cantidad *
					BigDecimal.valueOf(i * 10).multiply(						// precio   *
						BigDecimal.ONE.subtract(BigDecimal.valueOf(i / 10.0))	// (1 - descuento)
					)
				)
			);

            Mockito.when(restTemplate.getForObject(eq(PedidoService.URL_PRODUCTOS + Integer.toString(i)), eq(ProductoDTO.class))).thenReturn(producto);
        }

		// Mocks ms-productos
		cliente = new ClienteDTO(pedidoDTO.getIdCliente(), "DAN construcciones", total);
        Mockito.when(restTemplate.getForObject(eq(PedidoService.URL_CLIENTES + pedidoDTO.getIdCliente()), eq(ClienteDTO.class))).thenReturn(cliente);
	}

	@RabbitListener(queues = RabbitMQConfig.ORDENES_COMPRA_QUEUE)
    public Boolean gestionarReducirStockMock(OrdenCompraDTO dto) {
        return true;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDENES_PROVISION_QUEUE)
    public void gestionarIncrementarStockMock(OrdenProvisionDTO dto) { }

	@AfterAll
    static void afterAll() {
        mongoContainer.stop();
		rabbitMQContainer.stop();
    }

	// No se testean casos "extraños" porque ya se cubrieron con PedidoServiceTest
	@Test
	void casoGeneral() throws Exception {

		// Crear un pedido
		MvcResult resultado = mockMvc.perform(post("/api/pedidos")
			   					  .contentType(MediaType.APPLICATION_JSON)
								  .content(asJsonString(pedidoDTO)))
								  .andExpect(status().isOk())
								  .andExpect(jsonPath("$.estado").value(EstadoPedido.EN_PREPARACION.toString()))
								  .andReturn();

		String idPedido = valueFromJsonString(resultado, "id");

		// Actualizar estado del pedido
		mockMvc.perform(put("/api/pedidos/estado/" + idPedido)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(asJsonString(EstadoPedido.ENTREGADO)))
			   .andExpect(status().isNoContent());

		// Consultar estado del pedido
		mockMvc.perform(get("/api/pedidos/" + idPedido))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.estado").value(EstadoPedido.ENTREGADO.toString()));

		// Consultar pedidos realizados por un cliente
		mockMvc.perform(get("/api/pedidos/cliente/" + pedidoDTO.getIdCliente()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.length()").value(1));
	}

	private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private static String valueFromJsonString(MvcResult result, String attributeName) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		return new ObjectMapper().readTree(result.getResponse().getContentAsString()).get(attributeName).asText();
	}
}
