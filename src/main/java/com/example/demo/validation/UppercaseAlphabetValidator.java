package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UppercaseAlphabetValidator implements ConstraintValidator<UppercaseAlphabet, String> {
    private int minLength;
    private int maxLength;

    @Override
    public void initialize(UppercaseAlphabet annotation) {
        this.minLength = annotation.minLength();
        this.maxLength = annotation.maxLength();

        if (this.minLength < 0 || this.minLength > this.maxLength) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            value = "";
        }

        if (value.length() < this.minLength || value.length() > this.maxLength) {
            return false;
        }

        return value.matches("^[A-Z]*$");
    }
}
