package com.joel.gestion_pedidos.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponse {
    @JsonProperty("is_success")
    private boolean is_success;
    private String message;
    private Data data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Data {
        private Long id;
        private String nombre;
    }
}
