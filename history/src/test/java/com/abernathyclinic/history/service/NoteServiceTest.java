package com.abernathyclinic.history.service;

import com.abernathyclinic.history.exception.NoteNotFoundException;
import com.abernathyclinic.history.model.Note;
import com.abernathyclinic.history.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(NoteService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteServiceTest {
    @Autowired
    private NoteService noteService;
    @MockBean
    private NoteRepository noteRepository;

    private Note note;
    private Note otherNote;
    private Note samePatientNote;
    private List<Note> notes;

    @BeforeEach
    void setUp() {
        note = new Note("NOTE001", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level");
        otherNote = new Note("NOTE002", 2, "Patient: TestBorderline Practitioner's notes/recommendations: Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late");
        samePatientNote = new Note("NOTE003", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling very strong'");
        notes = List.of(note, otherNote, samePatientNote);
    }

    @Test
    @DisplayName("Registering new note with valid information should save note to database")
    void createNote_shouldCreate_newNote() {
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        note = noteService.createNote(note);

        verify(noteRepository, times(1)).save(any(Note.class));
        assertThat(note).isNotNull();
    }

    @Test
    @DisplayName("getNotes() should return list of existing notes")
    void getNotes() {
        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteService.getNotes();

        assertTrue(result.contains(note));
        assertTrue(result.contains(otherNote));
        assertTrue(result.contains(samePatientNote));
    }

    @Test
    @DisplayName("Id should not be null when calling getNoteById")
    void getNoteById_whenIDIsNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> noteService.getNoteById(null));
    }

    @Test
    @DisplayName("getNoteById() should return Optional of existing note")
    void getNoteById() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.ofNullable(note));
        Optional<Note> result = noteService.getNoteById(note.getId());
        assertTrue(result.isPresent());
        assertEquals(note.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Updating note which does not exist should throw NoteNotFoundException")
    void updateNote_whoDoesNotExist_shouldThrow_NoteNotFoundException() {
        when(noteRepository.existsById(any(String.class))).thenReturn(false);
        assertThrows(NoteNotFoundException.class, () -> noteService.updateNote(note));
    }

    @Test
    @DisplayName("Updating existing note should save changes to database")
    void updateNote_whichExists_shouldUpdate_existingNote() {
        when(noteRepository.existsById(any(String.class))).thenReturn(true);
        String newContent = "this is a new content for the test";
        note.setContent(newContent);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        note = noteService.updateNote(note);

        verify(noteRepository, times(1)).save(any(Note.class));
        assertThat(note).isNotNull();
        assertEquals(newContent, note.getContent());
    }

    @Test
    @DisplayName("Note should not be null when calling deleteNote()")
    void deleteNote_whenNoteIsNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> noteService.deleteNoteById(null));
    }

    @Test
    @DisplayName("Deleting existing note should delete them from database")
    void deleteNote() {
        when(noteRepository.existsById(any(String.class))).thenReturn(true);
        String idBeforeDeletion = note.getId();
        noteService.deleteNoteById(idBeforeDeletion);
        verify(noteRepository, times(1)).deleteById(any(String.class));
    }
}