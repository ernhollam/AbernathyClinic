package com.abernathyclinic.history.integration;

import com.abernathyclinic.history.model.Note;
import com.abernathyclinic.history.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class NoteRepositoryIT {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    private Note note;
    private Note otherNote;
    private Note samePatientNote;

    @BeforeEach
    void setUp() {
        note = new Note("NOTE001", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling terrific' Weight at or below recommended level");
        otherNote = new Note("NOTE002", 2, "Patient: TestBorderline Practitioner's notes/recommendations: Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late");
        samePatientNote = new Note("NOTE003", 1, "Patient: TestNone Practitioner's notes/recommendations: Patient states that they are 'feeling very strong'");
    }


    @Order(1)
    @Test
    void testDb() {
        assertThat(mongoTemplate.getDb()).isNotNull();
    }

    @Order(2)
    @Test
    void testSaveCreatesNotes() {
        noteRepository.save(note);
        noteRepository.save(otherNote);

        Note result = mongoTemplate.findById("NOTE001", Note.class);

        assertNotNull(result);
        assertEquals(note.getId(),result.getId());
        assertEquals(note.getPatId(), result.getPatId());
        assertEquals(note.getContent(), result.getContent());
    }
    @Order(3)
    @Test
    void testFindById() {
        Optional<Note> optionalNote = noteRepository.findById("NOTE001");
        assertTrue(optionalNote.isPresent());
        Note result = optionalNote.get();

        assertEquals(note.getId(), result.getId());
        assertEquals(note.getPatId(), result.getPatId());
        assertEquals(note.getContent(), result.getContent());
    }

    @Order(4)
    @Test
    void testGetPatientHistory() {
        noteRepository.save(note);
        noteRepository.save(otherNote);
        noteRepository.save(samePatientNote);
        List<Note> result = noteRepository.findAllByPatId(1);

        assertEquals(2, result.size());
        for (Note resultNote : result) {
            assertTrue(resultNote.getId().equals(note.getId()) || resultNote.getId().equals(samePatientNote.getId()));
        }
    }

    @Order(5)
    @Test
    void testDeleteById() {
        noteRepository.deleteById("NOTE001");
        assertFalse(noteRepository.existsById("NOTE001"));
    }
}