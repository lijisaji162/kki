package com.polus.fibicomp.grantcall.pojo;

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
@Table(name = "FIBI_GRANT_CALL_ELIGIBILITY")
public class GrantCallEligibility implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "grantEligibilityIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "grantEligibilityIdGenerator")
	@Column(name = "GRANT_ELIGIBILITY_ID", updatable = false, nullable = false)
	private Integer grantEligibilityId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_GRANT_CALL_ELGIBILITY"), name = "GRANT_HEADER_ID", referencedColumnName = "GRANT_HEADER_ID")
	private GrantCall grantCall;

	@Column(name = "GRANT_CRITERIA_CODE")
	private Integer grantCriteriaCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_GRANT_CALL_ELGIBILITY"), name = "GRANT_CRITERIA_CODE", referencedColumnName = "GRANT_CRITERIA_CODE", insertable = false, updatable = false)
	private GrantCallCriteria grantCallCriteria;

	@Column(name = "GRANT_ELGBLTY_TYPE_CODE")
	private Integer grantEligibilityTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_GRANT_CALL_ELGIBILITY"), name = "GRANT_ELGBLTY_TYPE_CODE", referencedColumnName = "GRANT_ELGBLTY_TYPE_CODE", insertable = false, updatable = false)
	private GrantCallEligibilityType grantCallEligibilityType;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getGrantEligibilityId() {
		return grantEligibilityId;
	}

	public void setGrantEligibilityId(Integer grantEligibilityId) {
		this.grantEligibilityId = grantEligibilityId;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public Integer getGrantCriteriaCode() {
		return grantCriteriaCode;
	}

	public void setGrantCriteriaCode(Integer grantCriteriaCode) {
		this.grantCriteriaCode = grantCriteriaCode;
	}

	public GrantCallCriteria getGrantCallCriteria() {
		return grantCallCriteria;
	}

	public void setGrantCallCriteria(GrantCallCriteria grantCallCriteria) {
		this.grantCallCriteria = grantCallCriteria;
	}

	public Integer getGrantEligibilityTypeCode() {
		return grantEligibilityTypeCode;
	}

	public void setGrantEligibilityTypeCode(Integer grantEligibilityTypeCode) {
		this.grantEligibilityTypeCode = grantEligibilityTypeCode;
	}

	public GrantCallEligibilityType getGrantCallEligibilityType() {
		return grantCallEligibilityType;
	}

	public void setGrantCallEligibilityType(GrantCallEligibilityType grantCallEligibilityType) {
		this.grantCallEligibilityType = grantCallEligibilityType;
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
