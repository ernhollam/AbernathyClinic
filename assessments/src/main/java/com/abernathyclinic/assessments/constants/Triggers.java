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
			"Fumeuse",
			"Anormal",
			"Cholestérol",
			"Vertige",
			"Rechute",
			"Réaction",
			"Anticorps"
	);

}
