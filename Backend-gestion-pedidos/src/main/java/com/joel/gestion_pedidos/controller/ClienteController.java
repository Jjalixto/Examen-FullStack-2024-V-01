package com.joel.gestion_pedidos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joel.gestion_pedidos.model.rdbms.ClienteEntity;
import com.joel.gestion_pedidos.service.ClienteService;
import com.joel.gestion_pedidos.util.exceptions.IdNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "cliente")
@Tag(name = "Cliente")
@AllArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @Operation(summary = "Create a new client")
    @PostMapping
    public Mono<ResponseEntity<ClienteEntity>> crearCliente(@Valid @RequestBody ClienteEntity cliente){
        return clienteService.crearCliente(cliente)
            .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }

    @Operation(summary = "List all clients")
    @GetMapping
    public Flux<ClienteEntity> listarCliente(){
        return clienteService.listarClientes();
    }

    @Operation(summary = "List find client_id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClienteEntity>> obtenerCliente(@PathVariable long id){
        return clienteService.obtenerCliente(id)
            .map(cliente -> ResponseEntity.ok(cliente))
            .switchIfEmpty(Mono.error(new IdNotFoundException("Cliente"))); 
    }

    @Operation(summary = "Update client")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClienteEntity>> actualizarCliente(@PathVariable Long id,@Valid @RequestBody ClienteEntity clienteActualizado){
        return clienteService.actualizarCliente(id, clienteActualizado)
            .map(c -> ResponseEntity.ok(c))
            .switchIfEmpty(Mono.error(new IdNotFoundException("Cliente")));
    }

    // @DeleteMapping("/{id}")
    // public Mono<ResponseEntity<Void>> eliminarCliente(@PathVariable Long id){
    //     return clienteService.eliminarCliente(id)
    //         .then(Mono.just(ResponseEntity.noContent().build())); 
    // }
}
