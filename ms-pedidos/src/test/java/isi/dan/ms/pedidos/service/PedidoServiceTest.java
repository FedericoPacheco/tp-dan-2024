package isi.dan.ms.pedidos.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.OrdenCompraDTO;
import isi.dan.ms.pedidos.dto.OrdenProvisionDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.dto.ProductoDTO;
import isi.dan.ms.pedidos.model.EstadoPedido;
import isi.dan.ms.pedidos.model.Pedido;
import isi.dan.ms.pedidos.services.PedidoService;

@ActiveProfiles("test")
@SpringBootTest
// Ignorar configuracion de rabbitMQ y Mongo (dan errores)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
public class PedidoServiceTest {
    
    List<Pedido> pedidosViejos;
    PedidoDTO pedidoNuevoDTO;
    Pedido pedidoNuevoIncompleto;
    List<ProductoDTO> productos;
    ClienteDTO cliente;

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void beforeEach() {

        Integer[] montos = {100, 50, 200, 150, 100, 150};
        EstadoPedido[] estados = {
            EstadoPedido.ACEPTADO, EstadoPedido.RECHAZADO, EstadoPedido.ENTREGADO, 
            EstadoPedido.EN_PREPARACION, EstadoPedido.ACEPTADO, EstadoPedido.EN_PREPARACION
        };
        pedidosViejos = new ArrayList<>();
        for (int i = 0; i < montos.length; i++) {
            Pedido pedido = new Pedido(new PedidoDTO(1,1,1,"")); 
            pedido.setTotal(BigDecimal.valueOf(montos[i]));
            pedido.setEstado(estados[i]);
            pedidosViejos.add(pedido);
        }

        pedidoNuevoDTO = new PedidoDTO(1,1,1,"");
        productos = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            ProductoDTO producto = new ProductoDTO(i,"",BigDecimal.valueOf(50), BigDecimal.valueOf(0));
            productos.add(producto);

            OrdenCompraDTO ordenCompra = new OrdenCompraDTO(i, 1);
            pedidoNuevoDTO.getProductos().add(ordenCompra); 
        
            Mockito.when(restTemplate.getForObject(PedidoService.URL_PRODUCTOS + Integer.toString(i), ProductoDTO.class)).thenReturn(producto);
            /* Mockito.when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.ORDENES_EXCHANGE, 
                RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY, 
                ordenCompra
            )).thenReturn(true);
            Mockito.doNothing().when(rabbitTemplate).convertAndSend(
                RabbitMQConfig.ORDENES_EXCHANGE, 
                RabbitMQConfig.ORDENES_PROVISION_ROUTING_KEY, 
                new OrdenProvisionDTO(ordenCompra.getIdProducto(), ordenCompra.getCantidad(), null)
            ); */
        }

        Mockito.when(rabbitTemplate.convertSendAndReceive(
            eq(RabbitMQConfig.ORDENES_EXCHANGE), 
            eq(RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY), 
            any(OrdenCompraDTO.class)
        )).thenReturn(true);
        Mockito.doNothing().when(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.ORDENES_EXCHANGE), 
            eq(RabbitMQConfig.ORDENES_PROVISION_ROUTING_KEY), 
            any(OrdenProvisionDTO.class)
        );

        cliente = new ClienteDTO(1, "DAN construcciones", BigDecimal.valueOf(550));
        Mockito.when(restTemplate.getForObject(PedidoService.URL_CLIENTES + pedidoNuevoDTO.getIdCliente(), ClienteDTO.class)).thenReturn(cliente);
    
        pedidoNuevoIncompleto = new Pedido(pedidoNuevoDTO);
        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoNuevoIncompleto); // No debe considerarse ese pedido, pero no puede usarse doNothing()
        Mockito.when(pedidoRepository.findByIdCliente(1)).thenReturn(pedidosViejos);
        Mockito.when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoNuevoIncompleto));
    }

    @Test
    public void peticionClienteFalla() {
        Mockito.when(restTemplate.getForObject(PedidoService.URL_CLIENTES + pedidoNuevoDTO.getIdCliente(), ClienteDTO.class)).thenThrow(HttpClientErrorException.class);
        assertTrue(pedidoService.save(pedidoNuevoDTO).getEstado().equals(EstadoPedido.RECIBIDO));
    }

    @Test
    public void montoMayorQueDescubierto() {
        assertTrue(pedidoService.save(pedidoNuevoDTO).getEstado().equals(EstadoPedido.RECHAZADO));
    }

    @Test
    public void montoMenorQueDescubiertoYStockActualizado() {
        cliente.setMaximoDescubierto(BigDecimal.valueOf(600));
        assertTrue(pedidoService.save(pedidoNuevoDTO).getEstado().equals(EstadoPedido.EN_PREPARACION));
    }

    @Test
    public void montoMenorQueDescubiertoYStockNoActualizado() {
        cliente.setMaximoDescubierto(BigDecimal.valueOf(600));
        /* for (OrdenCompraDTO ordenCompra: pedidoNuevoDTO.getProductos()) {
            Mockito.when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.ORDENES_EXCHANGE, 
                RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY, 
                ordenCompra
            )).thenReturn(false);
        } */
        Mockito.when(rabbitTemplate.convertSendAndReceive(
            eq(RabbitMQConfig.ORDENES_EXCHANGE), 
            eq(RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY), 
            any(OrdenCompraDTO.class)
        )).thenReturn(false);
        assertTrue(pedidoService.save(pedidoNuevoDTO).getEstado().equals(EstadoPedido.ACEPTADO));
    }

    @Test
    public void actualizarEstadoCancelado() {
        cliente.setMaximoDescubierto(BigDecimal.valueOf(600));
        /* for (OrdenCompraDTO ordenCompra: pedidoNuevoDTO.getProductos()) {
            Mockito.when(rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.ORDENES_EXCHANGE, 
                RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY, 
                ordenCompra
            )).thenReturn(false);
        } */
        Mockito.when(rabbitTemplate.convertSendAndReceive(
            eq(RabbitMQConfig.ORDENES_EXCHANGE), 
            eq(RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY), 
            any(OrdenCompraDTO.class)
        )).thenReturn(false);
        pedidoNuevoIncompleto.setEstado(EstadoPedido.EN_PREPARACION);
        
        pedidoService.actualizarEstado("idPedido", EstadoPedido.CANCELADO);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void actualizarEstadoEntregado() {
        pedidoNuevoIncompleto.setEstado(EstadoPedido.EN_PREPARACION);
        pedidoService.actualizarEstado("idPedido", EstadoPedido.ENTREGADO);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void actualizarEstadoIiniciado() {
        pedidoService.actualizarEstado("idPedido", EstadoPedido.INICIADO);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void actualizarEstadoRecibido() {
        pedidoService.actualizarEstado("idPedido", EstadoPedido.RECIBIDO);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void actualizarEstadoAceptado() {
        pedidoService.actualizarEstado("idPedido", EstadoPedido.ACEPTADO);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void actualizarEstadoRechazado() {
        pedidoService.actualizarEstado("idPedido", EstadoPedido.RECHAZADO);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

}
