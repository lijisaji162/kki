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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_BUDGET_HEADER")
public class BudgetHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "budgetHeaderIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "budgetHeaderIdGenerator")
	@Column(name = "BUDGET_HEADER_ID")
	private Integer budgetId;

	@Column(name = "MODULE_ITEM_CODE")
	private Integer moduleItemCode;

	@Column(name = "MODULE_ITEM_KEY")
	private String moduleItemKey;

	@Column(name = "VERSION_NUMBER")
	private Integer versionNumber;

	@Column(name = "MODULE_SEQUENCE_NUMBER")
	private Integer moduleSequenceNumber;

	@Column(name = "OBLIGATED_TOTAL", precision = 10, scale = 3)
	private BigDecimal obligatedTotal;

	@Column(name = "OBLIGATED_CHANGE", precision = 10, scale = 3)
	private BigDecimal obligatedChange;

	@Column(name = "BUDGET_STATUS_CODE")
	private String budgetStatusCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_BUDGET_HEADER"), name = "BUDGET_STATUS_CODE", referencedColumnName = "BUDGET_STATUS_CODE", insertable = false, updatable = false)
	private BudgetStatus budgetStatus;

	@Column(name = "IS_AUTO_CALC")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isAutoCalc = false;

	@Column(name = "BUDGET_TYPE_CODE")
	private String budgetTypeCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_BUDGET_HEADER"), name = "BUDGET_TYPE_CODE", referencedColumnName = "BUDGET_TYPE_CODE", insertable = false, updatable = false)
	private BudgetType budgetType;

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

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "ON_OFF_CAMPUS_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean onOffCampusFlag;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimeStamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "CREATE_USER_NAME")
	private String createUserName;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "UPDATE_USER_NAME")
	private String updateUserName;

	@Column(name = "RATE_CLASS_CODE")
	private String rateClassCode;

	@Column(name = "RATE_TYPE_CODE")
	private String rateTypeCode;

	@Column(name = "ANTICIPATED_TOTAL", precision = 10, scale = 3)
	private BigDecimal anticipatedTotal;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumns({
			@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_BUDGET_HEADER"), name = "RATE_CLASS_CODE", referencedColumnName = "RATE_CLASS_CODE", insertable = false, updatable = false),
			@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_BUDGET_HEADER"), name = "RATE_TYPE_CODE", referencedColumnName = "RATE_TYPE_CODE", insertable = false, updatable = false) })
	private RateType rateType;

	@JsonManagedReference
	@OneToMany(mappedBy = "budget", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<BudgetPeriod> budgetPeriods;

	@JsonManagedReference
	@OneToMany(mappedBy = "budgetHeader", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<FibiProposalRate> proposalRates;

	public BudgetHeader() {
		budgetPeriods = new ArrayList<>();
		proposalRates = new ArrayList<>();
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

	public Integer getModuleSequenceNumber() {
		return moduleSequenceNumber;
	}

	public void setModuleSequenceNumber(Integer moduleSequenceNumber) {
		this.moduleSequenceNumber = moduleSequenceNumber;
	}

	public BigDecimal getObligatedTotal() {
		return obligatedTotal;
	}

	public void setObligatedTotal(BigDecimal obligatedTotal) {
		this.obligatedTotal = obligatedTotal;
	}

	public BigDecimal getObligatedChange() {
		return obligatedChange;
	}

	public void setObligatedChange(BigDecimal obligatedChange) {
		this.obligatedChange = obligatedChange;
	}

	public String getBudgetStatusCode() {
		return budgetStatusCode;
	}

	public void setBudgetStatusCode(String budgetStatusCode) {
		this.budgetStatusCode = budgetStatusCode;
	}

	public BudgetStatus getBudgetStatus() {
		return budgetStatus;
	}

	public void setBudgetStatus(BudgetStatus budgetStatus) {
		this.budgetStatus = budgetStatus;
	}

	public Boolean getIsAutoCalc() {
		return isAutoCalc;
	}

	public void setIsAutoCalc(Boolean isAutoCalc) {
		this.isAutoCalc = isAutoCalc;
	}

	public String getBudgetTypeCode() {
		return budgetTypeCode;
	}

	public void setBudgetTypeCode(String budgetTypeCode) {
		this.budgetTypeCode = budgetTypeCode;
	}

	public BudgetType getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(BudgetType budgetType) {
		this.budgetType = budgetType;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getOnOffCampusFlag() {
		return onOffCampusFlag;
	}

	public void setOnOffCampusFlag(Boolean onOffCampusFlag) {
		this.onOffCampusFlag = onOffCampusFlag;
	}

	public Timestamp getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Timestamp createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public BigDecimal getAnticipatedTotal() {
		return anticipatedTotal;
	}

	public void setAnticipatedTotal(BigDecimal anticipatedTotal) {
		this.anticipatedTotal = anticipatedTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public RateType getRateType() {
		return rateType;
	}

	public void setRateType(RateType rateType) {
		this.rateType = rateType;
	}

	public List<BudgetPeriod> getBudgetPeriods() {
		return budgetPeriods;
	}

	public void setBudgetPeriods(List<BudgetPeriod> budgetPeriods) {
		this.budgetPeriods = budgetPeriods;
	}

	public List<FibiProposalRate> getProposalRates() {
		return proposalRates;
	}

	public void setProposalRates(List<FibiProposalRate> proposalRates) {
		this.proposalRates = proposalRates;
	}

	public Integer getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}

	/*public BudgetPeriod getNewBudgetPeriod() {
		return new BudgetPeriod();
	}

	public BudgetPeriod getBudgetPeriod(int index) {
        while (getBudgetPeriods().size() <= index) {
            BudgetPeriod budgetPeriod = getNewBudgetPeriod();
            budgetPeriod.setBudget(this);
            getBudgetPeriods().add(budgetPeriod);
        }
        return getBudgetPeriods().get(index);
    }*/

}
