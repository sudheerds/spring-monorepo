package com.example.validation.validator;

import com.example.validation.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private List<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValue annotation) {
        ignoreCase = annotation.ignoreCase();
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull separately if null is not allowed
        }
        if (ignoreCase) {
            return acceptedValues.stream().anyMatch(val -> val.equalsIgnoreCase(value.toString()));
        }
        return acceptedValues.contains(value.toString());
    }
}
