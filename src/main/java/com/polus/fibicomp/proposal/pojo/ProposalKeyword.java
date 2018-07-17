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
import com.polus.fibicomp.pojo.ScienceKeyword;

@Entity
@Table(name = "FIBI_SMU_PROPOSAL_KEYWORDS")
public class ProposalKeyword implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalKeywordIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalKeywordIdGenerator")
	@Column(name = "KEYWORD_ID")
	private Integer keywordId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_SMU_PROP_KEYWORDS"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID")
	private Proposal proposal;

	@Column(name = "SCIENCE_KEYWORD_CODE")
	private String scienceKeywordCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_SMU_PROP_KEYWORDS"), name = "SCIENCE_KEYWORD_CODE", referencedColumnName = "SCIENCE_KEYWORD_CODE", insertable = false, updatable = false)
	private ScienceKeyword scienceKeyword;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(Integer keywordId) {
		this.keywordId = keywordId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public String getScienceKeywordCode() {
		return scienceKeywordCode;
	}

	public void setScienceKeywordCode(String scienceKeywordCode) {
		this.scienceKeywordCode = scienceKeywordCode;
	}

	public ScienceKeyword getScienceKeyword() {
		return scienceKeyword;
	}

	public void setScienceKeyword(ScienceKeyword scienceKeyword) {
		this.scienceKeyword = scienceKeyword;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
