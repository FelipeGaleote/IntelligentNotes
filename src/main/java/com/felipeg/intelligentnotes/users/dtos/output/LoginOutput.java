package com.felipeg.intelligentnotes.users.dtos.output;

import com.felipeg.intelligentnotes.users.models.User;

public class LoginOutput {

    private Long id;
    private String username;
    private String email;

    public LoginOutput(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static LoginOutput from(User user) {
        return new LoginOutput(user.getId(), user.getUsername(), user.getEmail());
    }
}
