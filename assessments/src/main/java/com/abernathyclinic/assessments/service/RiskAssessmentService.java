package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.bean.NoteBean;
import com.abernathyclinic.assessments.bean.PatientBean;
import com.abernathyclinic.assessments.constants.Risk;
import com.abernathyclinic.assessments.constants.Triggers;
import com.abernathyclinic.assessments.proxy.HistoryProxy;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.abernathyclinic.assessments.constants.Risk.BORDERLINE;
import static com.abernathyclinic.assessments.constants.Risk.EARLY_ONSET;
import static com.abernathyclinic.assessments.constants.Risk.IN_DANGER;
import static com.abernathyclinic.assessments.constants.Risk.NONE;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.BORDERLINE_OVER_AGE_LIMIT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.EARLY_ONSET_FEMALE_TRIGGER_COUNT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.EARLY_ONSET_MALE_TRIGGER_COUNT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.EARLY_ONSET_OVER_AGE_LIMIT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.IN_DANGER_FEMALE_TRIGGER_COUNT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.IN_DANGER_MALE_TRIGGER_COUNT;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.IN_DANGER_OVER_AGE_LIMIT;

@Service
@Slf4j
public class RiskAssessmentService {
	@Autowired
	private PatientProxy patientProxy;
	@Autowired
	private HistoryProxy historyProxy;

	@Autowired
	private PatientProfileService patientProfileService;

	/**
	 * Evaluates risk to develop diabetes according to gender, age and number of triggers found.
	 *
	 * @param patientId ID of patient for which the risk assessment is done
	 * @return risk level to develop diabetes
	 */
	public Risk assessPatientRisk(Integer patientId) {
		PatientBean patient = patientProxy.getPatientById(patientId);
		// get patient traits
		int     age        = patientProfileService.getAge(patient.getDob());
		long    nbTriggers = countTriggers(historyProxy.getPatientHistory(patient.getId()));
		String  sex        = patient.getSex();
		boolean isFemale   = patientProfileService.isFemale(sex);
		boolean isMale     = patientProfileService.isMale(sex);

		if (nbTriggers == 0) {
			return NONE;
		}

		if (patientProfileService.isOverAgeLimit(age)) {
			if (nbTriggers >= EARLY_ONSET_OVER_AGE_LIMIT) return EARLY_ONSET;
			if (nbTriggers >= IN_DANGER_OVER_AGE_LIMIT) return IN_DANGER;
			if (nbTriggers >= BORDERLINE_OVER_AGE_LIMIT) return BORDERLINE;
		} else {
			if (isFemale) {
				if (nbTriggers >= EARLY_ONSET_FEMALE_TRIGGER_COUNT) return EARLY_ONSET;
				if (nbTriggers >= IN_DANGER_FEMALE_TRIGGER_COUNT) return IN_DANGER;
			} else if (isMale) {
				if (nbTriggers >= EARLY_ONSET_MALE_TRIGGER_COUNT) return EARLY_ONSET;
				if (nbTriggers >= IN_DANGER_MALE_TRIGGER_COUNT) return IN_DANGER;
			}
		}
		return NONE;
	}

	/**
	 * Counts how many times triggers appear in patient history.
	 *
	 * @param notes List of notes, patient history
	 * @return trigger count
	 */
	public long countTriggers(List<NoteBean> notes) {
		// concatenate all notes to one single String
		String concatenatedNotes = notes.stream()
				.map(NoteBean::getContent)
				.map(String::trim)
				.map(String::toLowerCase)
				.collect(Collectors.joining());

		// check how many times any trigger appears in the concatenated String
		return Triggers.list.stream()
				.map(String::toLowerCase)
				.filter(concatenatedNotes::contains)
				.count();
	}

}
