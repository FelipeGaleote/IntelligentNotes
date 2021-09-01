package com.felipeg.intelligentnotes.users.dtos.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginInput {

    @NotBlank(message = "Username is mandatory.")
    @Size(min = 1, max = 60, message = "Username should be between {min} and {max} characters.")
    private String username;

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, max = 100, message = "Password should be between {min} and {max} characters.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
