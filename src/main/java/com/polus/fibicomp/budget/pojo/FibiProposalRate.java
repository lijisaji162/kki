package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_PROPOSAL_RATES")
public class FibiProposalRate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "fibiProposalRateIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "fibiProposalRateIdGenerator")
	@Column(name = "PROPOSAL_RATE_ID")
	private Integer proposalRateId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_PROPOSAL_RATE"), name = "BUDGET_HEADER_ID", referencedColumnName = "BUDGET_HEADER_ID")
	private BudgetHeader budgetHeader;

	@Column(name = "RATE_CLASS_CODE")
	private String rateClassCode;

	@Column(name = "RATE_TYPE_CODE")
	private String rateTypeCode;

	@Column(name = "FISCAL_YEAR")
	private String fiscalYear;

	@Column(name = "ON_OFF_CAMPUS_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean onOffCampusFlag;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "INSTITUTE_RATE", precision = 2)
	private BigDecimal instituteRate;

	@Column(name = "APPLICABLE_RATE", precision = 2)
	private BigDecimal applicableRate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getProposalRateId() {
		return proposalRateId;
	}

	public void setProposalRateId(Integer proposalRateId) {
		this.proposalRateId = proposalRateId;
	}

	public String getRateClassCode() {
		return rateClassCode;
	}

	public void setRateClassCode(String rateClassCode) {
		this.rateClassCode = rateClassCode;
	}

	public String getRateTypeCode() {
		return rateTypeCode;
	}

	public void setRateTypeCode(String rateTypeCode) {
		this.rateTypeCode = rateTypeCode;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public Boolean getOnOffCampusFlag() {
		return onOffCampusFlag;
	}

	public void setOnOffCampusFlag(Boolean onOffCampusFlag) {
		this.onOffCampusFlag = onOffCampusFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getInstituteRate() {
		return instituteRate;
	}

	public void setInstituteRate(BigDecimal instituteRate) {
		this.instituteRate = instituteRate;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public BigDecimal getApplicableRate() {
		return applicableRate;
	}

	public void setApplicableRate(BigDecimal applicableRate) {
		this.applicableRate = applicableRate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BudgetHeader getBudgetHeader() {
		return budgetHeader;
	}

	public void setBudgetHeader(BudgetHeader budgetHeader) {
		this.budgetHeader = budgetHeader;
	}
}
