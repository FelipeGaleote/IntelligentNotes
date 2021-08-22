package com.felipeg.intelligentnotes.notes.repositories;

import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);
}
