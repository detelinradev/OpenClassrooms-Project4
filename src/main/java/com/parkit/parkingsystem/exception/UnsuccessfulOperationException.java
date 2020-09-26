package com.parkit.parkingsystem.exception;

public class UnsuccessfulOperationException extends RuntimeException {

    public UnsuccessfulOperationException(String errorMessage) {

        super(errorMessage);
    }

    public UnsuccessfulOperationException(String errorMessage, Throwable error) {

        super(errorMessage, error);
    }
}
