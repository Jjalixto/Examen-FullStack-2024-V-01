package com.joel.gestion_pedidos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joel.gestion_pedidos.model.rdbms.ProductoEntity;
import com.joel.gestion_pedidos.service.ProductoService;
import com.joel.gestion_pedidos.util.exceptions.IdNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "producto")
@Tag(name = "Producto")
@AllArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;

    @Operation(summary = "Create a new Product")
    @PostMapping
    public Mono<ResponseEntity<ProductoEntity>> crearProducto(@Valid @RequestBody ProductoEntity producto){
        return productoService.crearProducto(producto)
            .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null))); // Manejo de errores
    }

    @Operation(summary = "List all products")
    @GetMapping
    public Flux<ProductoEntity> listarProducto(){
        return productoService.listarProductos();
    }

    @Operation(summary = "List all products find by client_id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductoEntity>> obtenerProducto(@PathVariable Long id){
        return productoService.obtenerProducto(id)
            .map(producto -> ResponseEntity.ok(producto))
            .switchIfEmpty(Mono.error(new IdNotFoundException("Producto")));
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductoEntity>> actualizarProducto(@PathVariable Long id,@Valid @RequestBody ProductoEntity productoActualizado){
        return productoService.actualizarProducto(id, productoActualizado)
            .map(c -> ResponseEntity.ok(c))
            .switchIfEmpty(Mono.error(new IdNotFoundException("Producto")));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable Long id){
        return productoService.eliminarProducto(id)
            .then(Mono.just(ResponseEntity.noContent().build())); 
    }
}
