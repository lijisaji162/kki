package com.polus.fibicomp.committee.schedule;

public enum WeekOfMonth {

	first("FIRST"), second("SECOND"), third("THIRD"), fourth("FOURTH"), last("LAST");

	private String number;

	WeekOfMonth(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

}
