package com.joel.gestion_pedidos.repository.rdbms;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.joel.gestion_pedidos.model.rdbms.ClienteEntity;

import reactor.core.publisher.Mono;

public interface ClienteRepository extends ReactiveCrudRepository<ClienteEntity,Long>{
    Mono<ClienteEntity> findByEmail(String email);
}
