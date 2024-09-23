package com.joel.gestion_pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.joel.gestion_pedidos.model.nosql.PedidoEntity;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SagaPedidoService {

    private PedidoService pedidoService;

    public Mono<PedidoEntity> crearPedidoSaga(Long clienteId, List<Long> productoIds, PedidoEntity pedido) {
        return pedidoService.validarCliente(clienteId)
                .flatMap(validado -> pedidoService.validarStock(productoIds))

                // Paso 1: Obtener el siguiente pedidoId
                .flatMap(validado -> pedidoService.obtenerSiguientePedidoId(clienteId)
                        .flatMap(siguienteId -> {
                            pedido.setPedidoId(siguienteId); // Asignar el siguiente pedidoId
                            return pedidoService.crearPedido(pedido); // Crear el pedido
                        }))

                // Paso 2: Descontar stock en PostgreSQL
                .flatMap(pedidoCreado -> pedidoService.descontarStock(productoIds)
                        .then(Mono.just(pedidoCreado)))

                // Paso 3: Calcular el total
                .flatMap(stockValidado -> pedidoService.calcularTotal(productoIds)
                        .flatMap(total -> {
                            pedido.setTotal(total); // Asignar el total al pedido
                            return pedidoService.crearPedido(pedido); // Crear el pedido nuevamente (esto puede ser
                                                                      // redundante, ya que ya lo creaste antes)
                        }))

                // Si ocurre un error en la creación o descuento, se elimina el pedido en
                // MongoDB
                .onErrorResume(e -> pedidoService.eliminarPedido(pedido.getId())
                        .then(Mono.error(new RuntimeException("Saga fallida y compensada"))));
    }

    public Mono<PedidoEntity> actualizarPedidoSaga(int pedidoId, PedidoEntity pedidoActualizado) {
        return pedidoService.validarCliente(pedidoActualizado.getClienteId())
                .flatMap(validado -> pedidoService.validarStock(pedidoActualizado.getProductos()))

                // Paso 1: Calcular el total de los productos
                .flatMap(stockValidado -> pedidoService.calcularTotal(pedidoActualizado.getProductos())
                        .flatMap(total -> {
                            // Establece el total en el pedido actualizado
                            pedidoActualizado.setTotal(total);
                            // Ahora actualiza el pedido
                            return pedidoService.actualizarPedido(pedidoId, pedidoActualizado);
                        }))

                // Si ocurre un error en la actualización, se devuelve un error
                .onErrorResume(
                        e -> Mono.error(new RuntimeException("Error al actualizar el pedido: " + e.getMessage())));
    }
}
