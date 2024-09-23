package com.joel.gestion_pedidos.model.nosql;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "pedido")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PedidoEntity implements Serializable{
    
    @Id
    private String id;  // ID autogenerado por MongoDB
    
    @Field("cliente_id")
    private Long clienteId;  // ID del cliente que viene de PostgreSQL

    @Field("pedido_id")
    private int pedidoId;
    
    @Field("productos")
    private List<Long> productos;  // Lista de productos asociados al pedido

    @Field("total")
    private double total;

    @Field("fecha_pedido")
    private LocalDateTime fechaPedido;
}
