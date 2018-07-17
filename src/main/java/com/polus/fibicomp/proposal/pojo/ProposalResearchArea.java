package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name = "FIBI_SMU_PROPOSAL_RESRCH_AREAS")
public class ProposalResearchArea implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalResearchAreaIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalResearchAreaIdGenerator")
	@Column(name = "RESRCH_AREA_ID")
	private Integer researchAreaId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_SMU_PROP_RESRCH_AREA"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID")
	private Proposal proposal;

	@Column(name = "RESEARCH_AREA_CODE")
	private String researchAreaCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_SMU_PROP_RESRCH_AREA"), name = "RESEARCH_AREA_CODE", referencedColumnName = "RESEARCH_AREA_CODE", insertable = false, updatable = false)
	private ResearchArea researchArea;

	@Column(name = "RESRCH_TYPE_CODE")
	private Integer researchTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_SMU_PROP_RESRCH_AREA"), name = "RESRCH_TYPE_CODE", referencedColumnName = "RESRCH_TYPE_CODE", insertable = false, updatable = false)
	private ProposalResearchType proposalResearchType;

	@Column(name = "EXCELLENCE_AREA_CODE")
	private Integer excellenceAreaCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK4_FIBI_SMU_PROP_RESRCH_AREA"), name = "EXCELLENCE_AREA_CODE", referencedColumnName = "EXCELLENCE_AREA_CODE", insertable = false, updatable = false)
	private ProposalExcellenceArea proposalExcellenceArea;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getResearchAreaId() {
		return researchAreaId;
	}

	public void setResearchAreaId(Integer researchAreaId) {
		this.researchAreaId = researchAreaId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
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

	public Integer getResearchTypeCode() {
		return researchTypeCode;
	}

	public void setResearchTypeCode(Integer researchTypeCode) {
		this.researchTypeCode = researchTypeCode;
	}

	public ProposalResearchType getProposalResearchType() {
		return proposalResearchType;
	}

	public void setProposalResearchType(ProposalResearchType proposalResearchType) {
		this.proposalResearchType = proposalResearchType;
	}

	public Integer getExcellenceAreaCode() {
		return excellenceAreaCode;
	}

	public void setExcellenceAreaCode(Integer excellenceAreaCode) {
		this.excellenceAreaCode = excellenceAreaCode;
	}

	public ProposalExcellenceArea getProposalExcellenceArea() {
		return proposalExcellenceArea;
	}

	public void setProposalExcellenceArea(ProposalExcellenceArea proposalExcellenceArea) {
		this.proposalExcellenceArea = proposalExcellenceArea;
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
