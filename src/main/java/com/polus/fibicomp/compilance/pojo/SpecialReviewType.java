package com.polus.fibicomp.compilance.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SPECIAL_REVIEW")
public class SpecialReviewType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SPECIAL_REVIEW_CODE")
	private String specialReviewTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SORT_ID")
	private Integer sortId;

	public String getSpecialReviewTypeCode() {
		return specialReviewTypeCode;
	}

	public void setSpecialReviewTypeCode(String specialReviewTypeCode) {
		this.specialReviewTypeCode = specialReviewTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
