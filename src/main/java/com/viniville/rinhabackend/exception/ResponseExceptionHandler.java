package com.viniville.rinhabackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(ClienteNaoExisteException.class)
    public ResponseEntity<ErrorResponse> clienteNaoExisteException(ClienteNaoExisteException ex, WebRequest request) {
        return buildResponseError(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> saldoInsuficienteException(SaldoInsuficienteException ex, WebRequest request) {
        return buildResponseError(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler(ValidacaoRegistrarTransacaoException.class)
    public ResponseEntity<ErrorResponse> validacaoRegistrarTransacaoException(ValidacaoRegistrarTransacaoException ex, WebRequest request) {
        return buildResponseError(HttpStatus.BAD_REQUEST, ex);
    }

    private static ResponseEntity<ErrorResponse> buildResponseError(HttpStatus httpStatus, RuntimeException ex) {
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(
                        OffsetDateTime.now(ZoneOffset.UTC),
                        String.join(" - ", String.valueOf(httpStatus.value()), httpStatus.name()),
                        ex.getMessage()));
    }

    public record ErrorResponse(
            OffsetDateTime timestamp,
            String status,
            String message
    ) {
    }

}
