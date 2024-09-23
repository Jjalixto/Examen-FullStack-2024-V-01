package com.joel.gestion_pedidos.controller.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.joel.gestion_pedidos.dto.response.BaseErrorResponse;
import com.joel.gestion_pedidos.dto.response.ErrorResponse;
import com.joel.gestion_pedidos.util.exceptions.ForbiddenCustomerException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbbidenCustomerHandler {
    
    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleIdNotFound(ForbiddenCustomerException exception){
        return ErrorResponse.builder()
            .message(exception.getMessage())
            .status(HttpStatus.FORBIDDEN.name())
            .code(HttpStatus.FORBIDDEN.value())
            .build();
    }
}
