package com.abernathyclinic.history.exception;

/**
 * Exception thrown when the patient to be updated or deleted does not exist.
 */
public class PatientNotFoundException extends RuntimeException {
    /**
     * Exception thrown when the patient to be updated or deleted does not exist.
     *
     * @param message Exception message.
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}
