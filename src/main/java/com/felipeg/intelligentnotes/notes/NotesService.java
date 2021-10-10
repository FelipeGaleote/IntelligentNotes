package com.felipeg.intelligentnotes.notes;

import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.notes.repositories.NotesRepository;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

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

    public List<Note> listUserNotes(Principal principal) {
        var user = getUserFrom(principal);
        return notesRepository.findByUser(user);
    }

    private User getUserFrom(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }
}
