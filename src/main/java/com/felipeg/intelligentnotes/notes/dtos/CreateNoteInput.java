package com.felipeg.intelligentnotes.notes.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateNoteInput {

    @NotBlank
    @Size(min = 1, max = 255, message = "Title must be between {min} and {max} chars")
    private String title;
    @NotBlank
    @Size(min = 1, max = 3000, message = "Content must be between {min} and {max} chars")
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
