package com.felipeg.intelligentnotes.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler extends ResponseEntityExceptionHandler {

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = createFieldErrorsMessage(fieldErrors);
        Object path = getPath(request);

        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", errorMessage);
        body.put("path", path);

        return new ResponseEntity<>(body, headers, status);
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

    private Object getPath(WebRequest request) {
        return request.getAttribute(UrlPathHelper.PATH_ATTRIBUTE, 0);
    }
}
