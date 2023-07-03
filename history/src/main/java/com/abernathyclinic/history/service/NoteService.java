package com.abernathyclinic.history.service;

import com.abernathyclinic.history.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    public Note createNote(Note note) {
        return null;
    }

    public List<Note> getNotes() {
        return null;
    }

    public Optional<Note> getNoteById(String noteId) {
        return Optional.empty();
    }

    public Note updateNote(Note note) {
        return note;
    }

    public void deleteNoteById(String noteId) {
    }
}
