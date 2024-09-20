package com.tasks.user_management.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    private static final Set<String> VALID_ROLES = Set.of("manager", "admin", "user");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return VALID_ROLES.contains(s.toLowerCase());
    }
}
