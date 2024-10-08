package isi.dan.ms.pedidos.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.OrdenCompraDTO;
import isi.dan.ms.pedidos.dto.OrdenProvisionDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.dto.ProductoDTO;
import isi.dan.ms.pedidos.model.DetallePedido;
import isi.dan.ms.pedidos.model.EstadoPedido;
import isi.dan.ms.pedidos.model.Pedido;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    
    public static String URL_PRODUCTOS = "http://ms-productos/api/productos/";
    public static String URL_CLIENTES = "http://ms-clientes/api/clientes/";

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RestTemplate restTemplate;
    
    @SuppressWarnings("null")
    public Pedido save(PedidoDTO dto) {
    
        Pedido pedido = calcularTotalPedido(dto);
        try {
            ClienteDTO cliente = restTemplate.getForObject(URL_CLIENTES + pedido.getIdCliente(), ClienteDTO.class);
            
            // Evaluar si el monto del cliente supera el máximo descubierto
            if (this.calcularMontoCliente(pedido.getIdCliente(), pedido.getTotal()).compareTo(cliente.getMaximoDescubierto()) > 0)
                pedido.setEstado(EstadoPedido.RECHAZADO);  
            else {
                pedido.setEstado(EstadoPedido.ACEPTADO); 
                
                if (actualizarStockProductos(pedido))
                    pedido.setEstado(EstadoPedido.EN_PREPARACION);
                else
                    pedido.setEstado(EstadoPedido.ACEPTADO);
            } 
        } catch (Exception e) {
            pedido.setEstado(EstadoPedido.RECIBIDO);    
        }
        pedidoRepository.save(pedido);

        return pedido;
    }

    @SuppressWarnings("null")
    private Pedido calcularTotalPedido(PedidoDTO dto) {

        Pedido pedido = new Pedido(dto);

        List<DetallePedido> detallesInvalidos = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            try {
                // Llamar a ms-productos
                ProductoDTO producto = restTemplate.getForObject(URL_PRODUCTOS + detalle.getIdProducto(), ProductoDTO.class);
                
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setDescuento(producto.getDescuentoPromocional());
                detalle.setPrecioTotal(
                BigDecimal.valueOf(detalle.getCantidad()).multiply(
                    detalle.getPrecioUnitario()).multiply(
                        BigDecimal.ONE.subtract(detalle.getDescuento()))
                );
                
                pedido.setTotal(pedido.getTotal().add(detalle.getPrecioTotal()));
            }
            catch (HttpClientErrorException e) {
                detallesInvalidos.add(detalle);
            }
        }
        // Remover productos que no existen de los detalles        
        pedido.getDetalles().removeAll(detallesInvalidos);

        return pedido;
    }

    private BigDecimal calcularMontoCliente(Integer idCliente, BigDecimal totalPedidoActual) {
        List<Pedido> pedidosPrevios = pedidoRepository.findByIdCliente(idCliente);
        return pedidosPrevios.stream()
                             .filter(p -> p.getEstado().equals(EstadoPedido.EN_PREPARACION) || p.getEstado().equals(EstadoPedido.ACEPTADO))
                             .map(p -> p.getTotal())
                             .reduce(BigDecimal.ZERO, BigDecimal::add)
                             .add(totalPedidoActual);
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
            operacionExitosa = (Boolean) rabbitTemplate.convertSendAndReceive( // Sincrono
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
                    this.update(pedido);

                    // Aumentar el stock que se descontó anteriormente
                    for (DetallePedido detalle: pedido.getDetalles()) {
                        rabbitTemplate.convertAndSend(  // Asincrono
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
                    this.update(pedido);
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

    public List<Pedido> findByIdCliente(Integer idCliente) {
        return pedidoRepository.findByIdCliente(idCliente);
    }

    public void deleteById(String id) {
        pedidoRepository.deleteById(id);
    }
}
