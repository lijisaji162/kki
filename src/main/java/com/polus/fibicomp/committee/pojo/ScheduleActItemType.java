package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_SCHEDULE_ACT_ITEM_TYPE")
public class ScheduleActItemType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCHEDULE_ACT_ITEM_TYPE_CODE")
	private Integer scheduleActItemTypecode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getScheduleActItemTypecode() {
		return scheduleActItemTypecode;
	}

	public void setScheduleActItemTypecode(Integer scheduleActItemTypecode) {
		this.scheduleActItemTypecode = scheduleActItemTypecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
