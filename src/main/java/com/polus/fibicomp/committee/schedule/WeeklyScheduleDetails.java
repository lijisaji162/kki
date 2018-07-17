package com.polus.fibicomp.committee.schedule;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * This class holds weekly recurrence UI data.
 */
public class WeeklyScheduleDetails extends ScheduleDetails {

	private static final long serialVersionUID = 1L;

	private Integer week;

	private List<String> daysOfWeek;

	public WeeklyScheduleDetails() {
		super();
		this.setWeek(1);
		this.daysOfWeek = new ArrayList<String>(2);
		this.daysOfWeek.add(DayOfWeek.Monday.name());
		this.daysOfWeek.add("Hidden");
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getWeek() {
		return week;
	}

	public void setDaysOfWeek(List<String> daysOfWeek) {
		this.daysOfWeek = (null != daysOfWeek ? convertToWeekDays(daysOfWeek) : null);
	}

	public List<String> getDaysOfWeek() {
		return daysOfWeek;
	}

	private List<String> convertToWeekDays(List<String> daysOfWeek) {
		if (daysOfWeek.size() == 1 && daysOfWeek.get(0).equalsIgnoreCase("Hidden"))
			return null;
		List<String> tmp = new ArrayList<String>(daysOfWeek.size() - 1);
		if (CollectionUtils.isNotEmpty(daysOfWeek)) {
			for (String dayOfWeek : daysOfWeek) {
				if (dayOfWeek.equalsIgnoreCase("Hidden"))
					continue;
				tmp.add(dayOfWeek);
			}
		}
		return tmp;
	}
}
