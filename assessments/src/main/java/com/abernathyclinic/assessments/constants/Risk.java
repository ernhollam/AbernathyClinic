package com.abernathyclinic.assessments.constants;

public enum Risk {
	NONE("None"),
	BORDERLINE("Borderline"),
	IN_DANGER("In Danger"),
	EARLY_ONSET("Early onset");

	private final String value;

	public String getValue() {
		return value;
	}

	Risk(String value) {
		this.value = value;
	}
}
