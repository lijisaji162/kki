package com.polus.fibicomp.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MY_RESEARCH_SUMMARY_V")
public class ResearchSummaryView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "research_type")
	private String researchType;

	@Column(name = "research_count")
	private Long researchCount;

	@Column(name = "total_amount")
	private Integer totalAmount;

	@Column(name = "sort_order")
	private Long sortOrder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResearchType() {
		return researchType;
	}

	public void setResearchType(String researchType) {
		this.researchType = researchType;
	}

	public Long getResearchCount() {
		return researchCount;
	}

	public void setResearchCount(Long researchCount) {
		this.researchCount = researchCount;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
