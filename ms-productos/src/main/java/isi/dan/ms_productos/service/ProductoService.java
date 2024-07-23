package isi.dan.ms_productos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.dto.OrdenCompraDTO;
import isi.dan.ms_productos.dto.OrdenProvisionDTO;
import isi.dan.ms_productos.model.Producto;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    Logger log = LoggerFactory.getLogger(ProductoService.class);

    @RabbitListener(queues = RabbitMQConfig.ORDENES_COMPRA_QUEUE)
    public Boolean gestionarReducirStock(OrdenCompraDTO dto) {
        log.info("Reducir stock: " + dto);
        return this.reducirStock(dto);
    }

    @RabbitListener(queues = RabbitMQConfig.ORDENES_PROVISION_QUEUE)
    public void gestionarIncrementarStock(OrdenProvisionDTO dto) {
        log.info("Incrementar stock: " + dto);
        this.incrementarStock(dto);
    }

    public void incrementarStock(OrdenProvisionDTO dto) {
        Optional<Producto> optionalProducto = this.findById(dto.getIdProducto());
        if (optionalProducto.isPresent()) {
            optionalProducto.get().setStockActual(
                optionalProducto.get().getStockActual() + Math.abs(dto.getCantidad())
            );
            if (dto.getPrecio() != null)
                optionalProducto.get().setPrecio(dto.getPrecio());
            this.update(optionalProducto.get());
        }
    }

    public Boolean reducirStock(OrdenCompraDTO dto) {
        Optional<Producto> optionalProducto = this.findById(dto.getIdProducto());
        Boolean operacionExitosa = false;
        if (optionalProducto.isPresent()) {
            Integer nuevoStock = optionalProducto.get().getStockActual() - Math.abs(dto.getCantidad()); 
            if (nuevoStock > optionalProducto.get().getStockMinimo()) {
                optionalProducto.get().setStockActual(nuevoStock);
                this.update(optionalProducto.get());
                operacionExitosa = true;
            }    
        }
        return operacionExitosa;
    }

    /* 
    public BigDecimal getPrecioFinal(Integer idProducto) {
        Optional<Producto> optionalProducto = this.findById(idProducto);
        if (optionalProducto.isPresent())
            return optionalProducto.get().getPrecio().multiply(optionalProducto.get().getDescuentoPromocional());
        else throw new NoSuchElementException();
        /* 
        return idProductos.stream()
                          .map(id -> this.findById(id))
                          .filter(opt -> opt.isPresent())
                          .map(opt -> opt.get().getPrecio().multiply(opt.get().getDescuentoPromocional()))
                          .toList();
        
    }
    */

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

