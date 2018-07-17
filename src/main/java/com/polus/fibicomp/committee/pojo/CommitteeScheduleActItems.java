package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE_ACT_ITEMS")
public class CommitteeScheduleActItems implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "actionItemIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "actionItemIdGererator")
	@Column(name = "COMM_SCHEDULE_ACT_ITEMS_ID", updatable = false, nullable = false)
	private Integer commScheduleActItemsId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_ACT_ITEMS"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@Column(name = "ACTION_ITEM_NUMBER")
	private Integer actionItemNumber;

	@Column(name = "SCHEDULE_ACT_ITEM_TYPE_CODE")
	private Integer scheduleActItemTypecode;

	@Column(name = "ITEM_DESCTIPTION")
	private String itemDescription;

	@Column(name = "ACT_ITEM_TYPE_DESCRIPTION")
	private String scheduleActItemTypeDescription;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommScheduleActItemsId() {
		return commScheduleActItemsId;
	}

	public void setCommScheduleActItemsId(Integer commScheduleActItemsId) {
		this.commScheduleActItemsId = commScheduleActItemsId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getActionItemNumber() {
		return actionItemNumber;
	}

	public void setActionItemNumber(Integer actionItemNumber) {
		this.actionItemNumber = actionItemNumber;
	}

	public Integer getScheduleActItemTypecode() {
		return scheduleActItemTypecode;
	}

	public void setScheduleActItemTypecode(Integer scheduleActItemTypecode) {
		this.scheduleActItemTypecode = scheduleActItemTypecode;
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

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getScheduleActItemTypeDescription() {
		return scheduleActItemTypeDescription;
	}

	public void setScheduleActItemTypeDescription(String scheduleActItemTypeDescription) {
		this.scheduleActItemTypeDescription = scheduleActItemTypeDescription;
	}

}
