package com.abernathyclinic.clientui.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoteBean {
	private String   id;
	@NotNull(message = "You must provide a patient ID")
	Integer patId;
	@NotBlank(message = "Notes/recommendations can not be blank")
	String content;

	@Override
	public String toString() {
		return "{\n" +
				"id = " + id + ",\n "+
				"patId = \"" + patId + "\",\n "+
				"content =\"" + content + "\",\n "+"}";
	}
}
