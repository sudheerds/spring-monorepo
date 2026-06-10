package com.example.validation.validator;

import com.example.validation.annotation.Alphanumeric;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, CharSequence> {
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return ALPHANUMERIC_PATTERN.matcher(value).matches();
    }
}
