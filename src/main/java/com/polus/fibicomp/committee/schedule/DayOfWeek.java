package com.polus.fibicomp.committee.schedule;

public enum DayOfWeek {

	Sunday("SUN"), Monday("MON"), Tuesday("TUE"), Wednesday("WED"), Thursday("THU"), Friday("FRI"), Saturday("SAT");

	private String abbr;

	DayOfWeek(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbr() {
		return abbr;
	}

}
