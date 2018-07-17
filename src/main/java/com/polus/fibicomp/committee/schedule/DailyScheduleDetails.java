package com.polus.fibicomp.committee.schedule;

public class DailyScheduleDetails extends ScheduleDetails {

	private static final long serialVersionUID = 1L;

	private int defaultDay;

	private Integer day;

	private String dayOption;

	private String[] daysOfWeek;

	public static enum optionValues {
		XDAY, WEEKDAY
	};

	public DailyScheduleDetails() {
		super();
		this.setDefaultDay(1);
		this.setDay(this.getDefaultDay());
		this.setDayOption(optionValues.XDAY.toString());
		this.daysOfWeek = new String[5];
		this.getDaysOfWeek()[0] = DayOfWeek.Monday.name();
		this.getDaysOfWeek()[1] = DayOfWeek.Tuesday.name();
		this.getDaysOfWeek()[2] = DayOfWeek.Wednesday.name();
		this.getDaysOfWeek()[3] = DayOfWeek.Thursday.name();
		this.getDaysOfWeek()[4] = DayOfWeek.Friday.name();
	}

	public int getDefaultDay() {
		return defaultDay;
	}

	public void setDefaultDay(int defaultDay) {
		this.defaultDay = defaultDay;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getDayOption() {
		return dayOption;
	}

	public void setDayOption(String dayOption) {
		this.dayOption = dayOption;
	}

	public String[] getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(String[] daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

}
