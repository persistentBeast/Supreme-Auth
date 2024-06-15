package org.supreme.exceptions;

public class UnauthorisedRequestException extends RuntimeException{
    public UnauthorisedRequestException(String message) {
        super(message);
    }
}
