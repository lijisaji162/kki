package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "INSTITUTE_RATES")
public class InstituteRate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INSTITUTE_RATE_ID")
	private Long id;

	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name="ACTIVITY_TYPE_CODE", referencedColumnName="ACTIVITY_TYPE_CODE", insertable = false, updatable = false)
    private ActivityType activityType;

	@Column(name = "FISCAL_YEAR")
	private String fiscalYear;

	@Column(name = "ON_OFF_CAMPUS_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean onOffCampusFlag;

	@Column(name = "RATE_CLASS_CODE")
	private String rateClassCode;

	@Column(name = "RATE_TYPE_CODE")
	private String rateTypeCode;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name="RATE_CLASS_CODE", referencedColumnName="RATE_CLASS_CODE", insertable = false, updatable = false)
    private RateClass rateClass;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumns({ @JoinColumn(name = "RATE_CLASS_CODE", referencedColumnName = "RATE_CLASS_CODE", insertable = false, updatable = false), @JoinColumn(name = "RATE_TYPE_CODE", referencedColumnName = "RATE_TYPE_CODE", insertable = false, updatable = false) })
    private RateType rateType;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "RATE", precision = 10, scale = 2)
	private BigDecimal instituteRate;

	@Column(name = "UNIT_NUMBER")
	private String unitNumber;

	@Column(name = "ACTIVE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
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

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
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

	public RateClass getRateClass() {
		return rateClass;
	}

	public void setRateClass(RateClass rateClass) {
		this.rateClass = rateClass;
	}

	public RateType getRateType() {
		return rateType;
	}

	public void setRateType(RateType rateType) {
		this.rateType = rateType;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
}
