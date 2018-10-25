package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "NARRATIVE_STATUS")
public class NarrativeStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NARRATIVE_STATUS_CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * Determine if two NarrativeStatuses have the same values.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof NarrativeStatus) {
			NarrativeStatus other = (NarrativeStatus) obj;
			return StringUtils.equals(this.code, other.code) && StringUtils.equals(this.description, other.description);
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
