package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.VALID_REQUEST;

import com.microservicesteam.adele.booking.domain.BookingRequest;

public class BookingRequestValidator {

    public ValidationResult validate(BookingRequest request) {
        return VALID_REQUEST;
    }

}
