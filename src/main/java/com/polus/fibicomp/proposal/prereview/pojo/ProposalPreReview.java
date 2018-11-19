package com.polus.fibicomp.proposal.prereview.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "EPS_PROP_PRE_REVIEW")
public class ProposalPreReview implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "ProposalPreReviewIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProposalPreReviewIdGenerator")
	@Column(name = "PRE_REVIEW_ID")
	private Integer preReviewId;

	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;

	@Column(name = "PRE_REVIEW_TYPE_CODE")
	private String reviewTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "EPS_PROP_PRE_REVIEW_FK1"), name = "PRE_REVIEW_TYPE_CODE", referencedColumnName = "PRE_REVIEW_TYPE_CODE", insertable = false, updatable = false)
	private PreReviewType preReviewType;

	@Column(name = "PRE_REVIEW_STATUS_CODE")
	private String reviewStatusCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "EPS_PROP_PRE_REVIEW_FK2"), name = "PRE_REVIEW_STATUS_CODE", referencedColumnName = "PRE_REVIEW_STATUS_CODE", insertable = false, updatable = false)
	private PreReviewStatus preReviewStatus;

	@Column(name = "REVIEWER_PERSON_ID")
	private String reviewerPersonId;

	@Column(name = "REVIEWER_FULLNAME")
	private String reviewerFullName;

	@Column(name = "REVIEWER_EMAIL")
	private String reviewerEmailAddress;

	@Column(name = "REQUESTOR_PERSON_ID")
	private String requestorPersonId;

	@Column(name = "REQUESTOR_FULLNAME")
	private String requestorFullName;

	@Column(name = "REQUESTOR_EMAIL")
	private String requestorEmailAddress;

	@Column(name = "REQUESTOR_COMMENT")
	private String requestorComment;

	@Column(name = "REQUEST_DATE")
	private Timestamp requestDate;

	@Column(name = "COMPLETION_DATE")
	private Timestamp completionDate;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposalPreReview", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalPreReviewComment> proposalPreReviewComments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public ProposalPreReview() {
		proposalPreReviewComments = new ArrayList<>();
	}

	public Integer getPreReviewId() {
		return preReviewId;
	}

	public void setPreReviewId(Integer preReviewId) {
		this.preReviewId = preReviewId;
	}

	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public String getReviewTypeCode() {
		return reviewTypeCode;
	}

	public void setReviewTypeCode(String reviewTypeCode) {
		this.reviewTypeCode = reviewTypeCode;
	}

	public PreReviewType getPreReviewType() {
		return preReviewType;
	}

	public void setPreReviewType(PreReviewType preReviewType) {
		this.preReviewType = preReviewType;
	}

	public String getReviewStatusCode() {
		return reviewStatusCode;
	}

	public void setReviewStatusCode(String reviewStatusCode) {
		this.reviewStatusCode = reviewStatusCode;
	}

	public PreReviewStatus getPreReviewStatus() {
		return preReviewStatus;
	}

	public void setPreReviewStatus(PreReviewStatus preReviewStatus) {
		this.preReviewStatus = preReviewStatus;
	}

	public String getReviewerPersonId() {
		return reviewerPersonId;
	}

	public void setReviewerPersonId(String reviewerPersonId) {
		this.reviewerPersonId = reviewerPersonId;
	}

	public String getReviewerFullName() {
		return reviewerFullName;
	}

	public void setReviewerFullName(String reviewerFullName) {
		this.reviewerFullName = reviewerFullName;
	}

	public String getRequestorPersonId() {
		return requestorPersonId;
	}

	public void setRequestorPersonId(String requestorPersonId) {
		this.requestorPersonId = requestorPersonId;
	}

	public String getRequestorFullName() {
		return requestorFullName;
	}

	public void setRequestorFullName(String requestorFullName) {
		this.requestorFullName = requestorFullName;
	}

	public String getRequestorComment() {
		return requestorComment;
	}

	public void setRequestorComment(String requestorComment) {
		this.requestorComment = requestorComment;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Timestamp completionDate) {
		this.completionDate = completionDate;
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

	public List<ProposalPreReviewComment> getProposalPreReviewComments() {
		return proposalPreReviewComments;
	}

	public void setProposalPreReviewComments(List<ProposalPreReviewComment> proposalPreReviewComments) {
		this.proposalPreReviewComments = proposalPreReviewComments;
	}

	public String getReviewerEmailAddress() {
		return reviewerEmailAddress;
	}

	public void setReviewerEmailAddress(String reviewerEmailAddress) {
		this.reviewerEmailAddress = reviewerEmailAddress;
	}

	public String getRequestorEmailAddress() {
		return requestorEmailAddress;
	}

	public void setRequestorEmailAddress(String requestorEmailAddress) {
		this.requestorEmailAddress = requestorEmailAddress;
	}

}
