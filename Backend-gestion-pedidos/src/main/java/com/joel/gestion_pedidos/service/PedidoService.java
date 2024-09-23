package com.joel.gestion_pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.gestion_pedidos.model.nosql.PedidoEntity;
import com.joel.gestion_pedidos.repository.nosql.PedidoRepository;
import com.joel.gestion_pedidos.repository.rdbms.ClienteRepository;
import com.joel.gestion_pedidos.repository.rdbms.ProductoRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository; // MongoDB
    private final ClienteRepository clienteRepository; // PostgreSQL
    private final ProductoRepository productoRepository; // PostgreSQL

     // Paso 1: Validar Cliente
    public Mono<Boolean> validarCliente(Long clienteId) {
        return clienteRepository.existsById(clienteId)
            .flatMap(existe -> {
                if (!existe) {
                    return Mono.error(new RuntimeException("Cliente no encontrado"));
                }
                return Mono.just(true);
            });
    }

     // Paso 2: Validar Stock de Productos
    public Mono<Boolean> validarStock(List<Long> productoIds) {
        return productoRepository.findAllById(productoIds)
            .collectList()
            .flatMap(productos -> {
                if (productos.stream().anyMatch(producto -> producto.getStock() <= 0)) {
                    return Mono.error(new RuntimeException("Stock insuficiente"));
                }
                return Mono.just(true);
            });
    }

    // Paso 3: Crear Pedido en MongoDB
    @Transactional
    public Mono<PedidoEntity> crearPedido(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

      // Paso 4: Descontar Stock de Productos
    @Transactional
    public Mono<Void> descontarStock(List<Long> productoIds) {
        return productoRepository.findAllById(productoIds)
            .flatMap(producto -> {
                producto.setStock(producto.getStock() - 1);
                return productoRepository.save(producto);
            }).then();
    }

     // Paso 5: Calcular el total de los productos
    public Mono<Double> calcularTotal(List<Long> productoIds) {
        return productoRepository.findAllById(productoIds)
            .collectList()
            .map(productos -> productos.stream()
                .mapToDouble(producto -> producto.getPrecio()) // Asumiendo que cada producto tiene un método getPrecio()
                .sum());
    }

    public Mono<Void> eliminarPedido(String pedidoId) {
        return pedidoRepository.deleteById(pedidoId);
    }

    public Mono<Integer> obtenerSiguientePedidoId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)  // Esto debería devolver un Flux<PedidoEntity>
            .count() // Contar el número de pedidos
            .map(count -> count.intValue() + 1); // El siguiente pedidoId será el conteo actual + 1
    }
    
    @Transactional
    public Mono<PedidoEntity> actualizarPedido(int pedidoId, PedidoEntity pedidoActualizado) {
        return pedidoRepository.findByPedidoIdAndClienteId(pedidoId, pedidoActualizado.getClienteId()) // Ajusta esta consulta
            .flatMap(pedidoExistente -> {
                pedidoExistente.setProductos(pedidoActualizado.getProductos());
                pedidoExistente.setTotal(pedidoActualizado.getTotal());
                return pedidoRepository.save(pedidoExistente); // Guarda el pedido actualizado
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Pedido no encontrado"))); // Manejo de error si no existe
    }

    public Mono<List<PedidoEntity>> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
            .collectList();
    }

    public Mono<Void> eliminarPedido(int pedidoId, Long clienteId) {
        return pedidoRepository.findByPedidoIdAndClienteId(pedidoId, clienteId) // Buscar el pedido por clienteId y pedidoId
            .flatMap(pedido -> pedidoRepository.delete(pedido)) // Si existe, eliminarlo
            .switchIfEmpty(Mono.error(new RuntimeException("Pedido no encontrado"))); // Manejo de error si no se encuentra
    }
    
    public Mono<Void> borrarPedidoAndCliente(Long clienteId, int pedidoId) {
        return pedidoRepository.findByPedidoIdAndClienteId(pedidoId, clienteId)
            .flatMap(pedido -> {
                return pedidoRepository.delete(pedido); // Eliminar el pedido si se encuentra
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Pedido no encontrado"))); // Manejo de error
    }
}
