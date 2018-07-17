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
@Table(name = "BATCH_CORRESPONDENCE")
public class BatchCorrespondence implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BATCH_CORRESPONDENCE_TYPE_CODE")
	private String batchCorrespondencetypecode;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_BATCH_CORRESPONDENCE"), name = "FINAL_ACTION_TYPE_CODE", referencedColumnName = "PROTOCOL_ACTION_TYPE_CODE", insertable = false, updatable = false)
	private ProtocolActionType protocolActionType;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_BATCH_CORRESPONDENCE_2"), name = "FINAL_ACTION_CORRESP_TYPE", referencedColumnName = "PROTO_CORRESP_TYPE_CODE", insertable = false, updatable = false)
	private ProtoCorrespType protoCorrespType;

	@Column(name = "DAYS_TO_EVENT_UI_TEXT")
	private String daysToEventUiText;

	@Column(name = "SEND_CORRESPONDENCE")
	private String sendCorrespondence;

	@Column(name = "FINAL_ACTION_DAY")
	private Integer finalActionDay;

	@Column(name = "FINAL_ACTION_TYPE_CODE")
	private String finalActionTypecode;

	@Column(name = "FINAL_ACTION_CORRESP_TYPE")
	private String finalActionCorresptype;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getBatchCorrespondencetypecode() {
		return batchCorrespondencetypecode;
	}

	public void setBatchCorrespondencetypecode(String batchCorrespondencetypecode) {
		this.batchCorrespondencetypecode = batchCorrespondencetypecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDaysToEventUiText() {
		return daysToEventUiText;
	}

	public void setDaysToEventUiText(String daysToEventUiText) {
		this.daysToEventUiText = daysToEventUiText;
	}

	public String getSendCorrespondence() {
		return sendCorrespondence;
	}

	public void setSendCorrespondence(String sendCorrespondence) {
		this.sendCorrespondence = sendCorrespondence;
	}

	public Integer getFinalActionDay() {
		return finalActionDay;
	}

	public void setFinalActionDay(Integer finalActionDay) {
		this.finalActionDay = finalActionDay;
	}

	public String getFinalActionTypecode() {
		return finalActionTypecode;
	}

	public void setFinalActionTypecode(String finalActionTypecode) {
		this.finalActionTypecode = finalActionTypecode;
	}

	public String getFinalActionCorresptype() {
		return finalActionCorresptype;
	}

	public void setFinalActionCorresptype(String finalActionCorresptype) {
		this.finalActionCorresptype = finalActionCorresptype;
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

	public ProtocolActionType getProtocolActionType() {
		return protocolActionType;
	}

	public void setProtocolActionType(ProtocolActionType protocolActionType) {
		this.protocolActionType = protocolActionType;
	}

	public ProtoCorrespType getProtoCorrespType() {
		return protoCorrespType;
	}

	public void setProtoCorrespType(ProtoCorrespType protoCorrespType) {
		this.protoCorrespType = protoCorrespType;
	}

}
