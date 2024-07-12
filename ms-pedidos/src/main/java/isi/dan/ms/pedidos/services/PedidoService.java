package isi.dan.ms.pedidos.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ActualizarStockDTO;
import isi.dan.ms.pedidos.model.DetallePedido;
import isi.dan.ms.pedidos.model.Pedido;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger log = LoggerFactory.getLogger(PedidoService.class);


    public Pedido savePedido(Pedido pedido) {
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

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(String id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void deletePedido(String id) {
        pedidoRepository.deleteById(id);
    }
}
