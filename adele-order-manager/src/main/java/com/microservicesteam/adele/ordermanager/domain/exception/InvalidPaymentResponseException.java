package com.microservicesteam.adele.ordermanager.domain.exception;

public class InvalidPaymentResponseException extends RuntimeException {

    public InvalidPaymentResponseException(String message) {
        super(message);
    }

}
