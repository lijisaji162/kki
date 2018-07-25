package com.polus.fibicomp.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "EPS_PROP_PERSON_ROLE")
public class ProposalPersonRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROP_PERSON_ROLE_ID")
	private BigDecimal id;

	@Column(name = "PROP_PERSON_ROLE_CODE")
	private String code;

	@Column(name = "SPONSOR_HIERARCHY_NAME")
	private String sponsorHierarchyName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CERTIFICATION_REQUIRED")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean certificationRequired = Boolean.TRUE;

	@Column(name = "READ_ONLY_ROLE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean readOnly;

	@Column(name = "UNIT_DETAILS_REQUIRED")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean unitDetailsRequired = Boolean.TRUE;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSponsorHierarchyName() {
		return sponsorHierarchyName;
	}

	public void setSponsorHierarchyName(String sponsorHierarchyName) {
		this.sponsorHierarchyName = sponsorHierarchyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCertificationRequired() {
		return certificationRequired;
	}

	public void setCertificationRequired(Boolean certificationRequired) {
		this.certificationRequired = certificationRequired;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getUnitDetailsRequired() {
		return unitDetailsRequired;
	}

	public void setUnitDetailsRequired(Boolean unitDetailsRequired) {
		this.unitDetailsRequired = unitDetailsRequired;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}
}
