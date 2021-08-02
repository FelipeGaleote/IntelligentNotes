package com.felipeg.intelligentnotes.notes.controllers;

import com.felipeg.intelligentnotes.notes.dtos.CreateNoteInput;
import com.felipeg.intelligentnotes.notes.dtos.CreateNoteOutput;
import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.notes.repositories.NotesRepository;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.TimeZone;

@RestController()
@RequestMapping("notes")
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @PostMapping()
    public ResponseEntity<CreateNoteOutput> createNote(@RequestBody @Valid CreateNoteInput createNoteInput, Principal principal) {
        var note = new Note(createNoteInput.getTitle(), createNoteInput.getContent(), getUser(principal));
        note = notesRepository.save(note);
        var output = CreateNoteOutput.from(note);
        return ResponseEntity.ok(output);
    }

    private User getUser(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }
}
