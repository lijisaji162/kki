package com.polus.fibicomp.grantcall.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.committee.pojo.ResearchArea;

@Entity
@Table(name = "FIBI_GRANT_CALL_RESEARCH_AREAS")
public class GrantCallResearchArea implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "grantResearchAreaIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "grantResearchAreaIdGenerator")
	@Column(name = "GRANT_RESEARCH_AREAS_ID", updatable = false, nullable = false)
	private Integer grantResearchAreaId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_GRANT_CALL_RSRCH_AREA"), name = "GRANT_HEADER_ID", referencedColumnName = "GRANT_HEADER_ID")
	private GrantCall grantCall;

	@Column(name = "RESEARCH_AREA_CODE")
	private String researchAreaCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_GRANT_CALL_RSRCH_AREA"), name = "RESEARCH_AREA_CODE", referencedColumnName = "RESEARCH_AREA_CODE", insertable = false, updatable = false)
	private ResearchArea researchArea;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getGrantResearchAreaId() {
		return grantResearchAreaId;
	}

	public void setGrantResearchAreaId(Integer grantResearchAreaId) {
		this.grantResearchAreaId = grantResearchAreaId;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public String getResearchAreaCode() {
		return researchAreaCode;
	}

	public void setResearchAreaCode(String researchAreaCode) {
		this.researchAreaCode = researchAreaCode;
	}

	public ResearchArea getResearchArea() {
		return researchArea;
	}

	public void setResearchArea(ResearchArea researchArea) {
		this.researchArea = researchArea;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
