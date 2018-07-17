package com.polus.fibicomp.committee.schedule;

import com.polus.fibicomp.committee.schedule.DayOfWeek;
import com.polus.fibicomp.committee.schedule.Months;
import com.polus.fibicomp.committee.schedule.WeekOfMonth;
import com.polus.fibicomp.committee.schedule.CronSpecialChars;

public class ScheduleOptionsUtil {

	@SuppressWarnings("unused")
	private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
			.getLog(ScheduleOptionsUtil.class);

	/**
	 * This method returns CronSpecialChars of a month.
	 * 
	 * @param month
	 * @return
	 */
	public static CronSpecialChars getMonthOfWeek(String month) {
		String abbr = Months.valueOf(month).getAbbr();
		return CronSpecialChars.valueOf(abbr);
	}

	/**
	 * This method returns CronSpecialChars of a day of week.
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	public static CronSpecialChars getDayOfWeek(String dayOfWeek) {
		String abbr = DayOfWeek.valueOf(dayOfWeek).getAbbr();
		return CronSpecialChars.valueOf(abbr);
	}

	/**
	 * This method returns CronSpecialChars of month of week.
	 * 
	 * @param monthsWeek
	 * @return
	 */
	public static CronSpecialChars getWeekOfMonth(String monthsWeek) {
		String number = WeekOfMonth.valueOf(monthsWeek).getNumber();
		return CronSpecialChars.valueOf(number);
	}

	/**
	 * This method returns CronSpecialChars[] of days of week.
	 * 
	 * @param daysOfWeek
	 * @return
	 */
	public static CronSpecialChars[] convertToWeekdays(String[] daysOfWeek) {
		CronSpecialChars[] weekdays = new CronSpecialChars[daysOfWeek.length];
		int i = 0;
		for (String str : daysOfWeek) {
			if (null != str) {
				String abbr = DayOfWeek.valueOf(str).getAbbr();
				weekdays[i++] = CronSpecialChars.valueOf(abbr);
			}
		}
		return weekdays;
	}
}
