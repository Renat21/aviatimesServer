package com.example.aviatimes.Models.Exception.FieldError;

import java.util.List;

public class FieldErrorResponse {
    private List<CustomFieldError> fieldErrors;

    public FieldErrorResponse() {
    }

    public List<CustomFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<CustomFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
