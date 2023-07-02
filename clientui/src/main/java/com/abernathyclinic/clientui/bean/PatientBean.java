package com.abernathyclinic.clientui.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientBean {
	private Integer   id;
	private String    family;
	private String    given;
	private LocalDate dob;
	private String    sex;
	private String    address;
	private String    phone;
}
