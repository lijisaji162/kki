package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE_ATTENDANCE")
public class CommitteeScheduleAttendance implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "attendanceIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "attendanceIdGererator")
	@Column(name = "COMM_SCHEDULE_ATTENDANCE_ID", updatable = false, nullable = false)
	private Integer committeeScheduleAttendanceId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_ATTENDANCE"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "GUEST_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean guestFlag;

	@Column(name = "ALTERNATE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean alternateFlag;

	@Column(name = "ALTERNATE_FOR")
	private String alternateFor;

	@Column(name = "NON_EMPLOYEE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean nonEmployeeFlag;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "MEMBER_PRESENT")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean memberPresent;

	@Transient
	private String roleName;

	@Transient
	private boolean guestMemberActive;

	public Integer getCommitteeScheduleAttendanceId() {
		return committeeScheduleAttendanceId;
	}

	public void setCommitteeScheduleAttendanceId(Integer committeeScheduleAttendanceId) {
		this.committeeScheduleAttendanceId = committeeScheduleAttendanceId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAlternateFor() {
		return alternateFor;
	}

	public void setAlternateFor(String alternateFor) {
		this.alternateFor = alternateFor;
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

	public Boolean getGuestFlag() {
		return guestFlag;
	}

	public void setGuestFlag(Boolean guestFlag) {
		this.guestFlag = guestFlag;
	}

	public Boolean getAlternateFlag() {
		return alternateFlag;
	}

	public void setAlternateFlag(Boolean alternateFlag) {
		this.alternateFlag = alternateFlag;
	}

	public Boolean getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(Boolean nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isGuestMemberActive() {
		return guestMemberActive;
	}

	public void setGuestMemberActive(boolean guestMemberActive) {
		this.guestMemberActive = guestMemberActive;
	}

	public Boolean getMemberPresent() {
		return memberPresent;
	}

	public void setMemberPresent(Boolean memberPresent) {
		this.memberPresent = memberPresent;
	}

}
