package com.felipeg.intelligentnotes.notes.services;

import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.notes.repositories.NotesRepository;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Note createNote(String title, String content, Principal principal) {
        var user = getUserFrom(principal);
        var note = new Note(title, content, user);
        return notesRepository.save(note);
    }

    public Page<Note> listUserNotes(Principal principal, Pageable pageable) {
        var user = getUserFrom(principal);
        return notesRepository.findByUser(user, pageable);
    }

    private User getUserFrom(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }
}
