package com.felipeg.intelligentnotes.users.dtos.output;

public class SignUpOutput {

    private Long userId;

    public SignUpOutput(Long userId) {
        this.userId = userId;
    }

    public SignUpOutput() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
