package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.committee.schedule.DayOfWeek;
import com.polus.fibicomp.committee.schedule.Time12HrFmt;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE")
public class CommitteeSchedule implements Serializable, Comparable<CommitteeSchedule> {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "scheduleIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "scheduleIdGererator")
	@Column(name = "SCHEDULE_ID", updatable = false, nullable = false)
	private Integer scheduleId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE_2"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@Transient
	private boolean filter = true;

	@Transient
	private Time12HrFmt viewTime;

	@Column(name = "SCHEDULED_DATE")
	private Date scheduledDate;

	@Column(name = "PLACE")
	private String place;

	@Column(name = "TIME")
	private Timestamp time;

	@Column(name = "PROTOCOL_SUB_DEADLINE")
	private Date protocolSubDeadline;

	@Column(name = "SCHEDULE_STATUS_CODE")
	private Integer scheduleStatusCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE"), name = "SCHEDULE_STATUS_CODE", referencedColumnName = "SCHEDULE_STATUS_CODE", insertable = false, updatable = false)
	private ScheduleStatus scheduleStatus;

	@Column(name = "MEETING_DATE")
	private Date meetingDate;

	@Column(name = "START_TIME")
	private Timestamp startTime;

	@Column(name = "END_TIME")
	private Timestamp endTime;

	@Column(name = "AGENDA_PROD_REV_DATE")
	private Date agendaProdRevDate;

