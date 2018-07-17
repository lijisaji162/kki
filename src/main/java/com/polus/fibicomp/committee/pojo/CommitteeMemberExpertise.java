package com.polus.fibicomp.committee.pojo;

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

@Entity
@Table(name = "FIBI_COMM_MEMBER_EXPERTISE")
public class CommitteeMemberExpertise implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "commMemberExpertiseIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "commMemberExpertiseIdGererator")
	@Column(name = "COMM_MEMBER_EXPERTISE_ID", nullable = false, updatable = false)
	private Integer commMemberExpertiseId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBER_EXPERTISE"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID")
	private CommitteeMemberships committeeMemberships;

	@Column(name = "RESEARCH_AREA_CODE")
	private String researchAreaCode;

	@Column(name = "RESEARCH_AREA_DESCRIPTION")
	private String researchAreaDescription;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommMemberExpertiseId() {
		return commMemberExpertiseId;
	}

	public void setCommMemberExpertiseId(Integer commMemberExpertiseId) {
		this.commMemberExpertiseId = commMemberExpertiseId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public String getResearchAreaCode() {
		return researchAreaCode;
	}

	public void setResearchAreaCode(String researchAreaCode) {
		this.researchAreaCode = researchAreaCode;
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

	public String getResearchAreaDescription() {
		return researchAreaDescription;
	}

	public void setResearchAreaDescription(String researchAreaDescription) {
		this.researchAreaDescription = researchAreaDescription;
	}
}
