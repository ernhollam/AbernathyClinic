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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
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
}