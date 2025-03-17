package ru.digilabs.alkir.rahc.controller.v2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = InfoBaseIdIsNotEmptyValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InfoBaseIdIsNotEmpty {
    String message() default "ibId or ibName must be set";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
