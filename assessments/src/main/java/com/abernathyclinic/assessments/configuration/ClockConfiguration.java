package com.abernathyclinic.assessments.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {
	@Bean
	public Clock clock() {
		// create a Clock bean to have system's default zone for LocalDate.now()
		return Clock.systemDefaultZone();
	}
}
