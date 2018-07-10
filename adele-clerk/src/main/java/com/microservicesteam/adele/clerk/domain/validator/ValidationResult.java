package com.microservicesteam.adele.clerk.domain.validator;

public enum ValidationResult {

    VALID_REQUEST(true, "000", "Valid request"),
    INVALID_NO_TICKET(false, "001", "At least one ticket must be requested"),
    INVALID_TICKETS_OUT_OF_SECTOR(false, "002", "At least one of the tickets is invalid"),
    INVALID_TICKETS_RESERVED(false, "003", "At least one of the tickets is already reserved or sold");

    private boolean valid;
    private String code;
    private String message;

    ValidationResult(boolean valid, String code, String message) {
        this.valid = valid;
        this.code = code;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

}
