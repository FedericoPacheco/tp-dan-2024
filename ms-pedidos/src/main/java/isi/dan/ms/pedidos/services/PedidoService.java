package isi.dan.ms.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.model.DetallePedido;
import isi.dan.ms.pedidos.model.Pedido;
import isi.dan.ms.pedidos.model.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class PedidoService {
    
    // No estoy seguro si esto está bien
    private static String URL_PRODUCTOS = "http://ms-productos-svc:6180/api/productos/";

    @Autowired
    private PedidoRepository pedidoRepository;

    RestTemplate restTemplate;
    HttpHeaders header;

    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    Logger log = LoggerFactory.getLogger(PedidoService.class);


    public Pedido save(PedidoDTO dto) {
    
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        
        Pedido pedido = new Pedido(dto);

        // 1) Recuperar los precios de los productos y sus descuentos y calcular los totales
        List<DetallePedido> detallesInvalidos = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            try {
                detalle.setPrecioUnitario(
                    restTemplate.getForObject(URL_PRODUCTOS + "/precio/" + detalle.getIdProducto(), BigDecimal.class));
                detalle.setDescuento(
                    restTemplate.getForObject(URL_PRODUCTOS + "/descuento-Promocional/" + detalle.getIdProducto(), BigDecimal.class));            
            }
            catch (HttpClientErrorException e) {
                detallesInvalidos.add(detalle);
            }
            detalle.setPrecioTotal(detalle.getPrecioUnitario().multiply(detalle.getDescuento()));
            pedido.setTotal(pedido.getTotal().add(detalle.getPrecioTotal()));
        }
        // Remover productos que no existan de los detalles        
        pedido.getDetalles().removeAll(detallesInvalidos);
        


        
        /*
        ActualizarStockDTO dto = new ActualizarStockDTO();
        for(DetallePedido dp : pedido.getDetalle()) {
            dto.setIdProducto(dp.getProducto().getId());
            dto.setCantidad(dp.getCantidad());
            log.info("Enviando mensaje de reduccion de stock: " + dto);
            rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_ACTUALIZACION_STOCK, dto);
        }
         */
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido update(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido getById(String id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        pedidoRepository.deleteById(id);
    }
}
