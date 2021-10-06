package com.felipeg.intelligentnotes.error_handling;

import java.util.Date;

public class ErrorResponseBuilder {
    private Date timestamp;
    private String error;
    private String message;
    private String errorId;

    public ErrorResponseBuilder setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ErrorResponseBuilder setError(String error) {
        this.error = error;
        return this;
    }

    public ErrorResponseBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseBuilder setErrorId(String errorId) {
        this.errorId = errorId;
        return this;
    }

    public ErrorResponse createErrorResponse() {
        return new ErrorResponse(timestamp, error, message, errorId);
    }
}