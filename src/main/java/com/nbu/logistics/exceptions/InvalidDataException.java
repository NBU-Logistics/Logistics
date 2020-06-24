package com.nbu.logistics.exceptions;

/**
 * This is a custom exception that is used when some data validation fails.
 */
public class InvalidDataException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}