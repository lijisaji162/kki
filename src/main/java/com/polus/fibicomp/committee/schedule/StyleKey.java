package com.polus.fibicomp.committee.schedule;

public enum StyleKey {

	NEVER, DAILY, WEEKLY, MONTHLY, YEARLY;

	public boolean equalsString(String string) {
		return string.equalsIgnoreCase(this.toString());
	}
}
