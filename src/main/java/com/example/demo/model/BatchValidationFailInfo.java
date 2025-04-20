package com.example.demo.model;

import java.util.List;

public class BatchValidationFailInfo {
    private List<String> invalidFields;

    public List<String> getInvalidFields() {
        return invalidFields;
    }

    public void setInvalidFields(List<String> invalidFields) {
        this.invalidFields = invalidFields;
    }
}
