package isi.dan.ms.pedidos.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.OrdenCompraDTO;
import isi.dan.ms.pedidos.dto.OrdenProvisionDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.model.Cliente;
import isi.dan.ms.pedidos.model.DetallePedido;
import isi.dan.ms.pedidos.model.EstadoPedido;
import isi.dan.ms.pedidos.model.Pedido;
import isi.dan.ms.pedidos.model.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class PedidoService {
    
    // No estoy seguro si esto está bien
    private static String URL_PRODUCTOS = "http://ms-productos-svc:6180/api/productos/";
    private static String URL_CLIENTES = "http://ms-clientes-svc:6080/api/clientes/";

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    RestTemplate restTemplate;
    HttpHeaders header;
    Logger log;

    public PedidoService() {
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        log = LoggerFactory.getLogger(PedidoService.class);
    }
    
    // TODO: validar que usuario y obra pertenecen al cliente
    @SuppressWarnings("null")
    public Pedido save(PedidoDTO dto) {
    
        Pedido pedido = calcularTotalPedido(dto);
        try {
            Cliente cliente = restTemplate.getForObject(URL_CLIENTES + pedido.getIdCliente(), Cliente.class);
            
            // Evaluar si el monto del cliente supera el máximo descubierto
            if (this.calcularMontoCliente(pedido).compareTo(cliente.getMaximoDescubierto()) != -1)
                pedido.setEstado(EstadoPedido.RECHAZADO);  
            else {
                pedido.setEstado(EstadoPedido.ACEPTADO); 
                
                if (actualizarStockProductos(pedido))
                    pedido.setEstado(EstadoPedido.EN_PREPARACION);
                else
                    pedido.setEstado(EstadoPedido.ACEPTADO);
            } 
        } catch (HttpClientErrorException e) {
            pedido.setEstado(EstadoPedido.RECIBIDO);    
        }
        
        return pedidoRepository.save(pedido);
    }

    @SuppressWarnings("null")
    private Pedido calcularTotalPedido(PedidoDTO dto) {

        Pedido pedido = new Pedido(dto);

        List<DetallePedido> detallesInvalidos = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            try {
                // Llamar a ms-productos
                Producto producto = restTemplate.getForObject(URL_PRODUCTOS + detalle.getIdProducto(), Producto.class);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setDescuento(producto.getDescuentoPromocional());
                /* 
                detalle.setPrecioUnitario(
                    restTemplate.getForObject(URL_PRODUCTOS + "precio/" + detalle.getIdProducto(), BigDecimal.class));
                detalle.setDescuento(
                    restTemplate.getForObject(URL_PRODUCTOS + "descuento-Promocional/" + detalle.getIdProducto(), BigDecimal.class));            
                */
           }
            catch (HttpClientErrorException e) {
                detallesInvalidos.add(detalle);
            }
            detalle.setPrecioTotal(detalle.getPrecioUnitario().multiply(detalle.getDescuento()));
            pedido.setTotal(pedido.getTotal().add(detalle.getPrecioTotal()));
        }
        // Remover productos que no existen de los detalles        
        pedido.getDetalles().removeAll(detallesInvalidos);

        return pedido;
    }

    private BigDecimal calcularMontoCliente(Pedido pedido) {
        List<Pedido> pedidosPrevios = pedidoRepository.findByIdCliente(pedido.getIdCliente());
        return pedidosPrevios.stream()
                             .map(p -> p.getTotal())
                             .reduce(BigDecimal.ZERO, BigDecimal::add)
                             .add(pedido.getTotal());
    }

    private Boolean actualizarStockProductos(Pedido pedido) {
        
        Boolean operacionExitosa, todasLasOperacionesExitosas = true;
        for (DetallePedido detalle: pedido.getDetalles()) {    
            /* convertSendAndReceive() implementa el "reply queue pattern". Funcionamiento:
                    1. El remitente crea una cola temporal para recibir las respuestas
                    2. El remitente incluye el nombre de dicha cola en cada mensaje que envía
                    3. El consumidor procesa el mensaje y envía otro mensaje a la cola de respuestas
                    4. El remitente queda a la espera de la respuesta del consumidor
                No se usa REST como dice el enunciado porque contradeciría la cola que se implementó en ms-productos
            */
            operacionExitosa = (Boolean) rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.ORDENES_EXCHANGE, 
                RabbitMQConfig.ORDENES_COMPRA_ROUTING_KEY, 
                new OrdenCompraDTO(detalle.getIdProducto(), detalle.getCantidad())
            );
            todasLasOperacionesExitosas = todasLasOperacionesExitosas && (operacionExitosa == null? false: operacionExitosa);
        }

        return todasLasOperacionesExitosas;
    }

    public void actualizarEstado(String idPedido, EstadoPedido nuevoEstado) {
        Optional<Pedido> optionalPedido = this.findById(idPedido);
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            if (nuevoEstado.equals(EstadoPedido.CANCELADO))
            {
                if ( 
                    pedido.getEstado().equals(EstadoPedido.ACEPTADO) ||
                    pedido.getEstado().equals(EstadoPedido.EN_PREPARACION)
                ) {
                    pedido.setEstado(nuevoEstado);
                    pedidoRepository.save(pedido);

                    // Aumentar el stock que se descontó anteriormente
                    for (DetallePedido detalle: pedido.getDetalles()) {
                        rabbitTemplate.convertAndSend(
                            RabbitMQConfig.ORDENES_EXCHANGE, 
                            RabbitMQConfig.ORDENES_PROVISION_ROUTING_KEY, 
                            new OrdenProvisionDTO(detalle.getIdProducto(), detalle.getCantidad(), null)
                        );
                    }
                }
            }
            else if (nuevoEstado.equals(EstadoPedido.ENTREGADO))
                if (pedido.getEstado().equals(EstadoPedido.EN_PREPARACION)) {
                    pedido.setEstado(nuevoEstado);
                    pedidoRepository.save(pedido);
                }
        }
    }


    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    // Se permite la modificación sin restricciones (tener cuidado)
    public Pedido update(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> findById(String id) {
        return pedidoRepository.findById(id);
    }

    public void deleteById(String id) {
        pedidoRepository.deleteById(id);
    }
}
