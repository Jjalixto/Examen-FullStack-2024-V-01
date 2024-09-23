package com.joel.gestion_pedidos.util.exceptions;

public class ForbiddenCustomerException extends RuntimeException{
    
    public ForbiddenCustomerException(){
        super("This Customer is blocked");
    }
}
