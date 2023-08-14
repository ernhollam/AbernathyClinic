package com.abernathyclinic.assessments.service;

import com.abernathyclinic.assessments.proxy.HistoryProxy;
import com.abernathyclinic.assessments.proxy.PatientProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(PatientProfileService.class)
class PatientProfileServiceTest {
	// set now() to the 1st August 2023
	public final static LocalDate             LOCAL_DATE_NOW = LocalDate.of(2023, 8, 1);
	@MockBean
	private             Clock                 clock;
	@MockBean
	private             PatientProxy          patientProxy;
	@MockBean
	private             HistoryProxy          historyProxy;
	@Autowired
	private             PatientProfileService patientProfileService;

	@BeforeEach
	void setUp() {
		// configure a fixed clock to have fixed LocalDate.now()
		Clock fixedClock = Clock.fixed(LOCAL_DATE_NOW.atStartOfDay(ZoneId.systemDefault()).toInstant(),
				ZoneId.systemDefault());
		when(clock.instant()).thenReturn(fixedClock.instant());
		when(clock.getZone()).thenReturn(fixedClock.getZone());
	}

	@Test
	void getAge_shouldReturn_rightAge() {
		LocalDate birthday = LocalDate.of(1968, 6, 22);
		int       age      = patientProfileService.getAge(birthday);
		assertThat(age).isEqualTo(55);
	}

	@Test
	void getAge_shouldThrow_RuntimeException_whenBirthdayIsInTheFuture() {
		LocalDate birthday = LocalDate.of(2025, 12, 25);
		assertThrows(RuntimeException.class, () -> patientProfileService.getAge(birthday));
	}

	@Test
	void isOverAgeLimit_returnsTrue_whenAge_isOVerAgeLimit() {
		assertTrue(patientProfileService.isOverAgeLimit(55));
	}

	@Test
	void isOverAgeLimit_returnsFalse_whenAge_isOVerAgeLimit() {
		assertFalse(patientProfileService.isOverAgeLimit(23));
	}

	@Test
	void isFemale_returnsTrue() {
		assertTrue(patientProfileService.isFemale("F"));
	}

	@Test
	void isFemale_returnsFalse_whenEmptyString() {
		assertFalse(patientProfileService.isFemale(""));
	}

	@Test
	void isFemale_returnsFalse_whenAnyString() {
		assertFalse(patientProfileService.isFemale("M"));
	}

	@Test
	void isMale_returnsTrue() {
		assertTrue(patientProfileService.isMale("M"));
	}

	@Test
	void isMale_returnsFalse_whenEmptyString() {
		assertFalse(patientProfileService.isMale(""));
	}

	@Test
	void isMale_returnsFalse_whenAnyString() {
		assertFalse(patientProfileService.isMale("F"));
	}
}