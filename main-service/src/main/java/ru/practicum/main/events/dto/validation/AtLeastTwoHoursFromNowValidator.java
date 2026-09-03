package ru.practicum.main.events.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class AtLeastTwoHoursFromNowValidator
        implements ConstraintValidator<AtLeastTwoHoursFromNow, LocalDateTime> {

    @Override
    public boolean isValid(
            LocalDateTime value,
            ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        LocalDateTime minimumDateTime =
                LocalDateTime.now().plusHours(2);

        return !value.isBefore(minimumDateTime);
    }
}