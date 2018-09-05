package com.polus.fibicomp.compilance.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SP_REV_APPROVAL_TYPE")
public class SpecialReviewApprovalType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPROVAL_TYPE_CODE")
	private String approvalTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	public String getApprovalTypeCode() {
		return approvalTypeCode;
	}

	public void setApprovalTypeCode(String approvalTypeCode) {
		this.approvalTypeCode = approvalTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
