package com.felipeg.intelligentnotes.notes.controllers;

import com.felipeg.intelligentnotes.notes.dtos.CreateNoteInput;
import com.felipeg.intelligentnotes.notes.dtos.NoteOutput;
import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.notes.repositories.NotesRepository;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController()
@RequestMapping("notes")
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteOutput> createNote(@RequestBody @Valid CreateNoteInput createNoteInput, Principal principal) {
        var note = new Note(createNoteInput.getTitle(), createNoteInput.getContent(), getUser(principal));
        note = notesRepository.save(note);
        var output = NoteOutput.from(note);
        return ResponseEntity.ok(output);
    }

    private User getUser(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteOutput>> listUserNotes(Principal principal) {
        var user = getUser(principal);
        List<Note> userNotes = notesRepository.findByUser(user);
        List<NoteOutput> userNotesOutput = NoteOutput.from(userNotes);
        return ResponseEntity.ok(userNotesOutput);
    }
}
