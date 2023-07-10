package com.abernathyclinic.history.controller;

import com.abernathyclinic.history.exception.InvalidFormException;
import com.abernathyclinic.history.exception.NoteNotFoundException;
import com.abernathyclinic.history.exception.PatientNotFoundException;
import com.abernathyclinic.history.model.Note;
import com.abernathyclinic.history.service.NoteService;
import com.abernathyclinic.history.util.InvalidFormMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNoteForPatient(@Valid @RequestBody Note note, Errors errors) throws PatientNotFoundException, InvalidFormException {
        validateForm(errors);
        return noteService.createNote(note);
    }

    @GetMapping("/patient/{patientId}")
    public List<Note> getPatientHistory(@PathVariable Integer patientId) throws PatientNotFoundException {
        return noteService.getPatientHistory(patientId);
    }

    @GetMapping("/{noteId}")
    public Note getNoteById(@PathVariable String noteId) throws NoteNotFoundException {
        return noteService.getNoteById(noteId).orElseThrow(() -> new NoteNotFoundException("Note with ID " + noteId + " was not found"));
    }

    @GetMapping
    public List<Note> getNotes(){
        return noteService.getNotes();
    }

    @PutMapping("/{id}")
    public Note updateNoteById(@PathVariable String id, @Valid @RequestBody Note note, Errors errors)
            throws NoteNotFoundException, PatientNotFoundException, InvalidFormException {
        validateForm(errors);
        return noteService.updateNote(note);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNoteById(@PathVariable String id) throws NoteNotFoundException {
        noteService.deleteNoteById(id);
    }

    private void validateForm(Errors errors) throws InvalidFormException {
        if (errors.hasErrors()) {
            InvalidFormMessageBuilder.buildErrorMessage(errors);
        }
    }
}
