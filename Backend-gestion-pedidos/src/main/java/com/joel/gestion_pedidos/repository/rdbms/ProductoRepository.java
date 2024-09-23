package com.joel.gestion_pedidos.repository.rdbms;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.joel.gestion_pedidos.model.rdbms.ProductoEntity;

public interface ProductoRepository extends ReactiveCrudRepository<ProductoEntity,Long>{
}
