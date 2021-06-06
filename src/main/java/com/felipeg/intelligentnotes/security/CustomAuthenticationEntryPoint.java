package com.felipeg.intelligentnotes.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) {
        if (authenticationException != null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
