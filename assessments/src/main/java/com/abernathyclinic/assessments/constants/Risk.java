package com.abernathyclinic.assessments.constants;

public enum Risk {
	NONE {
		public String toString() {
			return "None";
		}
	},
	BORDERLINE {
		public String toString() {
			return "Borderline";
		}
	},
	IN_DANGER {
		public String toString() {
			return "In Danger";
		}
	},
	EARLY_ONSET {
		public String toString() {
			return "Early onset";
		}
	}
}
