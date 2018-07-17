package com.polus.fibicomp.committee.schedule;

import java.io.Serializable;
import java.sql.Date;


/**
 * This class holds common UI data of all types of recurrence.
 */
public class ScheduleDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date scheduleEndDate;

	public ScheduleDetails() {
		super();
		setScheduleEndDate(new Date(new java.util.Date().getTime()));
	}

	public Date getScheduleEndDate() {
		return scheduleEndDate;
	}

	public void setScheduleEndDate(Date scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}

}
