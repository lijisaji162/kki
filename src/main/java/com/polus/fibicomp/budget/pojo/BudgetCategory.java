package com.polus.fibicomp.budget.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_BUDGET_CATEGORY")
public class BudgetCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BUDGET_CATEGORY_CODE")
	private String code;

	@Column(name = "BUDGET_CATEGORY_TYPE_CODE")
	private String budgetCategoryTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_BUDGET_CATEGORY"), name = "BUDGET_CATEGORY_TYPE_CODE", referencedColumnName = "BUDGET_CATEGORY_TYPE_CODE", insertable = false, updatable = false)
	private BudgetCategoryType budgetCategoryType;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBudgetCategoryTypeCode() {
		return budgetCategoryTypeCode;
	}

	public void setBudgetCategoryTypeCode(String budgetCategoryTypeCode) {
		this.budgetCategoryTypeCode = budgetCategoryTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BudgetCategoryType getBudgetCategoryType() {
		return budgetCategoryType;
	}

	public void setBudgetCategoryType(BudgetCategoryType budgetCategoryType) {
		this.budgetCategoryType = budgetCategoryType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
}
