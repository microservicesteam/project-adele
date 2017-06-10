package com.microservicesteam.adele.booking.domain.validator;

import org.immutables.value.Value;

@Value.Immutable
public interface ValidationResult {

    ValidationResult VALID_REQUEST = ValidationResult.builder()
            .withValid(true)
            .withCode("000")
            .withMessage("Valid request")
            .build();

    boolean valid();

    String code();

    String message();

    class Builder extends ImmutableValidationResult.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}
