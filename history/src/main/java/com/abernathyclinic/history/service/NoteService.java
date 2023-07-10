package com.abernathyclinic.history.service;

import com.abernathyclinic.history.exception.NoteNotFoundException;
import com.abernathyclinic.history.exception.PatientNotFoundException;
import com.abernathyclinic.history.model.Note;
import com.abernathyclinic.history.proxy.PatientProxy;
import com.abernathyclinic.history.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    PatientProxy patientProxy;

    @Autowired
    private NoteRepository noteRepository;

    public NoteService(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    private final String genericNotEmptyIDMessage = "The provided ID should not be empty";

    public Note createNote(Note note) {
        if (patientExists(note.getPatId())) {
            return noteRepository.insert(note);
        }
        throw new PatientNotFoundException("Patient with the provided ID " + note.getPatId() + " was not found");
    }

    public List<Note> getPatientHistory(Integer patientId) {
        Assert.notNull(patientId, genericNotEmptyIDMessage);
        if (patientExists(patientId)) {
            return noteRepository.findAllByPatId(patientId);
        }
        throw new PatientNotFoundException("Patient with the provided ID " + patientId + " was not found");
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(String noteId) {
        Assert.notNull(noteId, genericNotEmptyIDMessage);
        return noteRepository.findById(noteId);
    }

    public Note updateNote(Note note) {
        if (noteRepository.findById(note.getId()).isEmpty()) {
            throw new NoteNotFoundException("Note with the given ID was not found.");
        }
        if (patientExists(note.getPatId())) {
            return noteRepository.save(note);
        }
        throw new PatientNotFoundException("Patient with the given ID was not found.");
    }

    public void deleteNoteById(String noteId) {
        Assert.notNull(noteId, genericNotEmptyIDMessage);
        if (noteRepository.findById(noteId).isEmpty()) {
            throw new NoteNotFoundException("Note with the given ID was not found.");
        }
        noteRepository.deleteById(noteId);
    }

    private boolean patientExists(Integer patientId) {
        return patientProxy.getPatientById(patientId) != null;
    }
}
