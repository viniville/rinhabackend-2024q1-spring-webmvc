package com.viniville.rinhabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(ClienteNaoExisteException.class)
    public ResponseEntity<Void> clienteNaoExisteException(ClienteNaoExisteException ex, WebRequest request) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Void> saldoInsuficienteException(SaldoInsuficienteException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    record ErrorResponse (
            OffsetDateTime timestamp,
            String status,
            String error
    ) {}

}
