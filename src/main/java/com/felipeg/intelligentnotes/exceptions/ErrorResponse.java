package com.felipeg.intelligentnotes.exceptions;

import java.util.Date;

public final class ErrorResponse {
    private final Date timestamp;
    private final String error;
    private final String message;
    private final String errorId;

    public ErrorResponse(Date timestamp, String error, String message, String errorId) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.errorId = errorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorId() {
        return errorId;
    }
}
