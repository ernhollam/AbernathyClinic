package com.abernathyclinic.patients.controller;


import com.abernathyclinic.patients.exception.AlreadyExistsException;
import com.abernathyclinic.patients.exception.InvalidFormException;
import com.abernathyclinic.patients.exception.PatientNotFoundException;
import com.abernathyclinic.patients.model.Patient;
import com.abernathyclinic.patients.service.PatientService;
import com.abernathyclinic.patients.util.InvalidFormMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
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
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    PatientService patientService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient(@Valid @RequestBody Patient patient, Errors errors) throws AlreadyExistsException, InvalidFormException {
        if (errors.hasErrors()) {
            InvalidFormMessageBuilder.buildErrorMessage(errors);
        }

        return patientService.createPatient(patient);
    }

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable String id) {
        return patientService.getPatientById(Integer.valueOf(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided ID does not exist."));
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable String id, @Valid @RequestBody Patient patient, Errors errors) throws PatientNotFoundException, InvalidFormException {
        if (patientService.getPatientById(Integer.valueOf(id)).isEmpty()) {
            throw new PatientNotFoundException("Patient with the provided ID does not exist.");
        }
        if (errors.hasErrors()) {
            InvalidFormMessageBuilder.buildErrorMessage(errors);
        }
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable String id) throws PatientNotFoundException {
        Assert.notNull(id, "ID must not be empty");
        if (patientService.getPatientById(Integer.valueOf(id)).isEmpty()) {
            throw new PatientNotFoundException("Patient with the provided ID does not exist.");
        }
        patientService.deletePatient(patientService.getPatientById(Integer.valueOf(id)).get());
    }

}

