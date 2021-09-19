package com.felipeg.intelligentnotes.authentication;

import com.felipeg.intelligentnotes.users.dtos.input.LoginInput;
import com.felipeg.intelligentnotes.users.dtos.input.SignUpInput;
import com.felipeg.intelligentnotes.users.dtos.output.LoginOutput;
import com.felipeg.intelligentnotes.users.dtos.output.SignUpOutput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationIntegrationTests {

    private static final String STANDARD_USERNAME = "felipe";
    private static final String STANDARD_PASSWORD = "111111";
    private static final String STANDARD_EMAIL = "felipe@teste.com";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testValidLogin() {
        doStandardSignup();

        LoginInput loginInput = new LoginInput();
        loginInput.setUsername(STANDARD_USERNAME);
        loginInput.setPassword(STANDARD_PASSWORD);

        ResponseEntity<LoginOutput> response = restTemplate.postForEntity("/user/login", loginInput, LoginOutput.class);
        LoginOutput responseBody = response.getBody();
        HttpHeaders responseHeaders = response.getHeaders();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getUsername()).isEqualTo(STANDARD_USERNAME);
        assertThat(responseBody.getEmail()).isNotBlank();
        assertThat(responseHeaders.get("Authorization")).isNotEmpty();
    }

    @Test
    public void testValidSignUp() {
        ResponseEntity<SignUpOutput> response = doStandardSignup();
        SignUpOutput responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUserId()).isEqualTo(1L);
    }

    private ResponseEntity<SignUpOutput> doStandardSignup() {
        SignUpInput signUpInput = new SignUpInput();
        signUpInput.setUsername(STANDARD_USERNAME);
        signUpInput.setPassword(STANDARD_PASSWORD);
        signUpInput.setEmail(STANDARD_EMAIL);

        return restTemplate.postForEntity("/user", signUpInput, SignUpOutput.class);
    }

}
