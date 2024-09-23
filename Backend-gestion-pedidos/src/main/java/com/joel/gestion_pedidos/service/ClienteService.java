package com.joel.gestion_pedidos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.gestion_pedidos.model.rdbms.ClienteEntity;
import com.joel.gestion_pedidos.repository.nosql.PedidoRepository;
import com.joel.gestion_pedidos.repository.rdbms.ClienteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Mono<ClienteEntity> crearCliente(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Flux<ClienteEntity> listarClientes() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Mono<ClienteEntity> obtenerCliente(Long id) {
        return clienteRepository.findById(id);
    }

    public Mono<ClienteEntity> actualizarCliente(Long id, ClienteEntity clienteActualizado) {
        return clienteRepository.findById(id)
                .flatMap(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setEmail(clienteActualizado.getEmail());
                    cliente.setDireccion(clienteActualizado.getDireccion());
                    cliente.setTelefono(clienteActualizado.getTelefono());
                    return clienteRepository.save(cliente);
                });
    }

    public Mono<Void> eliminarClienteConPedidos(Long clienteId) {

        // Paso 1: Eliminar pedidos del cliente en MongoDB
        Mono<Void> eliminarPedidos = pedidoRepository.deleteByClienteId(clienteId);

        // Paso 2: Eliminar cliente en PostgreSQL
        Mono<Void> eliminarCliente = clienteRepository.deleteById(clienteId);

        // Paso 3: Realizar ambas eliminaciones de forma reactiva y transaccional
        return eliminarPedidos
                .then(eliminarCliente)
                .onErrorResume(e -> {
                    // Aquí puedes manejar la compensación en caso de fallo (restaurar cliente,
                    // etc.)
                    return Mono.error(new RuntimeException("Error eliminando cliente y pedidos: " + e.getMessage()));
                });
    }
}
