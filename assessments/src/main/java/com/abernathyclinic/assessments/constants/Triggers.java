package com.abernathyclinic.assessments.constants;

import java.util.List;

/**
 * Constant class for trigger and threshold management.
 * The risk assessment follows the following table:
 * +---+-------------+-------------+-------------+
 * |   | MALE < 30   | FEMALE < 30 |    >= 30    |
 * +---+-------------+-------------+-------------+
 * | 0 | NONE        | NONE        | NONE        |
 * | 1 |             |             |             |
 * | 2 |             |             | BORDERLINE  |
 * | 3 | IN DANGER   |             |             |
 * | 4 |             | IN DANGER   |             |
 * | 5 | EARLY ONSET |             |             |
 * | 6 |             |             | IN DANGER   |
 * | 7 |             | EARLY ONSET |             |
 * | 8 |             |             | EARLY ONSET |
 * +---+-------------+-------------+-------------+
 */
public class Triggers {
	public static final List<String> list = List.of(
			"Hémoglobine A1C",
			"Microalbumine",
			"Taille",
			"Poids",
			"Fumeur",
			"Anormal",
			"Cholestérol",
			"Vertige",
			"Rechute",
			"Réaction",
			"Anticorps"
	);

	public static final int BORDERLINE_OVER_AGE_LIMIT        = 2;
	public static final int IN_DANGER_FEMALE_TRIGGER_COUNT   = 4;
	public static final int IN_DANGER_MALE_TRIGGER_COUNT     = 3;
	public static final int IN_DANGER_OVER_AGE_LIMIT         = 6;
	public static final int EARLY_ONSET_FEMALE_TRIGGER_COUNT = 7;
	public static final int EARLY_ONSET_MALE_TRIGGER_COUNT   = 5;
	public static final int EARLY_ONSET_OVER_AGE_LIMIT       = 8;
}
