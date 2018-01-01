package com.microservicesteam.adele.clerk.domain.validator;

public enum ValidationResult {

    VALID_REQUEST(true, "000", "Valid request"),
    INVALID_POSITIONS_EMPTY(false, "001", "At least one position must be requested"),
    INVALID_POSITIONS_OUT_OF_SECTOR(false, "002", "At least one of the positions is invalid"),
    INVALID_POSITIONS_BOOKED(false, "003", "At least one of the positions is already booked");

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
