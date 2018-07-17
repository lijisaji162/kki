package com.polus.fibicomp.committee.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberships;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.committee.schedule.CronSpecialChars;
import com.polus.fibicomp.committee.schedule.DailyScheduleDetails;
import com.polus.fibicomp.committee.schedule.DefaultScheduleSequence;
import com.polus.fibicomp.committee.schedule.MonthlyScheduleDetails;
import com.polus.fibicomp.committee.schedule.ScheduleData;
import com.polus.fibicomp.committee.schedule.ScheduleSequence;
import com.polus.fibicomp.committee.schedule.StyleKey;
import com.polus.fibicomp.committee.schedule.Time24HrFmt;
import com.polus.fibicomp.committee.schedule.TrimDatesScheduleSequenceDecorator;
import com.polus.fibicomp.committee.schedule.WeekScheduleSequenceDecorator;
import com.polus.fibicomp.committee.schedule.YearlyScheduleDetails;
import com.polus.fibicomp.committee.vo.CommitteeVo;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

@Transactional
@Service(value = "committeeService")
public class CommitteeServiceImpl implements CommitteeService {

	protected static Logger logger = Logger.getLogger(CommitteeServiceImpl.class.getName());

	private static final String COLON = ":";
	private static final String SCHEDULED = "Scheduled";

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private CommitteeScheduleService committeeScheduleService;

