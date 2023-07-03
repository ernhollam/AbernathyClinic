package com.abernathyclinic.history.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientBean {
	private Integer   id;
	@Size(max = 50, message = "Maximum of {max} characters")
	@NotBlank(message = "Last name is mandatory")
	private String    family;
	@Size(max = 50, message = "Maximum of {max} characters")
	@NotBlank(message = "First name is mandatory")
	private String    given;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	@Size(max = 1, message = "Maximum of {max} character, select either Male or Female")
	@Pattern(regexp = "[MF]")
	private String    sex;
	@Size(max = 50, message = "Maximum of {max} characters")
	private String    address;
	@Pattern(regexp = "^([0-9]{3}-[0-9]{3}-[0-9]{4})?$", message = "Phone number must be in 123-456-7890 format")
	private String    phone;

	@Override
	public String toString() {
		return "{\n" +
				"id = " + id + ",\n "+
				"family = \"" + family + "\",\n "+
				"given =\"" + given + "\",\n "+
				"dob = \"" + dob + "\",\n "+
				"sex = \"" + (sex != null ? sex : "") + "\",\n "+
				"address = \"" + (address != null ? address : "") + "\",\n "+
				"phone = \"" + (phone != null ? phone : "") + "\"\n "+
				"}";
	}
}
