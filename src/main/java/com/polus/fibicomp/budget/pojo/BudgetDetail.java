package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
//import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_BUDGET_DETAIL")
public class BudgetDetail implements Serializable, Comparable<BudgetDetail> {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "budgetDetailsIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "budgetDetailsIdGenerator")
	@Column(name = "BUDGET_DETAILS_ID")
	private Integer budgetDetailId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_BUDGET_DETAIL"), name = "BUDGET_PERIOD_ID", referencedColumnName = "BUDGET_PERIOD_ID")
	private BudgetPeriod period;

	@Column(name = "VERSION_NUMBER")
	private Integer versionNumber;

	@Column(name = "BUDGET_PERIOD")
	private Integer budgetPeriod;

	@Column(name = "LINE_ITEM_NUMBER")
	private Integer lineItemNumber;

	@Column(name = "END_DATE")
	//@Temporal(TemporalType.DATE)
	private Timestamp endDate;

	@Column(name = "START_DATE")
	//@Temporal(TemporalType.DATE)
	private Timestamp startDate;

	@Column(name = "BUDGET_CATEGORY_CODE")
	private String budgetCategoryCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_BUDGET_DETAIL"), name = "BUDGET_CATEGORY_CODE", referencedColumnName = "BUDGET_CATEGORY_CODE", insertable = false, updatable = false)
	private BudgetCategory budgetCategory;

	@Column(name = "COST_ELEMENT")
	private String costElementCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_BUDGET_DETAIL"), name = "COST_ELEMENT", referencedColumnName = "COST_ELEMENT", insertable = false, updatable = false)
	private CostElement costElement;

	@Column(name = "LINE_ITEM_DESCRIPTION")
	private String lineItemDescription;

	@Column(name = "LINE_ITEM_COST", precision = 10, scale = 2)
	private BigDecimal lineItemCost;

	@Column(name = "PREVIOUS_LINE_ITEM_COST", precision = 10, scale = 2)
	private BigDecimal prevLineItemCost;

	@Column(name = "BUDGET_JUSTIFICATION")
	private String budgetJustification;

	@Column(name = "IS_SYSTEM_GENRTED_COST_ELEMENT")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isSystemGeneratedCostElement = false;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "ON_OFF_CAMPUS_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean onOffCampusFlag;

	@JsonManagedReference
	@OneToMany(mappedBy = "budgetDetail", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<BudgetDetailCalcAmount> budgetDetailCalcAmounts;

	@JsonManagedReference
	@OneToMany(mappedBy = "budgetDetail", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<BudgetRateAndBase> budgetRateAndBases;

	@Column(name = "SYSTEM_GEN_COST_ELEMENT_TYPE")
	private String systemGeneratedCEType;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "PERSON_TYPE") // E - Employee, N - Non Employee, T - To Be Named
	private String personType;

	@Column(name = "TBN_ID")
	private String tbnId;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK4_FIBI_BUDGET_DETAIL"), name = "TBN_ID", referencedColumnName = "TBN_ID", insertable = false, updatable = false)
	private TbnPerson tbnPerson;

	public BudgetDetail () {
		budgetDetailCalcAmounts = new ArrayList<>();
		budgetRateAndBases = new ArrayList<>();
	}

	public Integer getBudgetDetailId() {
		return budgetDetailId;
	}

	public void setBudgetDetailId(Integer budgetDetailId) {
		this.budgetDetailId = budgetDetailId;
	}

	public BudgetPeriod getPeriod() {
		return period;
	}

	public void setPeriod(BudgetPeriod period) {
		this.period = period;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getBudgetPeriod() {
		return budgetPeriod;
	}

	public void setBudgetPeriod(Integer budgetPeriod) {
		this.budgetPeriod = budgetPeriod;
	}

	public Integer getLineItemNumber() {
		return lineItemNumber;
	}

	public void setLineItemNumber(Integer lineItemNumber) {
		this.lineItemNumber = lineItemNumber;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getBudgetCategoryCode() {
		return budgetCategoryCode;
	}

	public void setBudgetCategoryCode(String budgetCategoryCode) {
		this.budgetCategoryCode = budgetCategoryCode;
	}

	public BudgetCategory getBudgetCategory() {
		return budgetCategory;
	}

	public void setBudgetCategory(BudgetCategory budgetCategory) {
		this.budgetCategory = budgetCategory;
	}

	public String getCostElementCode() {
		return costElementCode;
	}

	public void setCostElementCode(String costElementCode) {
		this.costElementCode = costElementCode;
	}

	public CostElement getCostElement() {
		return costElement;
	}

	public void setCostElement(CostElement costElement) {
		this.costElement = costElement;
	}

	public String getLineItemDescription() {
		return lineItemDescription;
	}

	public void setLineItemDescription(String lineItemDescription) {
		this.lineItemDescription = lineItemDescription;
	}

	public BigDecimal getLineItemCost() {
		return lineItemCost;
	}

	public void setLineItemCost(BigDecimal lineItemCost) {
		this.lineItemCost = lineItemCost;
	}

	public BigDecimal getPrevLineItemCost() {
		return prevLineItemCost;
	}

	public void setPrevLineItemCost(BigDecimal prevLineItemCost) {
		this.prevLineItemCost = prevLineItemCost;
	}

	public String getBudgetJustification() {
		return budgetJustification;
	}

	public void setBudgetJustification(String budgetJustification) {
		this.budgetJustification = budgetJustification;
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

	public Boolean getOnOffCampusFlag() {
		return onOffCampusFlag;
	}

	public void setOnOffCampusFlag(Boolean onOffCampusFlag) {
		this.onOffCampusFlag = onOffCampusFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsSystemGeneratedCostElement() {
		return isSystemGeneratedCostElement;
	}

	public void setIsSystemGeneratedCostElement(Boolean isSystemGeneratedCostElement) {
		this.isSystemGeneratedCostElement = isSystemGeneratedCostElement;
	}

	public List<BudgetDetailCalcAmount> getBudgetDetailCalcAmounts() {
		return budgetDetailCalcAmounts;
	}

	public void setBudgetDetailCalcAmounts(List<BudgetDetailCalcAmount> budgetDetailCalcAmounts) {
		this.budgetDetailCalcAmounts = budgetDetailCalcAmounts;
	}

	public List<BudgetRateAndBase> getBudgetRateAndBases() {
		return budgetRateAndBases;
	}

	public void setBudgetRateAndBases(List<BudgetRateAndBase> budgetRateAndBases) {
		this.budgetRateAndBases = budgetRateAndBases;
	}

	public String getSystemGeneratedCEType() {
		return systemGeneratedCEType;
	}

	public void setSystemGeneratedCEType(String systemGeneratedCEType) {
		this.systemGeneratedCEType = systemGeneratedCEType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public int compareTo(BudgetDetail o) {
		if (getUpdateTimeStamp() == null) {
			return 0;
		}
		return getUpdateTimeStamp().compareTo(o.getUpdateTimeStamp());
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getTbnId() {
		return tbnId;
	}

	public void setTbnId(String tbnId) {
		this.tbnId = tbnId;
	}

	public TbnPerson getTbnPerson() {
		return tbnPerson;
	}

	public void setTbnPerson(TbnPerson tbnPerson) {
		this.tbnPerson = tbnPerson;
	}

}
