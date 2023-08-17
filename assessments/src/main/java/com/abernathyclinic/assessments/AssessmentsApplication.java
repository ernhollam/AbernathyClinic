package com.abernathyclinic.assessments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.abernathyclinic.assessments")
public class AssessmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentsApplication.class, args);
	}

}
