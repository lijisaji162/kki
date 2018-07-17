package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACTIVITY_TYPE")
public class ActivityType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ACTIVITY_TYPE_CODE", updatable = false, nullable = false)
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "HIGHER_EDUCATION_FUNCTION_CODE")
	private String higherEducationFunctionCode;

	public String getHigherEducationFunctionCode() {
		return higherEducationFunctionCode;
	}

	public void setHigherEducationFunctionCode(String higherEducationFunctionCode) {
		this.higherEducationFunctionCode = higherEducationFunctionCode;
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
}
