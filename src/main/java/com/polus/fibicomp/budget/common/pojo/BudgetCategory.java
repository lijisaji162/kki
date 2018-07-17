package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BUDGET_CATEGORY")
public class BudgetCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BUDGET_CATEGORY_CODE")
	private String code;

	@Column(name = "CATEGORY_TYPE")
	private String budgetCategoryTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "CATEGORY_TYPE", referencedColumnName = "BUDGET_CATEGORY_TYPE_CODE", insertable = false, updatable = false)
	private BudgetCategoryType budgetCategoryType;

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
}
