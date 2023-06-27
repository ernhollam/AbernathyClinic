package com.abernathyclinic.patients.util;

import org.springframework.validation.Errors;

public class FieldValidationErrorMessageBuilder {

    public static String buildErrorMessage(Errors errors) {
        StringBuilder errorMessage = new StringBuilder();

        errors.getFieldErrors().forEach(error -> errorMessage.append(error.getField()));
        return errorMessage.substring(0, errorMessage.length()-2);
    }
}
