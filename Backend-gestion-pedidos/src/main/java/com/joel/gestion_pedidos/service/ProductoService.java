package com.joel.gestion_pedidos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.gestion_pedidos.model.rdbms.ProductoEntity;
import com.joel.gestion_pedidos.repository.rdbms.ProductoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Mono<ProductoEntity> crearProducto(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public Flux<ProductoEntity> listarProductos() {
        return productoRepository.findAll();
    }

    @Transactional
    public Mono<ProductoEntity> obtenerProducto(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional // Para operaciones de escritura
    public Mono<ProductoEntity> actualizarProducto(Long id, ProductoEntity productoActualizado) {
        return productoRepository.findById(id)
                .flatMap(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setStock(productoActualizado.getStock());
                    producto.setPrecio(productoActualizado.getPrecio());
                    return productoRepository.save(producto);
                });
    }

    @Transactional // Para operaciones de escritura
    public Mono<Void> eliminarProducto(Long id) {
        return productoRepository.deleteById(id);
    }
}
