package com.felipeg.intelligentnotes.notes.controllers;

import com.felipeg.intelligentnotes.error_handling.ErrorResponse;
import com.felipeg.intelligentnotes.notes.dtos.CreateNoteInput;
import com.felipeg.intelligentnotes.notes.dtos.NoteOutput;
import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.notes.repositories.NotesRepository;
import com.felipeg.intelligentnotes.users.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Create a new note for the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created",
                    content = {
                            @Content(schema = @Schema(implementation = NoteOutput.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {
                            @Content(schema = @Schema(hidden = true))
                    })
    })
    public ResponseEntity<NoteOutput> createNote(@RequestBody @Valid CreateNoteInput createNoteInput, Principal principal) {
        var note = new Note(createNoteInput.getTitle(), createNoteInput.getContent(), getUser(principal));
        note = notesRepository.save(note);
        var output = NoteOutput.from(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    private User getUser(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List the notes of the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed notes",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = NoteOutput.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {
                            @Content(schema = @Schema(hidden = true))
                    })
    })
    public ResponseEntity<List<NoteOutput>> listUserNotes(Principal principal) {
        var user = getUser(principal);
        List<Note> userNotes = notesRepository.findByUser(user);
        List<NoteOutput> userNotesOutput = NoteOutput.from(userNotes);
        return ResponseEntity.ok(userNotesOutput);
    }
}
