package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.bean.NoteBean;
import com.abernathyclinic.assessments.bean.PatientBean;
import com.abernathyclinic.assessments.constants.Risk;
import com.abernathyclinic.assessments.proxy.HistoryProxy;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RiskAssessmentServiceTest {
	@Autowired
	RiskAssessmentService riskAssessmentService;
	// set now() to the 1st August 2023
	public final static LocalDate             LOCAL_DATE_NOW = LocalDate.of(2023, 8, 1);
	@MockBean
	private             Clock                 clock;
	@MockBean
	private             PatientProxy          patientProxy;
	@MockBean
	private             HistoryProxy          historyProxy;
	@MockBean
	private             PatientProfileService patientProfileService;

	private PatientBean testNone;
	private PatientBean testBorderline;
	private PatientBean testInDanger;
	private PatientBean testEarlyOnset;

	private final List<NoteBean> testNonesNotes       = new ArrayList<>();
	private final List<NoteBean> testBorderlinesNotes = new ArrayList<>();
	private final List<NoteBean> testInDangersNotes   = new ArrayList<>();
	private final List<NoteBean> testEarlyOnsetsNotes = new ArrayList<>();

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

		testNonesNotes.add(new NoteBean(1,
				"Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"));

		testBorderlinesNotes.addAll(List.of(
				new NoteBean(2, "\n" +
						"Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement"),
				new NoteBean(2,
						"Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale")
		));

		testInDangersNotes.addAll(List.of(
				new NoteBean(3, "Le patient déclare qu'il fume depuis peu"),
				new NoteBean(3,
						"Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé")
		));
		testEarlyOnsetsNotes.addAll(List.of(
				new NoteBean(4,
						"Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"),
				new NoteBean(4, "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"),
				new NoteBean(4,
						"Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"),
				new NoteBean(4, "Taille, Poids, Cholestérol, Vertige et Réaction")
		));

	}


	@Test
	void assessPatientRiskTest() {
		when(patientProxy.getPatientById(testNone.getId())).thenReturn(testNone);
		when(patientProxy.getPatientById(testBorderline.getId())).thenReturn(testBorderline);
		when(patientProxy.getPatientById(testInDanger.getId())).thenReturn(testInDanger);
		when(patientProxy.getPatientById(testEarlyOnset.getId())).thenReturn(testEarlyOnset);

		when(historyProxy.getPatientHistory(testNone.getId())).thenReturn(testNonesNotes);
		when(historyProxy.getPatientHistory(testBorderline.getId())).thenReturn(testBorderlinesNotes);
		when(historyProxy.getPatientHistory(testInDanger.getId())).thenReturn(testInDangersNotes);
		when(historyProxy.getPatientHistory(testEarlyOnset.getId())).thenReturn(testEarlyOnsetsNotes);

		when(patientProfileService.getAge(testNone.getDob())).thenReturn(57);
		when(patientProfileService.getAge(testBorderline.getDob())).thenReturn(78);
		when(patientProfileService.getAge(testInDanger.getDob())).thenReturn(19);
		when(patientProfileService.getAge(testEarlyOnset.getDob())).thenReturn(21);

		when(patientProfileService.isFemale(testNone.getSex())).thenReturn(true);
		when(patientProfileService.isFemale(testBorderline.getSex())).thenReturn(false);
		when(patientProfileService.isFemale(testInDanger.getSex())).thenReturn(false);
		when(patientProfileService.isFemale(testEarlyOnset.getSex())).thenReturn(true);

		when(patientProfileService.isMale(testNone.getSex())).thenReturn(false);
		when(patientProfileService.isMale(testBorderline.getSex())).thenReturn(true);
		when(patientProfileService.isMale(testInDanger.getSex())).thenReturn(true);
		when(patientProfileService.isMale(testEarlyOnset.getSex())).thenReturn(false);

		when(patientProfileService.isOverAgeLimit(19)).thenReturn(false);
		when(patientProfileService.isOverAgeLimit(21)).thenReturn(false);
		when(patientProfileService.isOverAgeLimit(57)).thenReturn(true);
		when(patientProfileService.isOverAgeLimit(78)).thenReturn(true);

		assertEquals(Risk.NONE, riskAssessmentService.assessPatientRisk(testNone.getId()));
		assertEquals(Risk.BORDERLINE, riskAssessmentService.assessPatientRisk(testBorderline.getId()));
		assertEquals(Risk.IN_DANGER, riskAssessmentService.assessPatientRisk(testInDanger.getId()));
		assertEquals(Risk.EARLY_ONSET, riskAssessmentService.assessPatientRisk(testEarlyOnset.getId()));
	}

	@Test
	void countTriggersTest() {
		assertEquals(1, riskAssessmentService.countTriggers(testNonesNotes));
		assertEquals(2, riskAssessmentService.countTriggers(testBorderlinesNotes));
		assertEquals(3, riskAssessmentService.countTriggers(testInDangersNotes));
		assertEquals(7, riskAssessmentService.countTriggers(testEarlyOnsetsNotes));
	}
}