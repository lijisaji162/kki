package com.polus.fibicomp.committee.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.schedule.CronExpression;
import com.polus.fibicomp.committee.schedule.CronSpecialChars;
import com.polus.fibicomp.committee.schedule.DefaultScheduleSequence;
import com.polus.fibicomp.committee.schedule.MonthDayCronExpression;
import com.polus.fibicomp.committee.schedule.MonthlyWeekDayCronExpression;
import com.polus.fibicomp.committee.schedule.NeverCronExpression;
import com.polus.fibicomp.committee.schedule.ScheduleSequence;
import com.polus.fibicomp.committee.schedule.Time24HrFmt;
import com.polus.fibicomp.committee.schedule.TrimDatesScheduleSequenceDecorator;
import com.polus.fibicomp.committee.schedule.WeekCronExpression;
import com.polus.fibicomp.committee.schedule.YearMonthDayCronExpression;
import com.polus.fibicomp.committee.schedule.YearMonthDayOfWeekCronExpression;

@Transactional
@Service(value = "committeeScheduleService")
public class CommitteeScheduleServiceImpl implements CommitteeScheduleService {

	protected static Logger logger = Logger.getLogger(CommitteeScheduleServiceImpl.class.getName());

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time,
			ScheduleSequence scheduleSequence) throws ParseException {
		CronExpression expr = new NeverCronExpression(startDate, time);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

	/**
	 * This is helper method, gets appropriate ScheduleSequence in case of null, wraps time accurately and executes schedule sequence.
	 * 
	 * @param expression expects valid cron expression.
	 * @param startDate is begin date of sequence.
	 * @param endDate is end date of sequence.
	 * @param time at which schedule is held.
	 * @param scheduleSequence executes schedule sequence based on implementation of the ScheduleSequence.
	 * @return List&lt;Date&gt; of valid schedule dates between start and end date.
	 * @throws ParseException
	 */
	protected List<Date> getScheduledDates(CronExpression expression, Date startDate, Date endDate, Time24HrFmt time,
			ScheduleSequence scheduleSequence) throws ParseException {
		scheduleSequence = getScheduleSequence(scheduleSequence);
		startDate = wrapTime(startDate, null);
		endDate = wrapTime(endDate, time);
		return scheduleSequence.executeScheduleSequence(expression.getExpression(), startDate, endDate);
	}

	/**
	 * This is helper method, if ScheduleSequence passed is null it will construct new DefaultScheduleSequence.
	 * 
	 * @param scheduleSequence
	 * @return ScheduleSequence after sanity check.
	 */
	protected ScheduleSequence getScheduleSequence(ScheduleSequence scheduleSequence) {
		if (null == scheduleSequence) {
			scheduleSequence = new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence());
		}
		return scheduleSequence;
	}

	/**
	 * This is helper method, wraps date with time accurately in java.util.Date (milliseconds).
	 * 
	 * @param date to be wrapped.
	 * @param time to be added to date.
	 * @return wrapped date &amp; time.
	 */
	protected Date wrapTime(Date date, Time24HrFmt time) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR);
		int min = calendar.get(Calendar.MINUTE);
		int am_pm = calendar.get(Calendar.AM_PM);
		if (am_pm == Calendar.AM) {
			date = DateUtils.addHours(date, -hour);
			date = DateUtils.addMinutes(date, -min);
		} else {
			date = DateUtils.addHours(date, -hour - 12);
			date = DateUtils.addMinutes(date, -min);
		}
		if (null != time) {
			date = DateUtils.addHours(date, new Integer(time.getHours()));
			date = DateUtils.addMinutes(date, new Integer(time.getMinutes()));
		}
		return date;
	}

	@Override
	public List<Date> getIntervalInDaysScheduledDates(Date startDate, Date endDate, Time24HrFmt time,
			Integer intervalInDays) throws ParseException {
		// create the start time for the simple trigger by setting the time of the startDate to noon---24 hour jumps will
		// still go to the next day irrespective of US daylight savings.
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date triggerStartDate = cal.getTime();

		// create the end time for the simple trigger by setting the time of the endDate to 2 pm
		cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date triggerEndDate = cal.getTime();

		SimpleTrigger trigger = new SimpleTrigger("myTrigger", "default", triggerStartDate, triggerEndDate,
				SimpleTrigger.REPEAT_INDEFINITELY, ((long) intervalInDays) * 24L * 60L * 60L * 1000L);

		@SuppressWarnings("unchecked")
		List<Date> dates = TriggerUtils.computeFireTimesBetween(trigger, null, triggerStartDate, triggerEndDate);

		// iterate through the dates and set each date's hour and minute as per the given argument (to avoid daylight savings timing issues)
		List<Date> returnDates = new ArrayList<Date>();
		for (Date date : dates) {
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.getHours()));
			cal.set(Calendar.MINUTE, Integer.valueOf(time.getMinutes()));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			returnDates.add(cal.getTime());
		}

		return returnDates;
	}

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time, CronSpecialChars[] weekdays,
			ScheduleSequence scheduleSequence) throws ParseException {
		CronExpression expr = new WeekCronExpression(startDate, time, weekdays);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time, Integer day,
			Integer frequencyInMonth, ScheduleSequence scheduleSequence) throws ParseException {
		CronExpression expr = new MonthDayCronExpression(startDate, time, day, frequencyInMonth);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time, CronSpecialChars dayOfWeek,
			CronSpecialChars weekOfMonth, Integer frequencyInMonth, ScheduleSequence scheduleSequence)
			throws ParseException {
		CronExpression expr = new MonthlyWeekDayCronExpression(startDate, time, dayOfWeek, weekOfMonth, frequencyInMonth);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time, CronSpecialChars month,
			Integer day, Integer frequencyInYear, ScheduleSequence scheduleSequence) throws ParseException {
		CronExpression expr = new YearMonthDayCronExpression(startDate, time, month, day, frequencyInYear);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

	@Override
	public List<Date> getScheduledDates(Date startDate, Date endDate, Time24HrFmt time, CronSpecialChars weekOfMonth,
			CronSpecialChars dayOfWeek, CronSpecialChars month, Integer frequencyInYear,
			ScheduleSequence scheduleSequence) throws ParseException {
		CronExpression expr = new YearMonthDayOfWeekCronExpression(startDate, time, weekOfMonth, dayOfWeek, month, frequencyInYear);
		return getScheduledDates(expr, startDate, endDate, time, scheduleSequence);
	}

}
