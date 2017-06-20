package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import com.microservicesteam.adele.booking.domain.validator.ValidationResult;

@Value.Immutable
public interface BookingRejected extends BookingResponse {

    String code();

    String reason();

    class Builder extends ImmutableBookingRejected.Builder {
    }

    static BookingRejected.Builder builder() {
        return new BookingRejected.Builder();
    }

    static BookingRejected fromValidationResult(ValidationResult result) {
        return BookingRejected.builder()
                .code(result.code())
                .reason(result.message())
                .build();
    }
}
