package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import com.microservicesteam.adele.booking.domain.validator.ValidationResult;

@Value.Immutable
public interface ReservationRejected extends ReservationResponse {

    String code();

    String reason();

    class Builder extends ImmutableReservationRejected.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

    static ReservationRejected fromValidationResult(ValidationResult result) {
        return ReservationRejected.builder()
                .code(result.code())
                .reason(result.message())
                .build();
    }
}