	@Column(name = "MAX_PROTOCOLS")
	private Integer maxProtocols;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "AVAILABLE_TO_REVIEWERS")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean availableToReviewers;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeSchedule", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProtocolSubmission> protocolSubmissions;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeSchedule", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeScheduleAttendance> committeeScheduleAttendances;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeSchedule", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeScheduleActItems> committeeScheduleActItems;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeSchedule", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeScheduleMinutes> committeeScheduleMinutes;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeSchedule", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeScheduleAttachment> committeeScheduleAttachments;

	@Transient
	private String dayOfWeek;

	@Transient
	private Time12HrFmt viewStartTime;

	@Transient
	private Time12HrFmt viewEndTime;

	@Transient
	private String committeeId;

	@Transient
	private String committeeName;

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public ScheduleStatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Integer getMaxProtocols() {
		if (maxProtocols == null && getCommittee() != null) {
            maxProtocols = getCommittee().getMaxProtocols();
        }
		return maxProtocols;
	}

	public void setMaxProtocols(Integer maxProtocols) {
		if (maxProtocols == null) {
			maxProtocols = 0;
		}
		this.maxProtocols = maxProtocols;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getProtocolSubDeadline() {
		return protocolSubDeadline;
	}

	public void setProtocolSubDeadline(Date protocolSubDeadline) {
		this.protocolSubDeadline = protocolSubDeadline;
	}

	public Integer getScheduleStatusCode() {
		return scheduleStatusCode;
	}

	public void setScheduleStatusCode(Integer scheduleStatusCode) {
		this.scheduleStatusCode = scheduleStatusCode;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public Timestamp getStartTime() {
		if (startTime == null || startTime.getTime() == 0) {
			java.util.Date dt = new java.util.Date(0);
			dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
			if (viewStartTime != null) {
				dt = DateUtils.addMinutes(dt, viewStartTime.findMinutes());
			}
			this.startTime = new Timestamp(dt.getTime());
		}
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		if (endTime == null || endTime.getTime() == 0) {
			java.util.Date dt = new java.util.Date(0); // set to 1969/12/31 19:00 ?
			dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH); // force it to 1970-01-01
			if (viewEndTime != null) {
				dt = DateUtils.addMinutes(dt, viewEndTime.findMinutes());
			}
			this.endTime = new Timestamp(dt.getTime());
		}
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Date getAgendaProdRevDate() {
		return agendaProdRevDate;
	}

	public void setAgendaProdRevDate(Date agendaProdRevDate) {
		this.agendaProdRevDate = agendaProdRevDate;
	}

	public Timestamp getTime() {
		if (this.time != null) {
			java.util.Date dt = new java.util.Date(this.time.getTime());
			dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
			if (viewTime != null) {
				dt = new java.util.Date(0); // 12/31/1969 19:00:00
				dt = DateUtils.round(dt, Calendar.DAY_OF_MONTH);
				dt = DateUtils.addMinutes(dt, viewTime.findMinutes()); // to set it to 1970-01-01
				this.time = new Timestamp(dt.getTime());
			}
		}
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	/**
     * This UI support method to find day Of week from BO's persistent field scheduledDate.
     * @return
     */
    public String getDayOfWeek() {
        Calendar cl = new GregorianCalendar();
        cl.setTime(scheduledDate);
        DayOfWeek dayOfWeek = null;        
        switch (cl.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayOfWeek = DayOfWeek.Sunday;
                break;
            case Calendar.MONDAY:
                dayOfWeek = DayOfWeek.Monday;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = DayOfWeek.Tuesday;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = DayOfWeek.Wednesday;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = DayOfWeek.Thursday;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = DayOfWeek.Friday;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = DayOfWeek.Saturday;
                break;
        }
        this.dayOfWeek = dayOfWeek.name().toUpperCase();
        return this.dayOfWeek;
    }

	@Override
	public int compareTo(CommitteeSchedule other) {
		int compareResult;

		if (getScheduledDate() == null) {
			if (other.getScheduledDate() == null) {
				compareResult = 0;
			} else {
				compareResult = -1;
			}
		} else {
			if (other.getScheduledDate() == null) {
				compareResult = 1;
			} else {
				compareResult = getScheduledDate().compareTo(other.getScheduledDate());
			}
		}
		return compareResult;
	}

	public Time12HrFmt getViewTime() {
		if(null == this.viewTime) {
			this.viewTime = new Time12HrFmt(time);
		}
		return viewTime;
	}

	public void setViewTime(Time12HrFmt viewTime) {
		this.viewTime = viewTime;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Time12HrFmt getViewStartTime() {
		if (null == this.viewStartTime) {
			this.viewStartTime = new Time12HrFmt(startTime);
		}
		return viewStartTime;
	}

	public void setViewStartTime(Time12HrFmt viewStartTime) {
		this.viewStartTime = viewStartTime;
	}

	public Time12HrFmt getViewEndTime() {
		if (null == this.viewEndTime) {
            this.viewEndTime = new Time12HrFmt(endTime);
        }
		return viewEndTime;
	}

	public void setViewEndTime(Time12HrFmt viewEndTime) {
		this.viewEndTime = viewEndTime;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public List<CommitteeScheduleAttendance> getCommitteeScheduleAttendances() {
		return committeeScheduleAttendances;
	}

	public void setCommitteeScheduleAttendances(List<CommitteeScheduleAttendance> committeeScheduleAttendances) {
		this.committeeScheduleAttendances = committeeScheduleAttendances;
	}

	/*for dashboard purpose*/
	public String getCommitteeId() {
		if (this.committee != null) {
			this.committeeId = this.committee.getCommitteeId();
		}
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	/*for dashboard purpose*/
	public String getCommitteeName() {
		if (this.committee != null) {
			this.committeeName = this.committee.getCommitteeName();
		}
		return committeeName;
	}

	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}

	public List<CommitteeScheduleActItems> getCommitteeScheduleActItems() {
		return committeeScheduleActItems;
	}

	public void setCommitteeScheduleActItems(List<CommitteeScheduleActItems> committeeScheduleActItems) {
		this.committeeScheduleActItems = committeeScheduleActItems;
	}

	public List<ProtocolSubmission> getProtocolSubmissions() {
		return protocolSubmissions;
	}

	public void setProtocolSubmissions(List<ProtocolSubmission> protocolSubmissions) {
		this.protocolSubmissions = protocolSubmissions;
	}

	public List<CommitteeScheduleMinutes> getCommitteeScheduleMinutes() {
		return committeeScheduleMinutes;
	}

	public void setCommitteeScheduleMinutes(List<CommitteeScheduleMinutes> committeeScheduleMinutes) {
		this.committeeScheduleMinutes = committeeScheduleMinutes;
	}

	public List<CommitteeScheduleAttachment> getCommitteeScheduleAttachments() {
		return committeeScheduleAttachments;
	}

	public void setCommitteeScheduleAttachments(List<CommitteeScheduleAttachment> committeeScheduleAttachments) {
		this.committeeScheduleAttachments = committeeScheduleAttachments;
	}

	public Boolean getAvailableToReviewers() {
		return availableToReviewers;
	}

	public void setAvailableToReviewers(Boolean availableToReviewers) {
		this.availableToReviewers = availableToReviewers;
	}

}
