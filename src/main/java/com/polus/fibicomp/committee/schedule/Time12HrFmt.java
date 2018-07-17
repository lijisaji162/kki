package com.polus.fibicomp.committee.schedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.polus.fibicomp.constants.Constants;

public class Time12HrFmt implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum MERIDIEM {
		AM, PM
	};

	private String time;

	private String meridiem;

	public Time12HrFmt() {
		
	}

	public Time12HrFmt(Timestamp day) {
		parseTimeTo12HrFmt(day);
	}

	public Time12HrFmt(String time, MERIDIEM meridiem) {
		this.time = time;
		this.meridiem = meridiem.toString();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMeridiem() {
		return meridiem;
	}

	public void setMeridiem(String meridiem) {
		this.meridiem = meridiem;
	}

	private void parseTimeTo12HrFmt(Timestamp day) {
		Calendar cl = new GregorianCalendar();
		if (day != null) {
			cl.setTime(day);
		}

		if (cl.get(Calendar.AM_PM) == 0)
			this.meridiem = MERIDIEM.AM.name();
		else
			this.meridiem = MERIDIEM.PM.name();

		int hr = cl.get(Calendar.HOUR_OF_DAY);

		hr = (hr == 0 ? 12 : hr);
		hr = (hr > 12 ? hr - 12 : hr);

		int min = cl.get(Calendar.MINUTE);

		String str = buildDisplayView(hr, min);

		this.time = str;
	}

	private String buildDisplayView(int hrs, int mins) {
		StringBuilder str = new StringBuilder();

		if (hrs < 10)
			str.append(Constants.ZERO).append(hrs);
		else
			str.append(hrs);

		str.append(Constants.COLON);

		if (mins < 10)
			str.append(Constants.ZERO).append(mins);
		else
			str.append(mins);

		return str.toString();
	}

	public int findMinutes() {

		String[] result = time.split(Constants.COLON);

		int hrs = new Integer(result[0]);
		int min = new Integer(result[1]);

		boolean am_pm = false;
		if (meridiem.equalsIgnoreCase(MERIDIEM.AM.toString()))
			am_pm = true;

		int mins = 0;
		if (hrs == 12)
			mins = 0 + min + (am_pm ? 0 : 12 * 60);
		else
			mins = (hrs * 60) + min + (am_pm ? 0 : 12 * 60);

		return mins;
	}

	@Override
	public String toString() {
		return time;
	}

}
