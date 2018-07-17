package com.polus.fibicomp.committee.schedule;

import com.polus.fibicomp.committee.schedule.Time12HrFmt.MERIDIEM;
import com.polus.fibicomp.constants.Constants;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ScheduleData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date scheduleStartDate;

	private Time12HrFmt time;

	private String place;

	private String recurrenceType;

	private DailyScheduleDetails dailySchedule;

	private WeeklyScheduleDetails weeklySchedule;

	private MonthlyScheduleDetails monthlySchedule;

	private YearlyScheduleDetails yearlySchedule;

	private Date filterStartDate;

	private Date filerEndDate;

	private List<Date> datesInConflict;

	public ScheduleData() {
		super();
		this.setScheduleStartDate(new Date(new java.util.Date().getTime()));

		this.setTime(new Time12HrFmt(Constants.DEFAULTTIME, MERIDIEM.PM));

		this.setRecurrenceType(StyleKey.NEVER.toString());

		this.setDailySchedule(new DailyScheduleDetails());
		this.setWeeklySchedule(new WeeklyScheduleDetails());
		this.setMonthlySchedule(new MonthlyScheduleDetails());
		this.setYearlySchedule(new YearlyScheduleDetails());
	}

	public Date getScheduleStartDate() {
		return scheduleStartDate;
	}

	public void setScheduleStartDate(Date scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public Time12HrFmt getTime() {
		return time;
	}

	public void setTime(Time12HrFmt time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	public DailyScheduleDetails getDailySchedule() {
		return dailySchedule;
	}

	public void setDailySchedule(DailyScheduleDetails dailySchedule) {
		this.dailySchedule = dailySchedule;
	}

	public WeeklyScheduleDetails getWeeklySchedule() {
		return weeklySchedule;
	}

	public void setWeeklySchedule(WeeklyScheduleDetails weeklySchedule) {
		this.weeklySchedule = weeklySchedule;
	}

	public MonthlyScheduleDetails getMonthlySchedule() {
		return monthlySchedule;
	}

	public void setMonthlySchedule(MonthlyScheduleDetails monthlySchedule) {
		this.monthlySchedule = monthlySchedule;
	}

	public YearlyScheduleDetails getYearlySchedule() {
		return yearlySchedule;
	}

	public void setYearlySchedule(YearlyScheduleDetails yearlySchedule) {
		this.yearlySchedule = yearlySchedule;
	}

	public Date getFilterStartDate() {
		return filterStartDate;
	}

	public void setFilterStartDate(Date filterStartDate) {
		this.filterStartDate = filterStartDate;
	}

	public Date getFilerEndDate() {
		return filerEndDate;
	}

	public void setFilerEndDate(Date filerEndDate) {
		this.filerEndDate = filerEndDate;
	}

	public List<Date> getDatesInConflict() {
		return datesInConflict;
	}

	public void setDatesInConflict(List<Date> datesInConflict) {
		this.datesInConflict = datesInConflict;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getDefaulttime() {
		return Constants.DEFAULTTIME;
	}

	public static CronSpecialChars getMonthOfWeek(String month) {
		return ScheduleOptionsUtil.getMonthOfWeek(month);
	}

	public static CronSpecialChars getDayOfWeek(String dayOfWeek) {
		return ScheduleOptionsUtil.getDayOfWeek(dayOfWeek);
	}

	public static CronSpecialChars getWeekOfMonth(String monthsWeek) {
		return ScheduleOptionsUtil.getWeekOfMonth(monthsWeek);
	}

	public static CronSpecialChars[] convertToWeekdays(String[] daysOfWeek) {
		return ScheduleOptionsUtil.convertToWeekdays(daysOfWeek);
	}
}
