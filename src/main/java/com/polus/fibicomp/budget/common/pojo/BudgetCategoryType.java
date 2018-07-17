package com.polus.fibicomp.budget.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;

@Entity
@Table(name = "BUDGET_CATEGORY_TYPE")
public class BudgetCategoryType implements Serializable, Comparable<BudgetCategoryType> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BUDGET_CATEGORY_TYPE_CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SORT_ID")
	private Integer sortId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int compareTo(BudgetCategoryType o) {
		return new CompareToBuilder().append(this.sortId, o.sortId).toComparison();
	}

	public boolean isCategoryParticipantSupport() {
		return getCode().equalsIgnoreCase("S");
	}
}
