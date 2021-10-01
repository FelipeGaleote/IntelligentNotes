package com.felipeg.intelligentnotes.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipeg.intelligentnotes.users.dtos.input.LoginInput;
import com.felipeg.intelligentnotes.users.dtos.input.SignUpInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationTestsHelper authenticationTestsHelper;

    @Test
    public void testSignUpWithSuccess() throws Exception {
        signUp(authenticationTestsHelper.createDefaultSignUpInput())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    public void testSignUpWithInvalidPasswordLength() throws Exception {
        signUp(authenticationTestsHelper.createSignUpInputWithInvalidPasswordLength())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Password should be between 6 and 100 characters."));
    }

    @Test
    public void testLoginWithSuccess() throws Exception {
        signUp(authenticationTestsHelper.createDefaultSignUpInput());

        login(authenticationTestsHelper.createDefaultLoginInput())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value(AuthenticationTestsHelper.STANDARD_USERNAME))
                .andExpect(jsonPath("$.email").value(AuthenticationTestsHelper.STANDARD_EMAIL))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        signUp(authenticationTestsHelper.createDefaultSignUpInput());

        LoginInput loginInput = new LoginInput();
        loginInput.setUsername(AuthenticationTestsHelper.STANDARD_USERNAME);
        loginInput.setPassword("wrong_password");
        login(loginInput)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    public void testLoginWithInvalidPasswordLength() throws Exception {
        signUp(authenticationTestsHelper.createDefaultSignUpInput());

        login(authenticationTestsHelper.createLoginInputWithInvalidPasswordLength())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Password should be between 6 and 100 characters."));
    }

    private ResultActions signUp(SignUpInput signUpInput) throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(signUpInput);
        return this.mockMvc.perform(post("/user").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }

    private ResultActions login(LoginInput loginInput) throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(loginInput);
        return this.mockMvc.perform(post("/user/login").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }


}
