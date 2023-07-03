package com.abernathyclinic.history.exception;

/**
 * Exception thrown when the patient to be updated or deleted does not exist.
 */
public class NoteNotFoundException extends RuntimeException {
    /**
     * Exception thrown when the patient to be updated or deleted does not exist.
     *
     * @param message Exception message.
     */
    public NoteNotFoundException(String message) {
        super(message);
    }
}
