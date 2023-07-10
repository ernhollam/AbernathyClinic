package com.abernathyclinic.history.util;

import com.abernathyclinic.history.exception.InvalidFormException;
import org.springframework.validation.Errors;

public class InvalidFormMessageBuilder {

    public static String buildErrorMessage(Errors errors) throws InvalidFormException {
        StringBuilder errorMessage = new StringBuilder();

        errors.getFieldErrors().forEach(error -> errorMessage.append(error.getField()).append(", "));
        throw new InvalidFormException("Please fix the following fields: \n"
                + errorMessage.substring(0, errorMessage.length()-2));
    }
}
