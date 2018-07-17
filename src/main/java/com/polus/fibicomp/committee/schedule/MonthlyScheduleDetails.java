package com.polus.fibicomp.committee.schedule;

/**
 * This class holds monthly recurrence UI data.
 */
public class MonthlyScheduleDetails extends ScheduleDetails {

	private static final long serialVersionUID = 1L;

	private Integer day;

	private Integer option1Month;

	private Integer option2Month;

	private String monthOption;

	public static enum optionValues {
		XDAYANDXMONTH, XDAYOFWEEKANDXMONTH
	};

	private String selectedMonthsWeek;

	private String selectedDayOfWeek;

	public MonthlyScheduleDetails() {
		super();
		this.monthOption = optionValues.XDAYANDXMONTH.toString();
		this.setDay(6);
		this.setOption1Month(1);
		this.setOption2Month(1);

		this.setSelectedMonthsWeek(WeekOfMonth.first.toString());

		this.setSelectedDayOfWeek(DayOfWeek.Monday.toString());
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getDay() {
		return day;
	}

	public void setOption1Month(Integer option1Month) {
		this.option1Month = option1Month;
	}

	public Integer getOption1Month() {
		return option1Month;
	}

	public void setOption2Month(Integer option2Month) {
		this.option2Month = option2Month;
	}

	public Integer getOption2Month() {
		return option2Month;
	}

	public void setMonthOption(String monthOption) {
		this.monthOption = monthOption;
	}

	public String getMonthOption() {
		return monthOption;
	}

	public void setSelectedMonthsWeek(String selectedMonthsWeek) {
		this.selectedMonthsWeek = selectedMonthsWeek;
	}

	public String getSelectedMonthsWeek() {
		return selectedMonthsWeek;
	}

	public void setSelectedDayOfWeek(String selectedDayOfWeek) {
		this.selectedDayOfWeek = selectedDayOfWeek;
	}

	public String getSelectedDayOfWeek() {
		return selectedDayOfWeek;
	}
}
