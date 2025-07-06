package com.sporty.betting.settlement.publisher.dto;

import com.sporty.betting.settlement.publisher.fixtures.EventOutcomeDtoMother;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EventOutcomeDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validate_validDto_noViolations() {
        // GIVEN
        var dto = EventOutcomeDtoMother.valid();

        // WHEN
        Set<ConstraintViolation<EventOutcomeDto>> violations = validator.validate(dto);

        // THEN
        assertThat(violations).isEmpty();
    }

    @Test
    void validate_nullEventId_violationOnEventId() {
        // GIVEN
        var dto = EventOutcomeDtoMother.withNullEventId();

        // WHEN
        Set<ConstraintViolation<EventOutcomeDto>> violations = validator.validate(dto);

        // THEN
        assertThat(violations)
                .hasSize(1)
                .first()
                .extracting(ConstraintViolation::getPropertyPath)
                .asString()
                .isEqualTo("eventId");
    }

    @Test
    void validate_nullEventName_violationOnEventName() {
        // GIVEN
        var dto = EventOutcomeDtoMother.withNullEventName();

        // WHEN
        Set<ConstraintViolation<EventOutcomeDto>> violations = validator.validate(dto);

        // THEN
        assertThat(violations)
                .hasSize(1)
                .first()
                .extracting(ConstraintViolation::getPropertyPath)
                .asString()
                .isEqualTo("eventName");
    }

    @Test
    void validate_nullWinnerId_violationOnEventWinnerId() {
        // GIVEN
        var dto = EventOutcomeDtoMother.withNullWinnerId();

        // WHEN
        Set<ConstraintViolation<EventOutcomeDto>> violations = validator.validate(dto);

        // THEN
        assertThat(violations)
                .hasSize(1)
                .first()
                .extracting(ConstraintViolation::getPropertyPath)
                .asString()
                .isEqualTo("eventWinnerId");
    }

    @Test
    void validate_whenAllFieldsAreNull_thenAllViolationsAreReported() {
        // GIVEN
        EventOutcomeDto dto = EventOutcomeDtoMother.withAllFieldsNull();

        // WHEN
        Set<ConstraintViolation<EventOutcomeDto>> violations = validator.validate(dto);

        // THEN
        assertThat(violations).hasSize(3);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("eventId"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("eventName"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("eventWinnerId"));
    }
}
