package com.joel.gestion_pedidos.service.auth;

import org.springframework.stereotype.Service;

import com.joel.gestion_pedidos.dto.request.LoginRequest;
import com.joel.gestion_pedidos.model.rdbms.ClienteEntity;
import com.joel.gestion_pedidos.repository.rdbms.ClienteRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthService {
    
    private final ClienteRepository clienteRepository;

    public Mono<ClienteEntity> autenticarCliente(LoginRequest loginRequest) {
        return clienteRepository.findByEmail(loginRequest.getEmail())
            .filter(cliente -> cliente.getPassword().equals(loginRequest.getPassword())) // Asegúrate de manejar la comparación de contraseñas de manera segura
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas"))); // Manejo de error
    }
}
