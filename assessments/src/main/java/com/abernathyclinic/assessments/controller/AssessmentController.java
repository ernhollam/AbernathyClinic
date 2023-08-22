package com.abernathyclinic.assessments.controller;

import com.abernathyclinic.assessments.constants.Risk;
import com.abernathyclinic.assessments.exception.PatientNotFoundException;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import com.abernathyclinic.assessments.service.RiskAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/assess")
@Slf4j
public class AssessmentController {
	@Autowired PatientProxy patientProxy;

	@Autowired
	RiskAssessmentService riskAssessmentService;

	@Operation(summary = "Get patient diabetes risk assessment by their id")
	@GetMapping("/{patientId}")
	public ResponseEntity<String> getAssessmentByPatientId(
			@Parameter(description = "id of patient to be assessed") @PathVariable Integer patientId)
			throws PatientNotFoundException {
		if (patientProxy.getPatientById(patientId) == null) {
			log.error("Patient with the provided ID " + patientId + " was not found");
			throw new PatientNotFoundException("Patient with the provided ID " + patientId + " was not found");
		}
		return new ResponseEntity<>(riskAssessmentService.assessPatientRiskById(patientId).getValue(), HttpStatus.OK);
	}

	@Operation(summary = "Get patient diabetes risk assessment by family name")
	@GetMapping("/familyName")
	public ResponseEntity<Map<Integer, Risk>> getAssessmentByPatientFamilyName(
			@Parameter(description = "Last name of patient to be assessed") @RequestParam String familyName)
			throws PatientNotFoundException {
		if (patientProxy.getPatientByFamilyName(familyName).isEmpty()) {
			log.error("There is no patient with the following name: " + familyName + ".");
			throw new PatientNotFoundException("There is no patient with the following name: " + familyName + ".");
		}
		return new ResponseEntity<>(riskAssessmentService.assessPatientRiskByFamilyName(familyName), HttpStatus.OK);
	}

}
