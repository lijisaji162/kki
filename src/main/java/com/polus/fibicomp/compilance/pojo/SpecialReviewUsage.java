package com.polus.fibicomp.compilance.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "SPECIAL_REVIEW_USAGE")
public class SpecialReviewUsage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SPECIAL_REVIEW_USAGE_ID")
	private Long specialReviewUsageId;

	@Column(name = "SPECIAL_REVIEW_CODE")
	private String specialReviewTypeCode;

	@Column(name = "MODULE_CODE")
	private String moduleCode;

	@Column(name = "GLOBAL_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean global;

	@Column(name = "ACTIVE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	public Long getSpecialReviewUsageId() {
		return specialReviewUsageId;
	}

	public void setSpecialReviewUsageId(Long specialReviewUsageId) {
		this.specialReviewUsageId = specialReviewUsageId;
	}

	public String getSpecialReviewTypeCode() {
		return specialReviewTypeCode;
	}

	public void setSpecialReviewTypeCode(String specialReviewTypeCode) {
		this.specialReviewTypeCode = specialReviewTypeCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
