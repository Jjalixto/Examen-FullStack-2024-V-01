package com.joel.gestion_pedidos.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseErrorResponse implements Serializable{
    
    private String status;
    private Integer code;
}
