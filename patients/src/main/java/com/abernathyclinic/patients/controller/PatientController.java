package com.abernathyclinic.patients.controller;

import com.abernathyclinic.patients.exception.AlreadyExistsException;
import com.abernathyclinic.patients.exception.InvalidFormException;
import com.abernathyclinic.patients.exception.PatientNotFoundException;
import com.abernathyclinic.patients.model.Patient;
import com.abernathyclinic.patients.service.PatientService;
import com.abernathyclinic.patients.util.InvalidFormMessageBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
	@Autowired
	PatientService patientService;

	@Operation(summary = "Creates new patient")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Patient createPatient(@Valid @RequestBody Patient patient, Errors errors)
			throws AlreadyExistsException, InvalidFormException {
		if (errors.hasErrors()) {
			InvalidFormMessageBuilder.buildErrorMessage(errors);
		}

		return patientService.createPatient(patient);
	}

	@Operation(summary = "Gets the list of patients")
	@GetMapping
	public List<Patient> getPatients() {
		return patientService.getPatients();
	}

	@Operation(summary = "Gets a patient by their id")
	@GetMapping("/{id}")
	public Patient getPatientById(@Parameter(description = "id of patient to be searched") @PathVariable Integer id) {
		return patientService.getPatientById(id)
				.orElseThrow(() -> new PatientNotFoundException("Patient with the provided ID does not exist."));
	}

	@Operation(summary = "Gets a patient by their family name")
	@GetMapping("/familyName")
	public List<Patient> getPatientByFamilyName(
			@Parameter(description = "Family name of patient to be searched") @RequestParam String family) {
		return patientService.getPatientByFamilyName(family);
	}

	@Operation(summary = "Updates a patient by their id")
	@PutMapping("/{id}")
	public Patient updatePatient(@Parameter(description = "id of patient to be searched") @PathVariable String id,
			@Valid @RequestBody Patient patient, Errors errors)
			throws PatientNotFoundException, InvalidFormException {
		if (patientService.getPatientById(Integer.valueOf(id)).isEmpty()) {
			throw new PatientNotFoundException("Patient with the provided ID does not exist.");
		}
		if (errors.hasErrors()) {
			InvalidFormMessageBuilder.buildErrorMessage(errors);
		}
		return patientService.updatePatient(patient);
	}

	@Operation(summary = "Removes a requested patient")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePatient(@Parameter(description = "id of patient to be deleted") @PathVariable String id)
			throws PatientNotFoundException {
		Assert.notNull(id, "ID must not be empty");
		if (patientService.getPatientById(Integer.valueOf(id)).isEmpty()) {
			throw new PatientNotFoundException("Patient with the provided ID does not exist.");
		}
		patientService.deletePatient(patientService.getPatientById(Integer.valueOf(id)).get());
	}

}

