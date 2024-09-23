package com.joel.gestion_pedidos.controller.error_handler;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.joel.gestion_pedidos.dto.response.BaseErrorResponse;
import com.joel.gestion_pedidos.dto.response.ErrorResponse;
import com.joel.gestion_pedidos.dto.response.ErrorsResponse;
import com.joel.gestion_pedidos.util.exceptions.IdNotFoundException;
import com.joel.gestion_pedidos.util.exceptions.UsernameNotFoundException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {
    @ExceptionHandler({ IdNotFoundException.class, UsernameNotFoundException.class })
    public ErrorResponse handleIdNotFound(RuntimeException exception) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleIdNotFound(MethodArgumentNotValidException exception) {
        var errors = new ArrayList<String>();
        exception.getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        return ErrorsResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
