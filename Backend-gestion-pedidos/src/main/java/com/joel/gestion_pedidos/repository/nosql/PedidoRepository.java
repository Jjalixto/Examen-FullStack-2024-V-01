package com.joel.gestion_pedidos.repository.nosql;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.joel.gestion_pedidos.model.nosql.PedidoEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PedidoRepository extends ReactiveMongoRepository<PedidoEntity,String>{
    // Método para eliminar pedidos por clienteId
    Mono<Void> deleteByClienteId(Long clienteId);
    Flux<PedidoEntity> findByClienteId(Long clienteId);
    Mono<PedidoEntity> findByPedidoIdAndClienteId(int pedidoId, Long clienteId);
}
