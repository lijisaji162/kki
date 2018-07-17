package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_MINUTE_ENTRY_TYPE")
public class MinuteEntryType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MINUTE_ENTRY_TYPE_CODE")
	private Integer minuteEntryTypecode;

	@Column(name = "SORT_ID")
	private Integer sortId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getMinuteEntryTypecode() {
		return minuteEntryTypecode;
	}

	public void setMinuteEntryTypecode(Integer minuteEntryTypecode) {
		this.minuteEntryTypecode = minuteEntryTypecode;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
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
