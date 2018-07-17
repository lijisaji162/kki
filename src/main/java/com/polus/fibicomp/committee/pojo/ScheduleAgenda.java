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
@Table(name = "FIBI_SCHEDULE_AGENDA")
public class ScheduleAgenda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCHEDULE_AGENDA_ID")
	private Integer SCHEDULE_AGENDA_ID;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE_AGENDA"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "AGENDA_NUMBER")
	private Integer agendaNumber;

	@Column(name = "AGENDA_NAME")
	private Integer agendaName;

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

	public Integer getSCHEDULE_AGENDA_ID() {
		return SCHEDULE_AGENDA_ID;
	}

	public void setSCHEDULE_AGENDA_ID(Integer sCHEDULE_AGENDA_ID) {
		SCHEDULE_AGENDA_ID = sCHEDULE_AGENDA_ID;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getAgendaNumber() {
		return agendaNumber;
	}

	public void setAgendaNumber(Integer agendaNumber) {
		this.agendaNumber = agendaNumber;
	}

	public Integer getAgendaName() {
		return agendaName;
	}

	public void setAgendaName(Integer agendaName) {
		this.agendaName = agendaName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

}
