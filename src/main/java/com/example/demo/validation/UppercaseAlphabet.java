package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UppercaseAlphabetValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UppercaseAlphabet {
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;

    String message() default "Should be uppercase alphabet.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
