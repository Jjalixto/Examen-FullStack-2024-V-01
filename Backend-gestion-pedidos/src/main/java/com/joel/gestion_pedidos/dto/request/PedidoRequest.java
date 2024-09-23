package com.joel.gestion_pedidos.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PedidoRequest implements Serializable{
    private Long clienteId;          // ID del cliente que hace el pedido
    private List<Long> productos;  // Lista de IDs de los productos
    private double total;            // Total del pedido
}
