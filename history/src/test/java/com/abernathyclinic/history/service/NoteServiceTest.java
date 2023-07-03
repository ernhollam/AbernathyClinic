package com.abernathyclinic.history.service;

import com.abernathyclinic.history.bean.PatientBean;
import com.abernathyclinic.history.exception.NoteNotFoundException;
import com.abernathyclinic.history.exception.PatientNotFoundException;
import com.abernathyclinic.history.model.Note;
import com.abernathyclinic.history.proxy.PatientProxy;
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

import java.time.LocalDate;
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
    @MockBean
    private PatientProxy patientProxy;

    private Note note;
    private Note otherNote;
    private Note samePatientNote;
    private List<Note> notes;
    private PatientBean patient;

    @BeforeEach
    void setUp() {
        note = new Note("NOTE001", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level");
        otherNote = new Note("NOTE002", 2, "Patient: TestBorderline Practitioner's notes/recommendations: Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late");
        samePatientNote = new Note("NOTE003", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling very strong'");
        notes = List.of(note, otherNote, samePatientNote);
        patient = new PatientBean(1, "TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");
    }

    @Test
    @DisplayName("Registering new note with valid information should save note to database")
    void createNote_shouldCreate_newNote() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(patient);
        when(noteRepository.insert(any(Note.class))).thenReturn(note);

        note = noteService.createNote(note);

        verify(noteRepository, times(1)).insert(any(Note.class));
        assertThat(note).isNotNull();
    }

    @Test
    @DisplayName("Adding a note to a patient who does not exist should throw an error")
    void createNote_shouldThrow_patientNotFoundException() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(null);
        assertThrows(PatientNotFoundException.class, () -> noteService.createNote(note));
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
    @DisplayName("getPatientHistory should return all notes with given patient ID")
    void getPatientHistory() {
        when(patientProxy.getPatientById(patient.getId())).thenReturn(patient);
        when(noteRepository.findAllByPatId(patient.getId())).thenReturn(List.of(note, samePatientNote));
        List<Note> result = noteService.getPatientHistory(patient.getId());
        assertTrue(result.contains(note));
        assertTrue(result.contains(samePatientNote));
    }

    @Test
    @DisplayName("getPatientHistory should throw IllegalArgumentException")
    void getPatientHistory_shouldThrow_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> noteService.getPatientHistory(null));
    }

    @Test
    @DisplayName("getPatientHistory should throw PatientNotFoundException")
    void getPatientHistory_shouldThrow_PatientNotFoundException() {
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(null);
        assertThrows(PatientNotFoundException.class, () -> noteService.getPatientHistory(patient.getId()));
    }

    @Test
    @DisplayName("Updating note which does not exist should throw NoteNotFoundException")
    void updateNote_whoDoesNotExist_shouldThrow_NoteNotFoundException() {
        when(noteRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NoteNotFoundException.class, () -> noteService.updateNote(note));
    }

    @Test
    @DisplayName("updateNote should throw PatientNotFoundException")
    void updateNote_shouldThrow_PatientNotFoundException() {
        when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note));
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(null);
        assertThrows(PatientNotFoundException.class, () -> noteService.updateNote(note));
    }

    @Test
    @DisplayName("Updating existing note should save changes to database")
    void updateNote_whichExists_shouldUpdate_existingNote() {
        when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note));
        when(patientProxy.getPatientById(any(Integer.class))).thenReturn(patient);
        String newContent = "this is a new content for the test";
        note.setContent(newContent);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        note = noteService.updateNote(note);

        verify(noteRepository, times(1)).findById(any(String.class));
        verify(patientProxy,times(1)).getPatientById(any(Integer.class));
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
        when(noteRepository.findById(any(String.class))).thenReturn(Optional.of(note));
        String idBeforeDeletion = note.getId();
        noteService.deleteNoteById(idBeforeDeletion);
        verify(noteRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    @DisplayName("Deleting note which does not exist should throw NoteNotFoundException")
    void deleteNote_whichDoesNotExist_shouldThrow_NoteNotFoundException() {
        when(noteRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNoteById(note.getId()));
    }
}