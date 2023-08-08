package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.constants.RiskAssessmentConstants;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class PatientProfileService {
	@Autowired
	private PatientProxy patientProxy;

	public int getAge(LocalDate birthday) {
		LocalDate now = LocalDate.now();
		if (birthday.isAfter(now)) {
			log.error("Birthday can not be after today.");
			throw new RuntimeException("Birthday can not be after today.");
		}
		return now.getYear() - birthday.getYear();
	}

	public boolean isOverAgeLimit(int age) {
		return age >= RiskAssessmentConstants.AGE_LIMIT;
	}

	public boolean isFemale(String sex) {
		return !sex.isBlank() && sex.equalsIgnoreCase("F");
	}

}
