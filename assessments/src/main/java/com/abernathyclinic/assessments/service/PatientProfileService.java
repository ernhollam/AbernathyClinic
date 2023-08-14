package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.constants.RiskAssessmentConstants;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
@Slf4j
public class PatientProfileService {
	@Autowired
	private Clock        clock;
	@Autowired
	private PatientProxy patientProxy;

	public int getAge(LocalDate birthday) {
		LocalDate now = LocalDate.now(clock);
		if (birthday.isAfter(now)) {
			log.error("Birthday can not be in the future.");
			throw new RuntimeException("Birthday can not be in the future.");
		}
		return now.getYear() - birthday.getYear();
	}

	public boolean isOverAgeLimit(int age) {
		return age >= RiskAssessmentConstants.AGE_LIMIT;
	}

	public boolean isFemale(String sex) {
		return sex.equalsIgnoreCase("F");
	}

	public boolean isMale(String sex) {
		return sex.equalsIgnoreCase("M");
	}

}
