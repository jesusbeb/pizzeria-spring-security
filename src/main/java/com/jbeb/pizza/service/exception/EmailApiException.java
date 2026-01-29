package com.jbeb.pizza.service.exception;

// Creamos una excepcion para el metodo que envia un email
public class EmailApiException extends RuntimeException{

    // Constructor que basicamente llama al constructor de super e imprime en consola
    public EmailApiException(){
        super("Error sending email!!!...");
    }
}
