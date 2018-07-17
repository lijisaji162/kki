package com.polus.fibicomp.committee.schedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class decorates ScheduleSequence to trim dates outside start and end dates passed as parameter.
 */
public class TrimDatesScheduleSequenceDecorator extends ScheduleSequenceDecorator {

	/**
	 * Constructs a TrimDatesScheduleSequenceDecorator.java.
	 * 
	 * @param scheduleSequence
	 */
	public TrimDatesScheduleSequenceDecorator(ScheduleSequence scheduleSequence) {
		super(scheduleSequence);
	}

	@Override
	public List<Date> executeScheduleSequence(String expression, Date startDate, Date endDate) throws ParseException {
		List<Date> dates = scheduleSequence.executeScheduleSequence(expression, startDate, endDate);
		List<Date> trimmedDates = new ArrayList<Date>();
		for (Date date : dates) {
			if (!date.before(startDate) && !date.after(endDate)) {
				trimmedDates.add(date);
			}
		}
		return trimmedDates;
	}
}
