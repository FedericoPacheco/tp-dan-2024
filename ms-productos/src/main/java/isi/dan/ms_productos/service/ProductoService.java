package isi.dan.ms_productos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.dto.ActualizarStockDTO;
import isi.dan.ms_productos.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    Logger log = LoggerFactory.getLogger(ProductoService.class);

    @RabbitListener(queues = RabbitMQConfig.COLA_ACTUALIZACION_STOCK)
    public void gestionarReducirStock(ActualizarStockDTO dto) {
        log.info("Reducir stock: " + dto);
        this.reducirStock(dto);
    }


    public void incrementarStock(ActualizarStockDTO dto, BigDecimal precio) {
        Optional<Producto> optionalProducto = this.actualizarStock(dto, 1, false);
        if (optionalProducto.isPresent()) {
            optionalProducto.get().setPrecio(precio);
            productoRepository.save(optionalProducto.get());
        }
    }

    public void reducirStock(ActualizarStockDTO dto) {
        this.actualizarStock(dto, -1, true);
    }

    private Optional<Producto> actualizarStock(ActualizarStockDTO dto, Integer signo, Boolean persistir)
    {
        Optional<Producto> optionalProducto = productoRepository.findById(dto.getIdProducto());
        if (optionalProducto.isPresent()) {
            optionalProducto.get().setStockActual(
                Math.max(
                    optionalProducto.get().getStockActual() + signo * Math.abs(dto.getCantidad()), 
                    0
                )
            );
            if (persistir) 
                productoRepository.save(optionalProducto.get());
        }
        return optionalProducto;
    }


    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto update(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteById(Integer id) {
        productoRepository.deleteById(id);
    }
}

