package com.felipeg.intelligentnotes.notes.controllers;

import com.felipeg.intelligentnotes.common.ApiPageable;
import com.felipeg.intelligentnotes.error_handling.ErrorResponse;
import com.felipeg.intelligentnotes.notes.services.NotesService;
import com.felipeg.intelligentnotes.notes.dtos.CreateNoteInput;
import com.felipeg.intelligentnotes.notes.dtos.NoteOutput;
import com.felipeg.intelligentnotes.notes.models.Note;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController()
@RequestMapping("notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

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
        var note = notesService.createNote(createNoteInput.getTitle(), createNoteInput.getContent(), principal);
        var output = NoteOutput.fromNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List the notes of the logged in user. The response is paginated and supports sorting, these settings can be defined using URL parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed notes"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {
                            @Content(schema = @Schema(hidden = true))
                    })
    })
    @ApiPageable
    public ResponseEntity<Page<NoteOutput>> listUserNotes(Principal principal, @Parameter(hidden = true) @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Note> userNotes = notesService.listUserNotes(principal, pageable);
        Page<NoteOutput> userNotesOutput = NoteOutput.fromNotes(userNotes);
        return ResponseEntity.ok(userNotesOutput);
    }
}
