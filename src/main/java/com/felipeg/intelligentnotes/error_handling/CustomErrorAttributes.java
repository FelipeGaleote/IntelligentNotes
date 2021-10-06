package com.felipeg.intelligentnotes.error_handling;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final ErrorLogger errorLogger = ErrorLogger.createLogger(CustomErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        String errorMessage = error.getMessage();
        String errorId = errorLogger.logError(errorMessage, error);
        errorAttributes.remove("status");
        errorAttributes.remove("path");
        errorAttributes.put("errorId", errorId);
        return errorAttributes;
    }
}
