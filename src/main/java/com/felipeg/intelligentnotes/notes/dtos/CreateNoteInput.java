package com.felipeg.intelligentnotes.notes.dtos;

import javax.validation.constraints.NotBlank;

public class CreateNoteInput {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
