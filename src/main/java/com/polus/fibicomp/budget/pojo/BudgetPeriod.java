package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_BUDGET_PERIOD")
public class BudgetPeriod implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "budgetPeriodIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "budgetPeriodIdGenerator")
	@Column(name = "BUDGET_PERIOD_ID")
	private Integer budgetPeriodId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_BUDGET_PERIOD"), name = "BUDGET_HEADER_ID", referencedColumnName = "BUDGET_HEADER_ID")
	private BudgetHeader budget;

	@Column(name = "MODULE_ITEM_CODE")
	private Integer moduleItemCode;

	@Column(name = "MODULE_ITEM_KEY")
	private String moduleItemKey;

	@Column(name = "VERSION_NUMBER")
	private Integer versionNumber;

	@Column(name = "BUDGET_PERIOD")
	private Integer budgetPeriod;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "TOTAL_COST", precision = 10, scale = 3)
	private BigDecimal totalCost;

	@Column(name = "TOTAL_DIRECT_COST", precision = 10, scale = 3)
	private BigDecimal totalDirectCost;

	@Column(name = "TOTAL_INDIRECT_COST", precision = 10, scale = 3)
	private BigDecimal totalIndirectCost;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "PERIOD_LABEL")
	private String periodLabel;

	@Column(name = "IS_OBLIGATED_PERIOD")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isObligatedPeriod;

	@JsonManagedReference
	@OneToMany(mappedBy = "period", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<BudgetDetail> budgetDetails;

	public BudgetPeriod() {
		budgetDetails = new ArrayList<>();
	}

	public Integer getBudgetPeriodId() {
		return budgetPeriodId;
	}

	public void setBudgetPeriodId(Integer budgetPeriodId) {
		this.budgetPeriodId = budgetPeriodId;
	}

	public BudgetHeader getBudget() {
		return budget;
	}

	public void setBudget(BudgetHeader budget) {
		this.budget = budget;
	}

	public Integer getModuleItemCode() {
		return moduleItemCode;
	}

	public void setModuleItemCode(Integer moduleItemCode) {
		this.moduleItemCode = moduleItemCode;
	}

	public String getModuleItemKey() {
		return moduleItemKey;
	}

	public void setModuleItemKey(String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BigDecimal getTotalDirectCost() {
		return totalDirectCost;
	}

	public void setTotalDirectCost(BigDecimal totalDirectCost) {
		this.totalDirectCost = totalDirectCost;
	}

	public BigDecimal getTotalIndirectCost() {
		return totalIndirectCost;
	}

	public void setTotalIndirectCost(BigDecimal totalIndirectCost) {
		this.totalIndirectCost = totalIndirectCost;
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

	public String getPeriodLabel() {
		return periodLabel;
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setIsObligatedPeriod(Boolean isObligatedPeriod) {
		this.isObligatedPeriod = isObligatedPeriod;
	}

	public Boolean getIsObligatedPeriod() {
		return isObligatedPeriod;
	}

	public List<BudgetDetail> getBudgetDetails() {
		return budgetDetails;
	}

	public void setBudgetDetails(List<BudgetDetail> budgetDetails) {
		this.budgetDetails = budgetDetails;
	}
}
