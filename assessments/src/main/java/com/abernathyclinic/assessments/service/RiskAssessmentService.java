package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.bean.NoteBean;
import com.abernathyclinic.assessments.bean.PatientBean;
import com.abernathyclinic.assessments.constants.Risk;
import com.abernathyclinic.assessments.constants.Triggers;
import com.abernathyclinic.assessments.exception.PatientNotFoundException;
import com.abernathyclinic.assessments.proxy.HistoryProxy;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.abernathyclinic.assessments.constants.Risk.*;
import static com.abernathyclinic.assessments.constants.RiskAssessmentConstants.*;

@Service
@Slf4j
public class RiskAssessmentService {
	@Autowired
	private PatientProxy          patientProxy;
	@Autowired
	private HistoryProxy          historyProxy;
	@Autowired
	private PatientProfileService patientProfileService;

	/**
	 * Calculates risk given an ID.
	 *
	 * @param patientId ID of patient for which the risk assessment is done
	 * @return risk level to develop diabetes
	 */
	public Risk assessPatientRiskById(Integer patientId) {
		PatientBean patient = patientProxy.getPatientById(patientId);
		return getRisk(patient);
	}

	/**
	 * Calculates risk given a family name.
	 *
	 * @param familyName Last name of patient for which the risk assessment is done
	 * @return risk level to develop diabetes
	 */
	public Map<Integer, Risk> assessPatientRiskByFamilyName(String familyName) {
		List<PatientBean> patients = patientProxy.getPatientByFamilyName(familyName);
		if (patients.isEmpty()) {
			throw new PatientNotFoundException("There is no patient with the following name: " + familyName + ".");
		}
		HashMap<Integer, Risk> result = new HashMap<>(patients.size());
		patients.forEach(patient -> result.put(patient.getId(), getRisk(patient)));
		return result;
	}

	/**
	 * Evaluates risk to develop diabetes according to gender, age and number of triggers found.
	 *
	 * @param patient Patient for which the risk assessment is done
	 * @return risk level to develop diabetes
	 */
	private Risk getRisk(PatientBean patient) {
		if (patient == null) {
			log.error("Patient was not found");
			throw new PatientNotFoundException("Patient was not found");
		} else {
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
