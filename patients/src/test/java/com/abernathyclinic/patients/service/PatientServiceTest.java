package com.abernathyclinic.patients.service;

import com.abernathyclinic.patients.exception.AlreadyExistsException;
import com.abernathyclinic.patients.exception.PatientNotFoundException;
import com.abernathyclinic.patients.model.Patient;
import com.abernathyclinic.patients.repository.PatientRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(PatientService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientServiceTest {
    /**
     * Class under test
     */
    @Autowired
    PatientService patientService;

    @MockBean
    PatientRepository patientRepository;

    private Patient testNone;
    private Patient testBorderline;

    @BeforeEach
    void setUp() {
        testNone = new Patient(1, "TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");
        testBorderline = new Patient(2, "TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444");
    }

    @Test
    @DisplayName("Registering new patient with valid information should save patient to database")
    void createPatient_whoDoesNotAlreadyExist_shouldCreate_newPatient() {
        when(patientRepository.existsById(any(Integer.class))).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(testNone);

        testNone = patientService.createPatient(testNone);

        verify(patientRepository, times(1)).save(any(Patient.class));
        assertThat(testNone).isNotNull();
    }

    @Test
    @DisplayName("Registering a patient who already exists should throw AlreadyExistException")
    void createPatient_whoAlreadyExists_shouldThrow_AlreadyExistException() {
        when(patientRepository.findByFamilyAndGivenAndDob(any(String.class), any(String.class), any(LocalDate.class))).thenReturn(Optional.of(testNone));
        assertThrows(AlreadyExistsException.class, () -> patientService.createPatient(testNone));

    }

    @Test
    @DisplayName("getPatients() should return list of existing patients")
    void getPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(testNone, testBorderline));

        List<Patient> result = patientService.getPatients();

        assertTrue(result.contains(testNone));
        assertTrue(result.contains(testBorderline));
    }

    @Test
    @DisplayName("Id should not be null when calling getPatientById")
    void getPatientById_whenIDIsNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> patientService.getPatientById(null));
    }

    @Test
    @DisplayName("getPatientById() should return Optional of existing patient")
    void getPatientById() {
        when(patientRepository.findById(testNone.getId())).thenReturn(Optional.ofNullable(testNone));
        Optional<Patient> result = patientService.getPatientById(testNone.getId());
        assertTrue(result.isPresent());
        assertEquals(testNone.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Updating patient who does not exist should throw PatientNotFoundException")
    void updatePatient_whoDoesNotExist_shouldThrow_PatientNotFoundException() {
        when(patientRepository.existsById(any(Integer.class))).thenReturn(false);
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(testNone));
    }

    @Test
    @DisplayName("Updating existing patient should save changes to database")
    void updatePatient_whoExists_shouldUpdate_existingPatient() {
        when(patientRepository.existsById(any(Integer.class))).thenReturn(true);
        String expectedFamilyName = "New family name";
        testNone.setFamily(expectedFamilyName);
        when(patientRepository.save(any(Patient.class))).thenReturn(testNone);

        testNone = patientService.updatePatient(testNone);

        verify(patientRepository, times(1)).save(any(Patient.class));
        assertThat(testNone).isNotNull();
        assertEquals(expectedFamilyName, testNone.getFamily());
    }

    @Test
    @DisplayName("Patient should not be null when calling deletePatient()")
    void deletePatient_whenPatientIsNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> patientService.deletePatient(null));
    }
    
    @Test
    @DisplayName("Deleting existing patient should delete them from database")
    void deletePatient() {
        when(patientRepository.existsById(any(Integer.class))).thenReturn(true);
        Integer idBeforeDeletion = testNone.getId();
        patientService.deletePatient(testNone);
        assertTrue(patientService.getPatientById(idBeforeDeletion).isEmpty());
    }

    @Test
    @DisplayName("Family name should not be null when calling getPatientByFamilyName")
    void getPatientByFamilyName_whenIDIsNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> patientService.getPatientByFamilyName(null));
    }

    @Test
    @DisplayName("getPatientByFamilyName() should return one patient")
    void getPatientByFamilyName_shouldReturnListOfOnePatient() {
        when(patientRepository.findByFamily(testNone.getFamily())).thenReturn(List.of(testNone));
        List<Patient> result = patientService.getPatientByFamilyName(testNone.getFamily());
        assertFalse(result.isEmpty());
        assertEquals(testNone.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("getPatientByFamilyName() should return list of two patients with same name")
    void getPatientByFamilyName_shouldReturnListOfTwoPatients() {
        testBorderline.setFamily("TestNone");
        List<Patient> expected = List.of(testNone, testBorderline);
        when(patientRepository.findByFamily(testNone.getFamily())).thenReturn(expected);
        List<Patient> result = patientService.getPatientByFamilyName(testNone.getFamily());
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(expected));
    }
}