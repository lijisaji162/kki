package com.polus.fibicomp.committee.schedule;

public enum Months {

	JANUARY("JAN"), FEBRUARY("FEB"), MARCH("MAR"), APRIL("APR"), MAY("MAY"), JUNE("JUN"), JULY("JUL"), AUGUST(
			"AUG"), SEPTEMBER("SEP"), OCTOBER("OCT"), NOVEMBER("NOV"), DECEMBER("DEC");

	private String abbr;

	Months(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbr() {
		return abbr;
	}

}
