package com.felipeg.intelligentnotes.user;

import com.felipeg.intelligentnotes.users.dtos.input.LoginInput;
import com.felipeg.intelligentnotes.users.dtos.input.SignUpInput;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTestsHelper {

    public static final String STANDARD_USERNAME = "felipe";
    public static final String STANDARD_PASSWORD = "111111";
    public static final String STANDARD_EMAIL = "felipe@teste.com";
    private static final String INVALID_PASSWORD = "111";

    public SignUpInput createDefaultSignUpInput() {
        SignUpInput signUpInput = new SignUpInput();
        signUpInput.setUsername(STANDARD_USERNAME);
        signUpInput.setPassword(STANDARD_PASSWORD);
        signUpInput.setEmail(STANDARD_EMAIL);
        return signUpInput;
    }

    public SignUpInput createSignUpInputWithInvalidPasswordLength() {
        SignUpInput signUpInput = new SignUpInput();
        signUpInput.setUsername(STANDARD_USERNAME);
        signUpInput.setPassword(INVALID_PASSWORD);
        signUpInput.setEmail(STANDARD_EMAIL);
        return signUpInput;
    }

    public LoginInput createDefaultLoginInput() {
        LoginInput loginInput = new LoginInput();
        loginInput.setUsername(STANDARD_USERNAME);
        loginInput.setPassword(STANDARD_PASSWORD);
        return loginInput;
    }

    public LoginInput createLoginInputWithInvalidPasswordLength() {
        LoginInput loginInput = new LoginInput();
        loginInput.setUsername(STANDARD_USERNAME);
        loginInput.setPassword(INVALID_PASSWORD);
        return loginInput;
    }


}
