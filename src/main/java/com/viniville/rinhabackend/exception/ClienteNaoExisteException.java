package com.viniville.rinhabackend.exception;

public class ClienteNaoExisteException extends RuntimeException {
    public ClienteNaoExisteException(String message) {
        super(message);
    }
}
