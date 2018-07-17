package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE_MINUTE_DOC")
public class CommitteeScheduleMinuteDoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCHEDULE_MINUTE_DOC_ID")
	private Integer scheduleMinuteDocId;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_MINUTE_DOC"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "MINUTE_NUMBER")
	private Integer minuteNumber;

	@Column(name = "MINUTE_NAME")
	private String minuteName;

	@Column(name = "PDF_STORE")
	private byte[] pdfStore;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getScheduleMinuteDocId() {
		return scheduleMinuteDocId;
	}

	public void setScheduleMinuteDocId(Integer scheduleMinuteDocId) {
		this.scheduleMinuteDocId = scheduleMinuteDocId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getMinuteNumber() {
		return minuteNumber;
	}

	public void setMinuteNumber(Integer minuteNumber) {
		this.minuteNumber = minuteNumber;
	}

	public String getMinuteName() {
		return minuteName;
	}

	public void setMinuteName(String minuteName) {
		this.minuteName = minuteName;
	}

	public byte[] getPdfStore() {
		return pdfStore;
	}

	public void setPdfStore(byte[] pdfStore) {
		this.pdfStore = pdfStore;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