	@Override
	public String fetchInitialDatas() {
		CommitteeVo committeeVo = new CommitteeVo();
		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		committeeVo.setScheduleStatus(scheduleStatus);
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String createCommittee(Integer committeeTypeCode) {
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = new Committee();
		committeeVo.setCommitteeTypeCode(committeeTypeCode);
		CommitteeType committeeType = committeeDao.fetchCommitteeType(committeeTypeCode);
		committee.setCommitteeType(committeeType);
		committeeVo.setCommittee(committee);

		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		committeeVo.setScheduleStatus(scheduleStatus);

		committeeVo.setCommitteeMembershipTypes(committeeDao.getMembershipTypes());
		committeeVo.setMembershipRoles(committeeDao.getMembershipRoles());

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String saveCommittee(CommitteeVo vo) {
		Committee committee = vo.getCommittee();
		committee = committeeDao.saveCommittee(committee);
		vo.setStatus(true);
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			vo.setMessage("Committee created successfully");
		} else {
			vo.setMessage("Committee updated successfully");
		}
		vo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String loadCommitteeById(String committeeId) {
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = committeeDao.fetchCommitteeById(committeeId);
		List<CommitteeMemberships> committeeMemberships = committee.getCommitteeMemberships();
		if (committeeMemberships != null && !committeeMemberships.isEmpty()) {
			for (CommitteeMemberships membership : committeeMemberships) {
				if (membership.getNonEmployeeFlag()) {
					Rolodex rolodex = committeeDao.getRolodexById(membership.getRolodexId());
					membership.setRolodex(rolodex);
				} else {
					PersonDetailsView personDetails = committeeDao.getPersonDetailsById(membership.getPersonId());
					membership.setPersonDetails(personDetails);
				}
			}
		}
		committeeVo.setCommittee(committee);
		committeeVo.setCommitteeMembershipTypes(committeeDao.getMembershipTypes());
		committeeVo.setMembershipRoles(committeeDao.getMembershipRoles());
		committeeVo.setReviewTypes(committeeDao.fetchAllReviewType());
		committeeVo.setHomeUnits(committeeDao.fetchAllHomeUnits());
		committeeVo.setResearchAreas(committeeDao.fetchAllResearchAreas());
		committeeVo.setScheduleStatus(committeeDao.fetchAllScheduleStatus());

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String addSchedule(CommitteeVo committeeVo) throws ParseException {

		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		List<Date> dates = null;
		Date dtEnd = null;
		int frequency = 0;
		int day = 0;
		CronSpecialChars[] weekdays = null;
		CronSpecialChars weekOfMonth = null;
		CronSpecialChars dayOfWeek = null;
		CronSpecialChars month = null;

		Time24HrFmt time = getTime24hFmt(scheduleData.getScheduleStartDate(), scheduleData.getTime().findMinutes());
		Date dt = scheduleData.getScheduleStartDate();

		StyleKey key = StyleKey.valueOf(scheduleData.getRecurrenceType());
		logger.info("RecurrenceType : " + scheduleData.getRecurrenceType());
		logger.info("ScheduleStartDate : " + scheduleData.getScheduleStartDate());
		switch (key) {
		case NEVER:
			dates = committeeScheduleService.getScheduledDates(dt, dt, time, null);
			break;
		case DAILY:
			DailyScheduleDetails.optionValues dailyoption = DailyScheduleDetails.optionValues
					.valueOf(scheduleData.getDailySchedule().getDayOption());
			switch (dailyoption) {
			case XDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				day = scheduleData.getDailySchedule().getDay();
				dates = committeeScheduleService.getIntervalInDaysScheduledDates(dt, dtEnd, time, day);
				break;
			case WEEKDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getDailySchedule().getDaysOfWeek());
				ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
						new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()), 1, weekdays.length);
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
				break;
			}
			break;
		case WEEKLY:
			dtEnd = scheduleData.getWeeklySchedule().getScheduleEndDate();
			if (CollectionUtils.isNotEmpty(scheduleData.getWeeklySchedule().getDaysOfWeek())) {
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getWeeklySchedule().getDaysOfWeek()
						.toArray(new String[scheduleData.getWeeklySchedule().getDaysOfWeek().size()]));
			}

			ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
					new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()),
					scheduleData.getWeeklySchedule().getWeek(), weekdays.length);
			dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
			break;
		case MONTHLY:
			MonthlyScheduleDetails.optionValues monthOption = MonthlyScheduleDetails.optionValues
					.valueOf(scheduleData.getMonthlySchedule().getMonthOption());
			switch (monthOption) {
			case XDAYANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				day = scheduleData.getMonthlySchedule().getDay();
				frequency = scheduleData.getMonthlySchedule().getOption1Month();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, day, frequency, null);
				break;
			case XDAYOFWEEKANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getMonthlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getMonthlySchedule().getSelectedDayOfWeek());
				frequency = scheduleData.getMonthlySchedule().getOption2Month();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, dayOfWeek, weekOfMonth, frequency, null);
				break;
			}
			break;
		case YEARLY:
			YearlyScheduleDetails.yearOptionValues yearOption = YearlyScheduleDetails.yearOptionValues
					.valueOf(scheduleData.getYearlySchedule().getYearOption());
			switch (yearOption) {
			case XDAY:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption1Month());
				day = scheduleData.getYearlySchedule().getDay();
				frequency = scheduleData.getYearlySchedule().getOption1Year();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, month, day, frequency, null);
				break;
			case CMPLX:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getYearlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getYearlySchedule().getSelectedDayOfWeek());
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption2Month());
				frequency = scheduleData.getYearlySchedule().getOption2Year();
				dates = committeeScheduleService.getScheduledDates(dt, dtEnd, time, weekOfMonth, dayOfWeek, month, frequency,
						null);
				break;
			}
			break;
		}
		List<java.sql.Date> skippedDates = new ArrayList<java.sql.Date>();
		addScheduleDatesToCommittee(dates, committee, scheduleData.getPlace(), skippedDates, committeeVo);
		scheduleData.setDatesInConflict(skippedDates);
		logger.info("skippedDates : " + skippedDates);
		committee = committeeDao.saveCommittee(committee);
		committeeVo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	protected Time24HrFmt getTime24hFmt(Date date, int min) throws ParseException {
		Date dt = DateUtils.round(date, Calendar.DAY_OF_MONTH);
		dt = DateUtils.addMinutes(dt, min);
		Calendar cl = new GregorianCalendar();
		cl.setTime(dt);
		StringBuffer sb = new StringBuffer();
		String str = sb.append(cl.get(Calendar.HOUR_OF_DAY)).append(COLON).append(cl.get(Calendar.MINUTE)).toString();
		return new Time24HrFmt(str);
	}

	protected void addScheduleDatesToCommittee(List<Date> dates, Committee committee, String location,
			List<java.sql.Date> skippedDates, CommitteeVo committeeVo) {
		for (Date date : dates) {
			java.sql.Date sqldate = new java.sql.Date(date.getTime());

			if (!isDateAvailable(committee.getCommitteeSchedules(), sqldate)) {
				skippedDates.add(sqldate);
				continue;
			}

			CommitteeSchedule committeeSchedule = new CommitteeSchedule();
			committeeSchedule.setCommittee(committee);
			committeeSchedule.setScheduledDate(sqldate);
			committeeSchedule.setPlace(location);
			committeeSchedule.setTime(new Timestamp(date.getTime()));

			int daysToAdd = committee.getAdvSubmissionDaysReq();
			java.sql.Date sqlDate = calculateAdvancedSubmissionDays(date, daysToAdd);
			committeeSchedule.setProtocolSubDeadline(sqlDate);

			committeeSchedule.setCommittee(committee);

			ScheduleStatus defaultStatus = getDefaultScheduleStatus();
			committeeSchedule.setScheduleStatusCode(defaultStatus.getScheduleStatusCode());
			committeeSchedule.setScheduleStatus(defaultStatus);
			committeeSchedule.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committeeSchedule.setUpdateUser(committeeVo.getCurrentUser());

			committee.getCommitteeSchedules().add(committeeSchedule);
		}
	}

	protected Boolean isDateAvailable(List<CommitteeSchedule> committeeSchedules, java.sql.Date date) {
		boolean retVal = true;
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			Date scheduledDate = committeeSchedule.getScheduledDate();
			if ((scheduledDate != null) && DateUtils.isSameDay(scheduledDate, date)) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	protected Boolean isDateAvailableForUpdate(List<CommitteeSchedule> committeeSchedules, CommitteeSchedule schedule) {
		boolean retVal = true;
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			Date scheduledDate = committeeSchedule.getScheduledDate();
			if ((scheduledDate != null) && DateUtils.isSameDay(scheduledDate, schedule.getScheduledDate())) {
				if (!committeeSchedule.getScheduleId().equals(schedule.getScheduleId())) {
					retVal = false;
					break;
				}
			}
		}
		return retVal;
	}

	protected java.sql.Date calculateAdvancedSubmissionDays(Date startDate, Integer days) {
		Date deadlineDate = DateUtils.addDays(startDate, -days);
		return new java.sql.Date(deadlineDate.getTime());
	}

	protected ScheduleStatus getDefaultScheduleStatus() {
		return committeeDao.fetchScheduleStatusByStatus(SCHEDULED);
	}

	@Override
	public String saveAreaOfResearch(CommitteeVo committeeVo) {
		Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
		CommitteeResearchAreas committeeResearchAreas = committeeVo.getCommitteeResearchArea();
		/*committeeResearchAreas.setCommittee(committee);
		committee.getResearchAreas().add(committeeResearchAreas);*/
		CommitteeResearchAreas researchAreas = new CommitteeResearchAreas();
		researchAreas.setCommittee(committee);
		researchAreas.setResearchAreaCode(committeeResearchAreas.getResearchAreaCode());
		researchAreas.setResearchAreaDescription(committeeResearchAreas.getResearchAreaDescription());
		researchAreas.setUpdateTimestamp(committeeResearchAreas.getUpdateTimestamp());
		researchAreas.setUpdateUser(committeeResearchAreas.getUpdateUser());
		researchAreas = committeeDao.saveCommitteeResearchAreas(researchAreas);
		committee.getResearchAreas().add(researchAreas);
		committee = committeeDao.saveCommittee(committee);
		committeeVo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String deleteAreaOfResearch(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeResearchAreas> list = committee.getResearchAreas();
			List<CommitteeResearchAreas> updatedlist = new ArrayList<CommitteeResearchAreas>(list);
			Collections.copy(updatedlist, list);
			for (CommitteeResearchAreas researchArea : list) {
				if (researchArea.getCommResearchAreasId().equals(committeeVo.getCommResearchAreasId())) {
					updatedlist.remove(researchArea);
				}
			}
			committee.getResearchAreas().clear();
			committee.getResearchAreas().addAll(updatedlist);
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee research area deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(true);
			committeeVo.setMessage("Problem occurred in deleting committee research area");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(committeeVo);
	}

	@Override
	public String deleteSchedule(CommitteeVo committeeVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
			List<CommitteeSchedule> list = committee.getCommitteeSchedules();
			List<CommitteeSchedule> updatedlist = new ArrayList<CommitteeSchedule>(list);
			Collections.copy(updatedlist, list);
			for (CommitteeSchedule schedule : list) {
				if (schedule.getScheduleId().equals(committeeVo.getScheduleId())) {
					updatedlist.remove(schedule);
				}
			}
			committee.getCommitteeSchedules().clear();
			committee.getCommitteeSchedules().addAll(updatedlist);
			committeeDao.saveCommittee(committee);
			committeeVo.setCommittee(committee);
			committeeVo.setStatus(true);
			committeeVo.setMessage("Committee schedule deleted successfully");
		} catch (Exception e) {
			committeeVo.setStatus(true);
			committeeVo.setMessage("Problem occurred in deleting committee schedule");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(committeeVo);
	}

	@Override
	public String filterCommitteeScheduleDates(CommitteeVo committeeVo) {
		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		Date startDate = scheduleData.getFilterStartDate();
		Date endDate = scheduleData.getFilerEndDate();
		logger.info("FilterStartDate : " + startDate);
		logger.info("FilerEndDate : " + endDate);
		startDate = DateUtils.addDays(startDate, -1);
		endDate = DateUtils.addDays(endDate, 1);
		java.util.Date scheduleDate = null;
		for (CommitteeSchedule schedule : getSortedCommitteeScheduleList(committee)) {
			scheduleDate = schedule.getScheduledDate();
			if ((scheduleDate != null) && scheduleDate.after(startDate) && scheduleDate.before(endDate)) {
				schedule.setFilter(true);
			} else {
				schedule.setFilter(false);
			}
		}
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	private List<CommitteeSchedule> getSortedCommitteeScheduleList(Committee committee) {
        List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
        Collections.sort(committeeSchedules);
        return committeeSchedules;
    }

	@Override
	public String resetCommitteeScheduleDates(CommitteeVo committeeVo) {
		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		for (CommitteeSchedule schedule : getSortedCommitteeScheduleList(committee)) {
			schedule.setFilter(true);
		}
		scheduleData.setFilterStartDate(null);
		scheduleData.setFilerEndDate(null);
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String updateCommitteeSchedule(CommitteeVo committeeVo) {
		Committee committee = committeeDao.fetchCommitteeById(committeeVo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		CommitteeSchedule schedule = committeeVo.getCommitteeSchedule();
		boolean isDateExist = isDateAvailableForUpdate(committeeSchedules, schedule);
		String response = "";
		if (!isDateExist) {
			committeeVo.setStatus(false);
			response = "Scheduled date already exist";
			committeeVo.setMessage(response);
		} else {
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(schedule.getScheduleId())) {
					committeeSchedule.setScheduledDate(schedule.getScheduledDate());
					committeeSchedule.setPlace(schedule.getPlace());
					committeeSchedule.setTime(schedule.getTime());
					committeeSchedule.setScheduleStatus(schedule.getScheduleStatus());
					committeeSchedule.setScheduleStatusCode(schedule.getScheduleStatusCode());
					int daysToAdd = committeeSchedule.getCommittee().getAdvSubmissionDaysReq();
					java.sql.Date sqlDate = calculateAdvancedSubmissionDays(schedule.getScheduledDate(), daysToAdd);
					committeeSchedule.setProtocolSubDeadline(sqlDate);
				}
			}
			committeeDao.saveCommittee(committee);
		}
		committeeVo.setCommittee(committee);
		response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

}
