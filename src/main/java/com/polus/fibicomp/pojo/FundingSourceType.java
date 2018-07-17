package com.polus.fibicomp.pojo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FUNDING_SOURCE_TYPE")
public class FundingSourceType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "FUNDING_SOURCE_TYPE_CODE", updatable = false, nullable = false)
	private String fundingSourceTypeCode;

    @Column(name = "DESCRIPTION")
	private String description;

    @Column(name = "FUNDING_SOURCE_TYPE_FLAG")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean fundingSourceTypeFlag;

	public FundingSourceType() {
	}

	public String getFundingSourceTypeCode() {
		return fundingSourceTypeCode;
	}

	public void setFundingSourceTypeCode(String fundingSourceTypeCode) {
		this.fundingSourceTypeCode = fundingSourceTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getFundingSourceTypeFlag() {
		return fundingSourceTypeFlag;
	}

	public void setFundingSourceTypeFlag(boolean fundingSourceTypeFlag) {
		this.fundingSourceTypeFlag = fundingSourceTypeFlag;
	}

}
