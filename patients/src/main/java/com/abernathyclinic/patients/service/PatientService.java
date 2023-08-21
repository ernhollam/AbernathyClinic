package com.abernathyclinic.patients.service;

import com.abernathyclinic.patients.exception.AlreadyExistsException;
import com.abernathyclinic.patients.exception.PatientNotFoundException;
import com.abernathyclinic.patients.model.Patient;
import com.abernathyclinic.patients.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Saves new patient to database
     *
     * @param patient Patient to save
     * @return Patient with ID if no error
     * @throws AlreadyExistsException Exception thrown when the patient to be created already exists in database
     */
    @Transactional
    public Patient createPatient(Patient patient) {
        if (patientRepository.findByFamilyAndGivenAndDob(patient.getFamily(), patient.getGiven(), patient.getDob()).isPresent()) {
            String alreadyExistsErrorMessage = "Patient " + patient.getFamily() + " " + patient.getGiven() + ", born " + patient.getDob() + " already exists.";
            log.error(alreadyExistsErrorMessage);
            throw new AlreadyExistsException(alreadyExistsErrorMessage);
        }
        log.debug("Saving new patient " + toString(patient));
        return patientRepository.save(patient);
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    /**
     * Finds a patient thanks to their ID.
     *
     * @param id Patient ID.
     * @return found patient or empty optional.
     */
    public Optional<Patient> getPatientById(Integer id) {
        Assert.notNull(id, "Patient ID must not be empty. Please provide an ID");
        return patientRepository.findById(id);
    }

    /**
     * Finds a patient given a family name
     *
     * @param family Patient's family name.
     * @return found patient or empty optional.
     */
    public List<Patient> getPatientByFamilyName(String family) {
        Assert.notNull(family, "Patient family name must not be empty. Please provide a name.");
        return patientRepository.findByFamily(family);
    }


    /**
     * Updates a patient.
     *
     * @param patient Patient to be updated
     * @return updated patient
     */
    @Transactional
    public Patient updatePatient(Patient patient) {
        Assert.notNull(patient, "Please provide a Patient to update");
        String patientString = toString(patient);
        if (patientRepository.existsById(patient.getId())) {
            log.debug("Updating patient " + patientString);
            return patientRepository.save(patient);
        } else {
            String patientNotFoundErrorMessage = "Patient " + patientString + " does not exist.";
            log.error(patientNotFoundErrorMessage);
            throw new PatientNotFoundException(patientNotFoundErrorMessage);
        }
    }

    /**
     * Deletes a patient if exists. Throws PatientNotFoundException if the provided patient does not exist in database.
     *
     * @param patient Patient to delete
     */
    @Transactional
    public void deletePatient(Patient patient) {
        Assert.notNull(patient, "Patient must be provided.");
        String patientString = toString(patient);
        if (patientRepository.existsById(patient.getId())) {
            patientRepository.delete(patient);
            log.debug("Deleted patient " + patientString);
        } else {
            String deleteErrorMessage = "Patient " + patientString + " does not exist.";
            log.error(deleteErrorMessage);
            throw new PatientNotFoundException(deleteErrorMessage);
        }
    }

    /**
     * Returns String containing patient's family and given name and birthday.
     * @param patient Patient to retrieve information for
     * @return string with family and given name + birthday
     */
    public String toString(Patient patient) {
        return patient.getFamily() + " " + patient.getGiven() + ", born the " + patient.getDob();
    }
}
