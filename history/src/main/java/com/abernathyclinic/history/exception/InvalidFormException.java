package com.abernathyclinic.history.exception;

import java.io.IOException;

/**
 * Exception thrown when fields in the form are invalid.
 */
public class InvalidFormException extends IOException {
    /**
     * Exception thrown when fields in the form are invalid.
     *
     * @param message Exception message.
     */
    public InvalidFormException(String message) {
        super(message);
    }
}
