package com.abernathyclinic.assessments.controller;

import com.abernathyclinic.assessments.bean.PatientBean;
import com.abernathyclinic.assessments.constants.Risk;
import com.abernathyclinic.assessments.proxy.HistoryProxy;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import com.abernathyclinic.assessments.service.PatientProfileService;
import com.abernathyclinic.assessments.service.RiskAssessmentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssessmentControllerTest {

	@Autowired MockMvc mockMvc;

	@MockBean RiskAssessmentService riskAssessmentService;
	@MockBean PatientProfileService patientProfileService;
	@MockBean PatientProxy          patientProxy;
	@MockBean HistoryProxy          historyProxy;

	private PatientBean testNone;
	private PatientBean testBorderline;
	private PatientBean testInDanger;
	private PatientBean testEarlyOnset;

	@BeforeAll
	void init() {
		testNone       = new PatientBean(1, "TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St",
				"00-222-3333");
		testBorderline = new PatientBean(2, "TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M", "2 High St",
				"200-333-444");
		testInDanger   = new PatientBean(3, "TestInDanger", "Test", LocalDate.of(2004, 6, 18), "M", "3Club Road",
				"300-444-5555");
		testEarlyOnset = new PatientBean(4, "TestEarlyOnset", "Test", LocalDate.of(2002, 6, 28), "F", "4 Valley Dr",
				"400-555-6666");
	}

	@Test
	@DisplayName("getAssessmentByPatientId with patient that does not exist")
	void getAssessmentByPatientId_throwsPatientNotFoundException() throws Exception {
		when(patientProxy.getPatientById(anyInt())).thenReturn(null);

		mockMvc.perform(get("/assess/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("getAssessmentByPatientId returns NONE")
	void getAssessmentByPatientId_returnsNONE() throws Exception {
		when(patientProxy.getPatientById(anyInt())).thenReturn(testNone);
		when(riskAssessmentService.assessPatientRiskById(anyInt())).thenReturn(Risk.NONE);

		mockMvc.perform(get("/assess/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Risk.NONE.getValue())));
	}

	@Test
	@DisplayName("getAssessmentByPatientId returns BORDERLINE")
	void getAssessmentByPatientId_returnsBORDERLINE() throws Exception {
		when(patientProxy.getPatientById(anyInt())).thenReturn(testBorderline);
		when(riskAssessmentService.assessPatientRiskById(anyInt())).thenReturn(Risk.BORDERLINE);

		mockMvc.perform(get("/assess/2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Risk.BORDERLINE.getValue())));
	}

	@Test
	@DisplayName("getAssessmentByPatientId returns IN_DANGER")
	void getAssessmentByPatientId_returnsIN_DANGER() throws Exception {
		when(patientProxy.getPatientById(anyInt())).thenReturn(testInDanger);
		when(riskAssessmentService.assessPatientRiskById(anyInt())).thenReturn(Risk.IN_DANGER);

		mockMvc.perform(get("/assess/3"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Risk.IN_DANGER.getValue())));
	}

	@Test
	@DisplayName("getAssessmentByPatientId returns EARLY_ONSET")
	void getAssessmentByPatientId_returnsEARLY_ONSET() throws Exception {
		when(patientProxy.getPatientById(anyInt())).thenReturn(testEarlyOnset);
		when(riskAssessmentService.assessPatientRiskById(anyInt())).thenReturn(Risk.EARLY_ONSET);

		mockMvc.perform(get("/assess/4"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(Risk.EARLY_ONSET.getValue())));
	}

	@Test
	@DisplayName("getAssessmentByPatientFamilyName returns NONE")
	void getAssessmentByPatientFamilyName_returnsNONE() throws Exception {
		Map<Integer, Risk> expected = new HashMap<>(1);
		expected.put(testNone.getId(), Risk.NONE);
		when(patientProxy.getPatientByFamilyName(anyString())).thenReturn(List.of(testNone));
		when(riskAssessmentService.assessPatientRiskByFamilyName(anyString())).thenReturn(expected);

		mockMvc.perform(get("/assess/familyName")
						.param("familyName", "TestNone"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasEntry(String.valueOf(testNone.getId()), String.valueOf(Risk.NONE))));
	}

	@Test
	@DisplayName("getAssessmentByPatientFamilyName returns BORDERLINE")
	void getAssessmentByPatientFamilyName_returnsBORDERLINE() throws Exception {
		Map<Integer, Risk> expected = new HashMap<>(1);
		expected.put(testBorderline.getId(), Risk.BORDERLINE);
		when(patientProxy.getPatientByFamilyName(anyString())).thenReturn(List.of(testBorderline));
		when(riskAssessmentService.assessPatientRiskByFamilyName(anyString())).thenReturn(expected);

		mockMvc.perform(get("/assess/familyName")
						.param("familyName", "TestBorderline"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$",
						hasEntry(String.valueOf(testBorderline.getId()), String.valueOf(Risk.BORDERLINE))));
	}

	@Test
	@DisplayName("getAssessmentByPatientFamilyName returns IN_DANGER")
	void getAssessmentByPatientFamilyName_returnsIN_DANGER() throws Exception {
		Map<Integer, Risk> expected = new HashMap<>(1);
		expected.put(testInDanger.getId(), Risk.IN_DANGER);
		when(patientProxy.getPatientByFamilyName(anyString())).thenReturn(List.of(testInDanger));
		when(riskAssessmentService.assessPatientRiskByFamilyName(anyString())).thenReturn(expected);

		mockMvc.perform(get("/assess/familyName")
						.param("familyName", "TestInDanger"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(
						jsonPath("$", hasEntry(String.valueOf(testInDanger.getId()), String.valueOf(Risk.IN_DANGER))));
	}

	@Test
	@DisplayName("getAssessmentByPatientFamilyName returns EARLY_ONSET")
	void getAssessmentByPatientFamilyName_returnsEARLY_ONSET() throws Exception {
		Map<Integer, Risk> expected = new HashMap<>(1);
		expected.put(testEarlyOnset.getId(), Risk.EARLY_ONSET);
		when(patientProxy.getPatientByFamilyName(anyString())).thenReturn(List.of(testEarlyOnset));
		when(riskAssessmentService.assessPatientRiskByFamilyName(anyString())).thenReturn(expected);

		mockMvc.perform(get("/assess/familyName")
						.param("familyName", "TestEarlyOnset"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$",
						hasEntry(String.valueOf(testEarlyOnset.getId()), String.valueOf(Risk.EARLY_ONSET))));
	}
}