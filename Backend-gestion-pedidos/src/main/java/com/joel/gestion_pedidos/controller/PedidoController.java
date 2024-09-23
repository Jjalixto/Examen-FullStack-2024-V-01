package com.joel.gestion_pedidos.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joel.gestion_pedidos.dto.request.PedidoRequest;
import com.joel.gestion_pedidos.model.nosql.PedidoEntity;
import com.joel.gestion_pedidos.repository.nosql.PedidoRepository;
import com.joel.gestion_pedidos.service.PedidoService;
import com.joel.gestion_pedidos.service.SagaPedidoService;
import com.joel.gestion_pedidos.util.exceptions.IdNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("pedido")
@Tag(name = "Pedido")
@AllArgsConstructor
public class PedidoController {

        private final SagaPedidoService sagaPedidoService;
        private final PedidoService pedidoService;
        private final PedidoRepository pedidoRepository;

        @Operation(summary = "Create a new Order")
        @Transactional
        @PostMapping("/crear")
        public Mono<ResponseEntity<PedidoEntity>> crearPedido(@Valid @RequestBody PedidoRequest pedidoRequest) {

                // Convertir productoIds de Integer a Long
                List<Long> productoIds = pedidoRequest.getProductos().stream()
                                .map(Long::longValue)
                                .collect(Collectors.toList());

                // Crear un nuevo PedidoEntity basado en el DTO recibido
                PedidoEntity pedido = PedidoEntity.builder()
                                .clienteId(pedidoRequest.getClienteId())
                                .productos(productoIds) // Usar productoIds convertidos a Long
                                .total(0.0) // Inicialmente, el total se establecerá en 0 y se calculará en el servicio
                                .fechaPedido(LocalDateTime.now()) // Asignar la fecha actual
                                .build();

                // Llama al servicio de Saga para crear el pedido
                return sagaPedidoService.crearPedidoSaga(pedidoRequest.getClienteId(), productoIds, pedido)
                                .map(pedidoCreado -> ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado))
                                .onErrorResume(e -> {
                                        // Manejo de errores
                                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(null));
                                });
        }

        @Operation(summary = "Update order")
        @Transactional
        @PutMapping("/actualizar/{pedidoId}/{clienteId}")
        public Mono<ResponseEntity<PedidoEntity>> actualizarPedido(
                        @PathVariable int pedidoId,
                        @PathVariable Long clienteId,
                        @Valid @RequestBody PedidoEntity pedidoActualizado) {
                pedidoActualizado.setClienteId(clienteId); // Asegúrate de establecer el clienteId en el objeto
                                                           // actualizado
                return sagaPedidoService.actualizarPedidoSaga(pedidoId, pedidoActualizado)
                                .map(pedido -> ResponseEntity.ok(pedido)) // Retorna el pedido actualizado
                                .onErrorResume(e -> Mono.just(
                                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)))
                                .switchIfEmpty(Mono.error(new IdNotFoundException("Pedido"))); // Lanza excepción si no
                                                                                               // se encuentra
        }

        @Operation(summary = "List all orders")
        @GetMapping("/cliente/{clienteId}/pedidos")
        public Mono<ResponseEntity<List<PedidoEntity>>> obtenerPedidosPorCliente(@PathVariable Long clienteId) {
                return pedidoService.obtenerPedidosPorCliente(clienteId)
                                .map(pedidos -> ResponseEntity.ok(pedidos))
                                .switchIfEmpty(Mono.error(new IdNotFoundException("Pedidos"))); // Lanza excepción si no
                                                                                                // se encuentran pedidos
        }

        @Operation(summary = "Delete order")
        @DeleteMapping("/borrar/{clienteId}/{pedidoId}")
        public Mono<ResponseEntity<Object>> borrarPedidoAndCliente(@PathVariable Long clienteId,
                        @PathVariable Integer pedidoId) {
                return pedidoRepository.findByPedidoIdAndClienteId(pedidoId, clienteId)
                                .flatMap(pedido -> pedidoRepository.delete(pedido)
                                                .then(Mono.just(ResponseEntity.noContent().build())))
                                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body("Pedido no encontrado para el cliente especificado.")));
        }

}
