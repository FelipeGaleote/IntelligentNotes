package com.felipeg.intelligentnotes.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var errorId = newErrorId();
        var httpStatus = HttpStatus.BAD_REQUEST;

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = createFieldErrorsMessage(fieldErrors);

        var responseBuilder = new ErrorResponseBuilder();
        var errorResponse = responseBuilder
                .setTimestamp(new Date())
                .setError(httpStatus.getReasonPhrase())
                .setMessage(errorMessage)
                .setErrorId(errorId)
                .createErrorResponse();

        logError(errorId, errorMessage, ex);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponse> handleGenericError(Throwable ex) {
        var errorId = newErrorId();
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = ex.getMessage() == null ? "An internal error occurred" : ex.getMessage();

        var responseBuilder = new ErrorResponseBuilder();
        var errorResponse = responseBuilder
                .setTimestamp(new Date())
                .setError(httpStatus.getReasonPhrase())
                .setMessage(errorMessage)
                .setErrorId(errorId)
                .createErrorResponse();

        logError(errorId, errorMessage, ex);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private String newErrorId() {
        return UUID.randomUUID().toString();
    }

    private void logError(String errorId, String message, Throwable throwable) {
        logger.error(() -> String.format("Error Id: %s - %s", errorId, message), throwable);
    }

    private String createFieldErrorsMessage(List<FieldError> fieldErrors) {
        var errors = new StringBuilder();
        int quantityOfFieldErrors = fieldErrors.size();
        if (quantityOfFieldErrors > 1) {
            for (var i = 0; i < quantityOfFieldErrors; i++) {
                errors.append(createFieldErrorMessage(i, fieldErrors));
            }
        } else {
            errors.append(fieldErrors.get(0).getDefaultMessage());
        }
        return errors.toString().trim();
    }

    private String createFieldErrorMessage(int errorIndex, List<FieldError> fieldErrors) {
        int prettyIndex = errorIndex + 1;
        return String.format(" %d) %s", prettyIndex, fieldErrors.get(errorIndex).getDefaultMessage());
    }
}
