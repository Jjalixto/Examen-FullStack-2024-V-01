package com.joel.gestion_pedidos.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Gestion Pedidos API", 
        version = "1.0", 
        description = "Documentation for endpoint in Gestion_Pedidos")
)
public class OpenApiConfig {
    
}
