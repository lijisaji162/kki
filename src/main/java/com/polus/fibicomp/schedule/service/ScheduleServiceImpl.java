package com.polus.fibicomp.schedule.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberRoles;
import com.polus.fibicomp.committee.pojo.CommitteeMemberships;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachType;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttachment;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttendance;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleMinutes;
import com.polus.fibicomp.committee.pojo.MinuteEntryType;
import com.polus.fibicomp.committee.pojo.ProtocolContingency;
import com.polus.fibicomp.committee.pojo.ProtocolSubmission;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.committee.schedule.Time12HrFmt;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.schedule.dao.ScheduleDao;
import com.polus.fibicomp.schedule.vo.ScheduleVo;
import com.polus.fibicomp.view.ProtocolView;

@Transactional
@Service(value = "scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	protected static Logger logger = Logger.getLogger(ScheduleServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	public String loadScheduleById(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleId);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommittee(committeeSchedule.getCommittee());
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		scheduleVo.setScheduleStatus(scheduleStatus);
		List<ScheduleActItemType> scheduleActItemTypes = scheduleDao.fetchAllScheduleActItemType();
		scheduleVo.setScheduleActItemTypes(scheduleActItemTypes);
		List<MinuteEntryType> minuteEntrytypes = scheduleDao.fetchAllMinuteEntryTypes();
		scheduleVo.setMinuteEntrytypes(minuteEntrytypes);
		List<ProtocolContingency> protocolContingencies = scheduleDao.fetchAllProtocolContingency();
		scheduleVo.setProtocolContingencies(protocolContingencies);
		List<CommitteeScheduleAttachType> committeeScheduleAttachTypes = scheduleDao.fetchAllCommitteeScheduleAttachType();
		scheduleVo.setAttachmentTypes(committeeScheduleAttachTypes);
		List<ProtocolSubmission> protocolSubmissions = committeeSchedule.getProtocolSubmissions();
		if (protocolSubmissions != null && !protocolSubmissions.isEmpty()) {
			for (ProtocolSubmission protocolSubmission : protocolSubmissions) {
				logger.info("protocolId : " + protocolSubmission.getProtocolId());
				logger.info("piPersonId : " + protocolSubmission.getPiPersonId());
				logger.info("piPersonName : " + protocolSubmission.getPiPersonName());
				ProtocolView protocolView = scheduleDao.fetchProtocolViewByParams(protocolSubmission.getProtocolId().intValue(), protocolSubmission.getPiPersonId(), protocolSubmission.getPiPersonName());
				if (protocolView != null) {
					protocolSubmission.setDocumentNumber(protocolView.getDocumentNumber());
				}
			}
		}
		if (committeeSchedule.getCommitteeScheduleAttendances().isEmpty() && !committeeSchedule.getCommittee().getCommitteeMemberships().isEmpty()) {
			List<CommitteeScheduleAttendance> activeMembers = new ArrayList<CommitteeScheduleAttendance>();
			initAttendance(activeMembers, committeeSchedule);
			committeeSchedule.getCommitteeScheduleAttendances().clear();
			committeeSchedule.getCommitteeScheduleAttendances().addAll(activeMembers);
			committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		} else {
			//populateAttendanceToForm(scheduleVo, committeeSchedule.getCommittee().getCommitteeMemberships(), committeeSchedule);
			List<CommitteeScheduleAttendance> activeMembers = new ArrayList<CommitteeScheduleAttendance>();
			loadAttendance(activeMembers, committeeSchedule);
			//committeeSchedule.getCommitteeScheduleAttendances().clear();
			if (!activeMembers.isEmpty()) {
				committeeSchedule.getCommitteeScheduleAttendances().addAll(activeMembers);
				committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
			}
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	/*
	 * Init attendance if this meeting schedule is maintained for the first time.
	 */
	protected void initAttendance(List<CommitteeScheduleAttendance> committeeScheduleAttendances, CommitteeSchedule commSchedule) {
		List<CommitteeMemberships> committeeMemberships = commSchedule.getCommittee().getCommitteeMemberships();
		committeeMemberships.forEach(committeeMembership -> {
			if (isActiveMembership(committeeMembership, commSchedule.getScheduledDate())) {
				CommitteeScheduleAttendance committeeScheduleAttendance = new CommitteeScheduleAttendance();
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					committeeScheduleAttendance.setPersonId(committeeMembership.getRolodexId().toString());
					committeeScheduleAttendance.setNonEmployeeFlag(true);
				} else {
					committeeScheduleAttendance.setPersonId(committeeMembership.getPersonId());
					committeeScheduleAttendance.setNonEmployeeFlag(false);
				}
				committeeScheduleAttendance.setPersonName(committeeMembership.getPersonName());
				if (isAlternate(committeeMembership, commSchedule.getScheduledDate())) {
					committeeScheduleAttendance.setAlternateFlag(true);
				} else {
					committeeScheduleAttendance.setAlternateFlag(false);
				}
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				committeeScheduleAttendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				// memberAbsentBean.setAttendance(committeeScheduleAttendance);
				committeeScheduleAttendance.setMemberPresent(false);
				committeeScheduleAttendance.setCommitteeSchedule(commSchedule);
				committeeScheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(committeeScheduleAttendance);
				committeeScheduleAttendances.add(committeeScheduleAttendance);
			}
		});
	}

	protected void loadAttendance(List<CommitteeScheduleAttendance> committeeScheduleAttendances, CommitteeSchedule commSchedule) {
		List<CommitteeMemberships> committeeMemberships = commSchedule.getCommittee().getCommitteeMemberships();
		List<CommitteeMemberships> updatedlist = new ArrayList<CommitteeMemberships>(committeeMemberships);
		Collections.copy(updatedlist, committeeMemberships);
		List<CommitteeScheduleAttendance> commScheduleAttendances = commSchedule.getCommitteeScheduleAttendances();
		committeeMemberships.forEach(committeeMembership -> {
			if (isActiveMembership(committeeMembership, commSchedule.getScheduledDate())) {
				for (CommitteeScheduleAttendance attendance : commScheduleAttendances) {
					if ((attendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null && 
							attendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
							|| (!attendance.getNonEmployeeFlag() && attendance.getPersonId().equals(committeeMembership.getPersonId()))) {
						//committeeScheduleAttendances.add(attendance);
						updatedlist.remove(committeeMembership);
					}
				}
			} else {
				updatedlist.remove(committeeMembership);
			}
		});
		for (CommitteeMemberships updated : updatedlist) {
			CommitteeScheduleAttendance committeeScheduleAttendance = new CommitteeScheduleAttendance();
			if (StringUtils.isBlank(updated.getPersonId())) {
				committeeScheduleAttendance.setPersonId(updated.getRolodexId().toString());
				committeeScheduleAttendance.setNonEmployeeFlag(true);
			} else {
				committeeScheduleAttendance.setPersonId(updated.getPersonId());
				committeeScheduleAttendance.setNonEmployeeFlag(false);
			}
			committeeScheduleAttendance.setPersonName(updated.getPersonName());
			if (isAlternate(updated, commSchedule.getScheduledDate())) {
				committeeScheduleAttendance.setAlternateFlag(true);
			} else {
				committeeScheduleAttendance.setAlternateFlag(false);
			}
			committeeScheduleAttendance.setRoleName(getRoleNameForMembership(updated, commSchedule.getScheduledDate()));
			committeeScheduleAttendance.setMemberPresent(false);
			committeeScheduleAttendance.setCommitteeSchedule(commSchedule);
			committeeScheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(committeeScheduleAttendance);
			committeeScheduleAttendances.add(committeeScheduleAttendance);
		}
	}

	protected boolean isActiveMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		return isActiveForScheduledDate(scheduledDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())
				&& hasActiveMembershipRoleForScheduledDate(committeeMembership.getCommitteeMemberRoles(), scheduledDate);
	}

	private boolean isActiveForScheduledDate(Date scheduledDate, Date startDate, Date endDate) {
		return startDate.before(scheduledDate) && endDate.after(scheduledDate);
	}

	private boolean hasActiveMembershipRoleForScheduledDate(List<CommitteeMemberRoles> committeeMembershipRoles,
			Date scheduledDate) {
		for (CommitteeMemberRoles membershipRole : committeeMembershipRoles) {
			if (!membershipRole.getMembershipRoleCode().equals(Constants.INACTIVE_ROLE)
					&& isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * get rolename, concatenated with ',' separator if multiple roles exist for
	 * this membership
	 */
	protected String getRoleNameForMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		String roleName = "";
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				roleName = roleName + "," + membershipRole.getMembershipRoleDescription();
			}
		}
		if (StringUtils.isNotBlank(roleName)) {
			roleName = roleName.substring(1); // remove ","
		}
		return roleName;
	}

	/*
	 * check if this membership has alternate role based on schedule date.
	 */
	protected boolean isAlternate(CommitteeMemberships committeeMembership, Date scheduledDate) {
		boolean isAlternate = false;
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (membershipRole.getMembershipRoleCode().equals(Constants.ALTERNATE_ROLE)
					&& !membershipRole.getStartDate().after(scheduledDate)
					&& !membershipRole.getEndDate().before(scheduledDate)) {
				isAlternate = true;
				break;
			}
		}
		return isAlternate;
	}

	/*
	 * populate 3 attendance form beans
	 */
	protected void populateAttendanceToForm(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		//populatePresentBean(scheduleVo, committeeMemberships, commSchedule);
		populateMemberAbsentBean(scheduleVo, committeeMemberships, commSchedule);
	}

	/*
	 * populate memberpresentbean &amp; otherpresentbean
	 */
	protected void populatePresentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships,
			CommitteeSchedule commSchedule) {
		//scheduleVo.setOtherPresents(new ArrayList<CommitteeScheduleAttendance>());
		// commSchedule.setMemberPresents(new ArrayList<>());
		for (CommitteeScheduleAttendance committeeScheduleAttendance : commSchedule.getCommitteeScheduleAttendances()) {
			getRoleName(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate());
			if (committeeScheduleAttendance.getGuestFlag()) {
				// OtherPresentBeanBase otherPresentBean =
				// getNewOtherPresentBeanInstanceHook();
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
				committeeScheduleAttendance.setGuestMemberActive(isActiveMember(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeScheduleAttendance.getRoleName())) {
					committeeScheduleAttendance.setRoleName("Guest");
				}
				//scheduleVo.getOtherPresents().add(committeeScheduleAttendance);
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
			}
			/*
			 * else { MemberPresentBean memberPresentBean = new
			 * MemberPresentBean();
			 * memberPresentBean.setAttendance(committeeScheduleAttendance);
			 * meetingHelper.getMemberPresentBeans().add(memberPresentBean); }
			 */
		}
	}

	/*
	 * get a person's role name within this committee memberships based on
	 * schedule date.
	 */
	protected void getRoleName(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		String roleName = "";
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				roleName = getRoleNameForMembership(committeeMembership, scheduleDate);
				break;
			}
		}
		committeeScheduleAttendance.setRoleName(roleName);
	}

	/*
	 * Check if this member is active in this committee. Inactive scenario : -
	 * not defined in membership. - in membership, but non of the memberships
	 * period cover schedule date - an 'Inactive' role period cover schedule
	 * date.
	 */
	protected boolean isActiveMember(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		boolean isActiveMember = false;
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				if (isActiveForScheduledDate(scheduleDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())) {
					isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
				}
			}
		}
		return isActiveMember;
	}

	/*
	 * populate memberabsentbean
	 */
	protected void populateMemberAbsentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		//scheduleVo.setMemberAbsents(new ArrayList<CommitteeScheduleAttendance>());
		committeeMemberships.forEach(committeeMembership -> {
			if (!isInMemberPresent(commSchedule.getCommitteeScheduleAttendances(), committeeMembership)
					&& !isInOtherPresent(commSchedule.getCommitteeScheduleAttendances(), committeeMembership)) {
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				CommitteeScheduleAttendance attendance = new CommitteeScheduleAttendance();
				attendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					attendance.setPersonId(committeeMembership.getRolodexId().toString());
				} else {
					attendance.setPersonId(committeeMembership.getPersonId());
				}
				if (isActiveMemberAbsent(attendance, committeeMembership, commSchedule.getScheduledDate())) {
					attendance.setPersonName(committeeMembership.getPersonName());
					attendance.setAlternateFlag(false);
					attendance.setNonEmployeeFlag(StringUtils.isBlank(committeeMembership.getPersonId()));
					// memberAbsentBean.setAttendance(attendance);
					//scheduleVo.getMemberAbsents().add(attendance);
				}
			}
		});
	}

	/*
	 * check if person is in member present.
	 */
	protected boolean isInMemberPresent(List<CommitteeScheduleAttendance> memberPresents,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance memberPresentBean : memberPresents) {
			if (memberPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!memberPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	/*
	 * check if person is in other present
	 */
	protected boolean isInOtherPresent(List<CommitteeScheduleAttendance> otherPresentBeans,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance otherPresentBean : otherPresentBeans) {
			if (otherPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!otherPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	protected boolean isActiveMemberAbsent(CommitteeScheduleAttendance committeeScheduleAttendance,
			CommitteeMemberships committeeMembership, Date scheduleDate) {
		boolean isActiveMember = false;
		if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
				&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
				|| (!committeeScheduleAttendance.getNonEmployeeFlag()
						&& committeeScheduleAttendance.getPersonId() != null
						&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
			if (!committeeMembership.getTermStartDate().after(scheduleDate)
					&& !committeeMembership.getTermEndDate().before(scheduleDate)) {
				isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
			}
		}

		return isActiveMember;
	}

	@Override
	public String updateSchedule(ScheduleVo scheduleVo) {
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		CommitteeSchedule committeeSchedule = scheduleVo.getCommitteeSchedule();
		committeeSchedule.setCommittee(committee);
		committeeSchedule.setStartTime(addHrMinToDate(committeeSchedule.getViewStartTime()));
        committeeSchedule.setEndTime(addHrMinToDate(committeeSchedule.getViewEndTime()));
        committeeSchedule.setTime(addHrMinToDate(committeeSchedule.getViewTime()));
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		committee.getCommitteeSchedules().add(committeeSchedule);
		committee = committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	/*
     * utility methods by adding minutes to date
     */
	protected Timestamp addHrMinToDate(Time12HrFmt viewTime) {
		java.util.Date dt = new java.util.Date(0); // this is actually 12-31-1969 19:00. its GMT time
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		try {
			// support localization.
			dt = dateFormat.parse("01/01/1970 " + viewTime.getTime() + " " + viewTime.getMeridiem());
			return new Timestamp(dt.getTime());
		} catch (Exception e) {
			// folowing may convert date to 07-02-1970 iftz is gmt+12 or more
			dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
			return new Timestamp(DateUtils.addMinutes(dt, viewTime.findMinutes()).getTime());
		}
	}

	@Override
	public String addOtherActions(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleActItems committeeScheduleActItems = scheduleVo.getCommitteeScheduleActItems();

		CommitteeScheduleActItems scheduleActItem = new CommitteeScheduleActItems();
		scheduleActItem.setCommitteeSchedule(committeeSchedule);
		scheduleActItem.setScheduleActItemTypecode(committeeScheduleActItems.getScheduleActItemTypecode());
		scheduleActItem.setItemDescription(committeeScheduleActItems.getItemDescription());
		scheduleActItem.setScheduleActItemTypeDescription(committeeScheduleActItems.getScheduleActItemTypeDescription());
		scheduleActItem.setActionItemNumber(getNextActionItemNumber(committeeSchedule));
		scheduleActItem.setUpdateTimestamp(committeeScheduleActItems.getUpdateTimestamp());
		scheduleActItem.setUpdateUser(committeeScheduleActItems.getUpdateUser());
		scheduleActItem = scheduleDao.addOtherActions(scheduleActItem);

		committeeSchedule.getCommitteeScheduleActItems().add(scheduleActItem);
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		committeeSchedule.setCommittee(committee);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	public Integer getNextActionItemNumber(CommitteeSchedule committeeSchedule) {
		Integer nextActionItemNumber = committeeSchedule.getCommitteeScheduleActItems().size();
		for (CommitteeScheduleActItems commScheduleActItem : committeeSchedule.getCommitteeScheduleActItems()) {
			if (commScheduleActItem.getActionItemNumber() > nextActionItemNumber) {
				nextActionItemNumber = commScheduleActItem.getActionItemNumber();
			}
		}
		return nextActionItemNumber + 1;
	}

	@Override
	public String deleteOtherActions(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleActItems> list = committeeSchedule.getCommitteeScheduleActItems();
					List<CommitteeScheduleActItems> updatedlist = new ArrayList<CommitteeScheduleActItems>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleActItems actionItem : list) {
						if (actionItem.getCommScheduleActItemsId().equals(scheduleVo.getCommScheduleActItemsId())) {
							updatedlist.remove(actionItem);
						}
					}
					committeeSchedule.getCommitteeScheduleActItems().clear();
					committeeSchedule.getCommitteeScheduleActItems().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule other action item deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule other action item");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	@Override
	public String addCommitteeScheduleMinute(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleMinutes committeeScheduleMinute = scheduleVo.getNewCommitteeScheduleMinute();

		String protocolNumber = null;
		Long submissionId = null;
		Integer submissionNumber = null;

		if (committeeScheduleMinute.getProtocolNumber() != null) {
			protocolNumber = committeeScheduleMinute.getProtocolNumber();
			for (ProtocolSubmission protocolSubmission : committeeSchedule.getProtocolSubmissions()) {
				if (protocolSubmission.getProtocolNumber().equals(protocolNumber)) {
					submissionId = protocolSubmission.getSubmissionId();
					submissionNumber = protocolSubmission.getSubmissionNumber();
				}
			}
		}

		Integer entryNumber = getNextMinuteEntryNumber(committeeSchedule);
		String minuteEntryTypeCode = committeeScheduleMinute.getMinuteEntryTypeCode().toString();

		committeeScheduleMinute.setSubmissionId(submissionId);
		committeeScheduleMinute.setSubmissionNumber(submissionNumber);
		committeeScheduleMinute.setProtocolNumber(protocolNumber);
		committeeScheduleMinute.setEntryNumber(entryNumber);
		committeeScheduleMinute.setCommitteeSchedule(committeeSchedule);

		if (Constants.ATTENDANCE.equals(minuteEntryTypeCode)) {
			addAttendanceMinuteEntry(committeeSchedule, committeeScheduleMinute);
		} else if (Constants.ACTION_ITEM.equals(minuteEntryTypeCode)) {
			addActionItem(committeeSchedule, committeeScheduleMinute);
		} else if (Constants.PROTOCOL.equals(minuteEntryTypeCode) || Constants.PROTOCOL_REVIEWER_COMMENT.equals(minuteEntryTypeCode)) {
			resetActionItemFields(committeeScheduleMinute);
		} else {
			resetProtocolFields(committeeScheduleMinute);
			resetActionItemFields(committeeScheduleMinute);
		}

		committeeScheduleMinute = scheduleDao.addCommitteeScheduleMinute(committeeScheduleMinute);
		committeeSchedule.getCommitteeScheduleMinutes().add(committeeScheduleMinute);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	protected Integer getNextMinuteEntryNumber(CommitteeSchedule committeeSchedule) {
		Integer nextMinuteEntryNumber = committeeSchedule.getCommitteeScheduleMinutes().size();
		for (CommitteeScheduleMinutes committeeScheduleMinute : committeeSchedule.getCommitteeScheduleMinutes()) {
			if (committeeScheduleMinute.getEntryNumber() > nextMinuteEntryNumber) {
				nextMinuteEntryNumber = committeeScheduleMinute.getEntryNumber();
			}
		}
		return nextMinuteEntryNumber + 1;
	}

	protected void addAttendanceMinuteEntry(CommitteeSchedule committeeSchedule,
			CommitteeScheduleMinutes committeeScheduleMinutes) {
		if (committeeScheduleMinutes.isGenerateAttendance()) {
			committeeScheduleMinutes.setMinuteEntry(generateAttendanceComment(committeeSchedule.getCommitteeScheduleAttendances(), committeeSchedule));
		}
		resetProtocolFields(committeeScheduleMinutes);
		resetActionItemFields(committeeScheduleMinutes);
	}

	protected String generateAttendanceComment(List<CommitteeScheduleAttendance> memberPresentList, CommitteeSchedule committeeSchedule) {
		String comment = "";
		String eol = System.getProperty("line.separator");
		for (CommitteeScheduleAttendance memberPresent : memberPresentList) {
			if (StringUtils.isNotBlank(comment)) {
				comment = comment + eol;
			}
			comment = comment + memberPresent.getPersonName();
			if (StringUtils.isNotBlank(memberPresent.getAlternateFor())) {
				comment = comment + " Alternate For: "
						+ getAlternateForName(committeeSchedule, memberPresent.getAlternateFor());
			}
			if (memberPresent.getGuestFlag()) {
				comment = comment + memberPresent.getPersonName() + " Guest ";
			}
		}
		return comment;
	}

	protected String getAlternateForName(CommitteeSchedule committeeSchedule, String alternateFor) {
		String personName = "";
		for (CommitteeMemberships committeeMembership : committeeSchedule.getCommittee().getCommitteeMemberships()) {
			if ((StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& committeeMembership.getPersonId().equals(alternateFor))
					|| (StringUtils.isBlank(committeeMembership.getPersonId())
							&& committeeMembership.getRolodexId().toString().equals(alternateFor))) {
				personName = committeeMembership.getPersonName();
				break;
			}
		}
		return personName;
	}

	protected void resetProtocolFields(CommitteeScheduleMinutes committeeScheduleMinutes) {
		committeeScheduleMinutes.setProtocolId(null);
		committeeScheduleMinutes.setProtocolNumber(null);
		// committeeScheduleMinutes.setProtocol(null);
	}

	protected void resetActionItemFields(CommitteeScheduleMinutes committeeScheduleMinutes) {
		committeeScheduleMinutes.setCommScheduleActItemsId(null);
		committeeScheduleMinutes.setScheduleActItems(null);
	}

	protected void addActionItem(CommitteeSchedule committeeSchedule, CommitteeScheduleMinutes committeeScheduleMinutes) {
		if (committeeScheduleMinutes.getCommScheduleActItemsId() != null) {
			// in case adding non-persisted action item
			committeeScheduleMinutes.setScheduleActItems(getActionItem(committeeScheduleMinutes.getCommScheduleActItemsId(), committeeSchedule.getCommitteeScheduleActItems()));
		}
		resetProtocolFields(committeeScheduleMinutes);
	}

	protected CommitteeScheduleActItems getActionItem(Integer commScheduleActItemsId, List<CommitteeScheduleActItems> commScheduleActItems) {
		CommitteeScheduleActItems actionItem = null;

		for (CommitteeScheduleActItems commScheduleActItem : commScheduleActItems) {
			if (commScheduleActItem.getCommScheduleActItemsId().equals(commScheduleActItemsId)) {
				actionItem = commScheduleActItem;
				break;
			}
		}

		return actionItem;
	}

	@Override
	public String updateScheduleAttendance(ScheduleVo scheduleVo) {
		String response = "";
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		CommitteeScheduleAttendance scheduleAttendance = scheduleVo.getUpdatedAttendance();
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
				List<CommitteeScheduleAttendance> attendances = committeeSchedule.getCommitteeScheduleAttendances();
				for (CommitteeScheduleAttendance attendance : attendances) {
					if (attendance.getCommitteeScheduleAttendanceId().equals(scheduleAttendance.getCommitteeScheduleAttendanceId())) {
						attendance.setMemberPresent(scheduleAttendance.getMemberPresent());
						attendance.setComments(scheduleAttendance.getComments());
						attendance.setAlternateFor(scheduleAttendance.getAlternateFor());
						attendance.setUpdateTimestamp(scheduleAttendance.getUpdateTimestamp());
						attendance.setUpdateUser(scheduleAttendance.getUpdateUser());
					}
				}
				scheduleVo.setCommitteeSchedule(committeeSchedule);
			}
		}
		committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	@Override
	public String addOthersPresent(ScheduleVo scheduleVo) {
		String response = "";
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleAttendance scheduleAttendance = scheduleVo.getUpdatedAttendance();
		scheduleAttendance.setCommitteeSchedule(committeeSchedule);
		scheduleAttendance = scheduleDao.addCommitteeScheduleAttendance(scheduleAttendance);
		committeeSchedule.getCommitteeScheduleAttendances().add(scheduleAttendance);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	@Override
	public String deleteScheduleMinute(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleMinutes> list = committeeSchedule.getCommitteeScheduleMinutes();
					List<CommitteeScheduleMinutes> updatedlist = new ArrayList<CommitteeScheduleMinutes>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleMinutes minutes : list) {
						if (minutes.getCommScheduleMinutesId().equals(scheduleVo.getCommScheduleMinuteId())) {
							updatedlist.remove(minutes);
						}
					}
					committeeSchedule.getCommitteeScheduleMinutes().clear();
					committeeSchedule.getCommitteeScheduleMinutes().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule minute deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule minute");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	@Override
	public String deleteScheduleAttachment(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleAttachment> list = committeeSchedule.getCommitteeScheduleAttachments();
					List<CommitteeScheduleAttachment> updatedlist = new ArrayList<CommitteeScheduleAttachment>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleAttachment attachment : list) {
						if (attachment.getCommScheduleAttachId().equals(scheduleVo.getCommScheduleAttachId())) {
							updatedlist.remove(attachment);
						}
					}
					committeeSchedule.getCommitteeScheduleAttachments().clear();
					committeeSchedule.getCommitteeScheduleAttachments().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule attachment deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule attachment");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	@Override
	public String addScheduleAttachment(MultipartFile[] files, String formDataJSON) {
		ScheduleVo scheduleVo = new ScheduleVo();
		try {
			ObjectMapper mapper = new ObjectMapper();
			ScheduleVo jsonObj = mapper.readValue(formDataJSON, ScheduleVo.class);
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(jsonObj.getScheduleId());
			CommitteeScheduleAttachment newAttachment = jsonObj.getNewCommitteeScheduleAttachment();
			List<CommitteeScheduleAttachment> attachments = new ArrayList<CommitteeScheduleAttachment>();
			for (int i = 0; i < files.length; i++) {
				CommitteeScheduleAttachment committeeScheduleAttachment = new CommitteeScheduleAttachment();
				committeeScheduleAttachment.setAttachmentType(newAttachment.getAttachmentType());
				committeeScheduleAttachment.setCommitteeSchedule(committeeSchedule);
				committeeScheduleAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
				committeeScheduleAttachment.setDescription(newAttachment.getDescription());
				committeeScheduleAttachment.setUpdateTimestamp(newAttachment.getUpdateTimestamp());
				committeeScheduleAttachment.setUpdateUser(newAttachment.getUpdateUser());
				committeeScheduleAttachment.setAttachment(files[i].getBytes());
				committeeScheduleAttachment.setFileName(files[i].getOriginalFilename());
				committeeScheduleAttachment.setMimeType(files[i].getContentType());
				committeeScheduleAttachment = scheduleDao.addScheduleAttachment(committeeScheduleAttachment);
				attachments.add(committeeScheduleAttachment);
			}
			committeeSchedule.getCommitteeScheduleAttachments().addAll(attachments);
			committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
			scheduleVo.setCommitteeSchedule(committeeSchedule);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	@Override
	public String deleteScheduleAttendance(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleAttendance> list = committeeSchedule.getCommitteeScheduleAttendances();
					List<CommitteeScheduleAttendance> updatedlist = new ArrayList<CommitteeScheduleAttendance>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleAttendance attachment : list) {
						if (attachment.getCommitteeScheduleAttendanceId().equals(scheduleVo.getCommScheduleAttendanceId())) {
							updatedlist.remove(attachment);
						}
					}
					committeeSchedule.getCommitteeScheduleAttendances().clear();
					committeeSchedule.getCommitteeScheduleAttendances().addAll(updatedlist);
					scheduleVo.setCommitteeSchedule(committeeSchedule);
				}
			}
			committee = committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule attendance deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule attendance");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	@Override
	public String updateScheduleAttachment(ScheduleVo scheduleVo) {
		String response = "";
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		CommitteeScheduleAttachment scheduleAttachment = scheduleVo.getNewCommitteeScheduleAttachment();
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
				List<CommitteeScheduleAttachment> attachments = committeeSchedule.getCommitteeScheduleAttachments();
				for (CommitteeScheduleAttachment attachment : attachments) {
					if (attachment.getCommScheduleAttachId().equals(scheduleAttachment.getCommScheduleAttachId())) {
						attachment.setDescription(scheduleAttachment.getDescription());
					}
				}
				scheduleVo.setCommitteeSchedule(committeeSchedule);
			}
		}
		committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAttachment(Integer attachmentId) {
		CommitteeScheduleAttachment attachment = scheduleDao.fetchAttachmentById(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] data = attachment.getAttachment();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
			String filename = attachment.getFileName();
			headers.setContentDispositionFormData(filename, filename);
			headers.setContentLength(data.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentData;
	}

	@Override
	public String updateCommitteeScheduleMinute(ScheduleVo vo) {
		String response = "";
		Committee committee = committeeDao.fetchCommitteeById(vo.getCommitteeId());
		List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
		CommitteeScheduleMinutes scheduleMinutes = vo.getNewCommitteeScheduleMinute();
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			if (committeeSchedule.getScheduleId().equals(vo.getScheduleId())) {
				List<CommitteeScheduleMinutes> minutes = committeeSchedule.getCommitteeScheduleMinutes();
				for (CommitteeScheduleMinutes minute : minutes) {
					if (minute.getCommScheduleMinutesId().equals(scheduleMinutes.getCommScheduleMinutesId())) {
						minute.setMinuteEntry(scheduleMinutes.getMinuteEntry());
					}
				}
				vo.setCommitteeSchedule(committeeSchedule);
			}
		}
		committeeDao.saveCommittee(committee);
		vo.setCommittee(committee);
		response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

}
