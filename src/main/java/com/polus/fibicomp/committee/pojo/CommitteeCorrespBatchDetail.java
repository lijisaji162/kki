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
@Table(name = "FIBI_COMM_CORRESP_BATCH_DETAIL")
public class CommitteeCorrespBatchDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CORRESP_BATCH_DETAIL_ID")
	private Integer correspBatchDetailId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_CORRESP_BATCH_DTL"), name = "CORRESP_BATCH_ID", referencedColumnName = "CORRESP_BATCH_ID")
	private CommitteeCorrespBatch committeeCorrespBatch;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "ACTION_ID")
	private Integer actionId;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCorrespBatchDetailId() {
		return correspBatchDetailId;
	}

	public void setCorrespBatchDetailId(Integer correspBatchDetailId) {
		this.correspBatchDetailId = correspBatchDetailId;
	}

	public CommitteeCorrespBatch getCommitteeCorrespBatch() {
		return committeeCorrespBatch;
	}

	public void setCommitteeCorrespBatch(CommitteeCorrespBatch committeeCorrespBatch) {
		this.committeeCorrespBatch = committeeCorrespBatch;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
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

}
