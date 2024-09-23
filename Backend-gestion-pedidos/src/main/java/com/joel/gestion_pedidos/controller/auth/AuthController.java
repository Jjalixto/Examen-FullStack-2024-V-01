package com.joel.gestion_pedidos.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joel.gestion_pedidos.dto.request.LoginRequest;
import com.joel.gestion_pedidos.dto.response.LoginResponse;
import com.joel.gestion_pedidos.service.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "")
@Tag(name = "Login")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.autenticarCliente(loginRequest)
                .map(cliente -> {
                    LoginResponse response = new LoginResponse(
                            true,
                            "Inicio de sesión exitoso. Bienvenido " + cliente.getNombre(),
                            new LoginResponse.Data(cliente.getId(),cliente.getNombre()));
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(false, "Credenciales inválidas", null))));
    }

}
