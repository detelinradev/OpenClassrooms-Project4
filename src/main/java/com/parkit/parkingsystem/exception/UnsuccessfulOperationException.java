package com.parkit.parkingsystem.exception;

/**
 * Creates more business specific exception able to present otherwise not
 * exceptional states as exceptional following the application logic.
 */
public class UnsuccessfulOperationException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param errorMessage  <code>String</code> variable as parameter
     *                    representing error message explaining the cause for
     *                    the exception
     */
    public UnsuccessfulOperationException(String errorMessage) {

        super(errorMessage);
    }

    /**
     * Constructs a new exception with the specified detail message and causing
     * exception.
     *
     * @param errorMessage  <code>String</code> variable as parameter
     *                     representing error message explaining the cause for
     *                     the exception
     * @param error  instance of <code>Throwable</code> causing this exception
     */
    public UnsuccessfulOperationException(String errorMessage, Throwable error) {

        super(errorMessage, error);
    }
}
