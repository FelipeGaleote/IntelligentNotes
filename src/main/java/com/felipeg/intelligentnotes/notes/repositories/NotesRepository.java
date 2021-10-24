package com.felipeg.intelligentnotes.notes.repositories;

import com.felipeg.intelligentnotes.notes.models.Note;
import com.felipeg.intelligentnotes.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {

    Page<Note> findByUser(User user, Pageable pageable);
}
