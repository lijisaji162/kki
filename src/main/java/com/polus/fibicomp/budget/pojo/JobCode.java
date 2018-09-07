package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JOB_CODE")
public class JobCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "JOB_CODE")
	private String jobCode;

	@Column(name = "JOB_TITLE")
	private String jobTitle;

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
