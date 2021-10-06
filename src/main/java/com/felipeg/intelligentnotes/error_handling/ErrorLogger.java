package com.felipeg.intelligentnotes.error_handling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class ErrorLogger {

    private final Logger logger;

    public static ErrorLogger createLogger(final Class<?> clazz) {
        return new ErrorLogger(clazz);
    }

    private ErrorLogger(final Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
    }

    public String logError(String message, Throwable throwable) {
        String errorId = newErrorId();
        logger.error(() -> String.format("Error Id: %s - %s", errorId, message), throwable);
        return errorId;
    }

    private String newErrorId() {
        return UUID.randomUUID().toString();
    }
}
