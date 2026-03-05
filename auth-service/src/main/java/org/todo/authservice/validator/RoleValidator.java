package org.todo.authservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.todo.authservice.model.Role;

public class RoleValidator implements ConstraintValidator<org.todo.authservice.validator.ValidRole, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Role.valueOf(value.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
