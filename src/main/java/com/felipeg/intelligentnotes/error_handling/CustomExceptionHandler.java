package com.felipeg.intelligentnotes.error_handling;

import io.swagger.v3.oas.annotations.Hidden;
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

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomExceptionHandler {

    private static final ErrorLogger errorLogger = ErrorLogger.createLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Hidden
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = createFieldErrorsMessage(fieldErrors);
        var errorId = errorLogger.logError(errorMessage, ex);
        var httpStatus = HttpStatus.BAD_REQUEST;


        var responseBuilder = new ErrorResponseBuilder();
        var errorResponse = responseBuilder
                .setTimestamp(new Date())
                .setError(httpStatus.getReasonPhrase())
                .setMessage(errorMessage)
                .setErrorId(errorId)
                .createErrorResponse();

        return new ResponseEntity<>(errorResponse, httpStatus);
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
