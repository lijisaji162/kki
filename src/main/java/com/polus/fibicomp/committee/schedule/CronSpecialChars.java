package com.polus.fibicomp.committee.schedule;

/**
 * This class defines cron special character construct.
 */
public enum CronSpecialChars {

	SPACE(" "), STAR("*"), QUESTION("?"), COMMA(","), HASH("#"), HYPHEN("-"), LAST("L"), SLASH("/"), COMMASEPRATOR(
			","), SUN("SUN"), MON("MON"), TUE("TUE"), WED("WED"), THU("THU"), FRI("FRI"), SAT("SAT"), FIRST(
					"1"), SECOND("2"), THIRD("3"), FOURTH("4"), FIFTH("5"), JAN("JAN"), FEB("FEB"), MAR("MAR"), APR(
							"APR"), MAY("MAY"), JUN(
									"JUN"), JUL("JUL"), AUG("AUG"), SEP("SEP"), OCT("OCT"), NOV("NOV"), DEC("DEC");

	private String chr;

	/**
	 * Constructs a CronSpecialChars.java.
	 * 
	 * @param chr value is used to create cron expression.
	 */
	CronSpecialChars(String chr) {
		this.chr = chr;
	}

	public String toString() {
		return chr;
	}

}
