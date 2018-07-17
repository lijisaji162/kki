package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROTOCOL_ACTION_TYPE")
public class ProtocolActionType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROTOCOL_ACTION_TYPE_CODE")
	private String protocolActionTypecode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TRIGGER_SUBMISSION")
	private char triggerSubmission;

	@Column(name = "TRIGGER_CORRESPONDENCE")
	private char triggerCorrespondence;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "FINAL_ACTION_FOR_BATCH_CORRESP")
	private char finalActionForBatchcorresp;

	public String getProtocolActionTypecode() {
		return protocolActionTypecode;
	}

	public void setProtocolActionTypecode(String protocolActionTypecode) {
		this.protocolActionTypecode = protocolActionTypecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public char getTriggerSubmission() {
		return triggerSubmission;
	}

	public void setTriggerSubmission(char triggerSubmission) {
		this.triggerSubmission = triggerSubmission;
	}

	public char getTriggerCorrespondence() {
		return triggerCorrespondence;
	}

	public void setTriggerCorrespondence(char triggerCorrespondence) {
		this.triggerCorrespondence = triggerCorrespondence;
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

	public char getFinalActionForBatchcorresp() {
		return finalActionForBatchcorresp;
	}

	public void setFinalActionForBatchcorresp(char finalActionForBatchcorresp) {
		this.finalActionForBatchcorresp = finalActionForBatchcorresp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
