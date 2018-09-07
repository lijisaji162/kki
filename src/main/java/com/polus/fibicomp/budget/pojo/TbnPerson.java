package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "TBN")
public class TbnPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TBN_ID")
	private String tbnId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "JOB_CODE")
	private String jobCode;

	@Column(name = "ACTIVE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "JOB_CODE", referencedColumnName = "JOB_CODE", insertable = false, updatable = false)
	private JobCode jobCodeReference;

	@Transient
	private int quantity;

	public String getTbnId() {
		return tbnId;
	}

	public void setTbnId(String tbnId) {
		this.tbnId = tbnId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public JobCode getJobCodeReference() {
		return jobCodeReference;
	}

	public void setJobCodeReference(JobCode jobCodeReference) {
		this.jobCodeReference = jobCodeReference;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
