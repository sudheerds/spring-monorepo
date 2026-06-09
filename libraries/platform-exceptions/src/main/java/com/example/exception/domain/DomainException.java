package com.example.exception.domain;

import org.springframework.http.HttpStatus;

public class DomainException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public DomainException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public DomainException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
