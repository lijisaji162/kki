package com.polus.fibicomp.compilance.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
//import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.proposal.pojo.Proposal;

@Entity
@Table(name = "FIBI_PROPOSAL_SPECIAL_REVIEW")
public class ProposalSpecialReview implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalSpecialReviewIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalSpecialReviewIdGenerator")
	@Column(name = "PROPOSAL_SPECIAL_REVIEW_ID")
	private Integer id;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_PROPOSAL_SPECIAL_REVIEW"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID")
	private Proposal proposal;

	@Column(name = "SPECIAL_REVIEW_CODE", length = 3)
	private String specialReviewTypeCode;

	/*@Column(name = "SPECIAL_REVIEW_DESCRIPTION")
	private String specialReviewTypeDescription;*/

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_PROPOSAL_SPECIAL_REVIEW"), name = "SPECIAL_REVIEW_CODE", referencedColumnName = "SPECIAL_REVIEW_CODE", insertable = false, updatable = false)
	private SpecialReviewType specialReviewType;

	@Column(name = "APPROVAL_TYPE_CODE", length = 3)
	private String approvalTypeCode;

	/*@Column(name = "APPROVAL_TYPE_DESCRIPTION")
	private String approvalTypeDescription;*/

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_PROPOSAL_SPECIAL_REVIEW"), name = "APPROVAL_TYPE_CODE", referencedColumnName = "APPROVAL_TYPE_CODE", insertable = false, updatable = false)
	private SpecialReviewApprovalType approvalType;

	@Column(name = "PROTOCOL_STATUS_DESCRIPTION")
	private String protocolStatus;

	@Column(name = "PROTOCOL_NUMBER")
	private String protocolNumber;

	@Column(name = "APPLICATION_DATE")
	//@Temporal(TemporalType.DATE)
	private Timestamp applicationDate;

	@Column(name = "APPROVAL_DATE")
	//@Temporal(TemporalType.DATE)
	private Timestamp approvalDate;

	@Column(name = "EXPIRATION_DATE")
	//@Temporal(TemporalType.DATE)
	private Timestamp expirationDate;

	@Column(name = "COMMENTS")
	@Lob
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public String getSpecialReviewTypeCode() {
		return specialReviewTypeCode;
	}

	public void setSpecialReviewTypeCode(String specialReviewTypeCode) {
		this.specialReviewTypeCode = specialReviewTypeCode;
	}

	public String getApprovalTypeCode() {
		return approvalTypeCode;
	}

	public void setApprovalTypeCode(String approvalTypeCode) {
		this.approvalTypeCode = approvalTypeCode;
	}

	public String getProtocolStatus() {
		return protocolStatus;
	}

	public void setProtocolStatus(String protocolStatus) {
		this.protocolStatus = protocolStatus;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Timestamp getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Timestamp applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Timestamp getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public SpecialReviewType getSpecialReviewType() {
		return specialReviewType;
	}

	public void setSpecialReviewType(SpecialReviewType specialReviewType) {
		this.specialReviewType = specialReviewType;
	}

	public SpecialReviewApprovalType getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(SpecialReviewApprovalType approvalType) {
		this.approvalType = approvalType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	/*public String getSpecialReviewTypeDescription() {
		return specialReviewTypeDescription;
	}

	public void setSpecialReviewTypeDescription(String specialReviewTypeDescription) {
		this.specialReviewTypeDescription = specialReviewTypeDescription;
	}

	public String getApprovalTypeDescription() {
		return approvalTypeDescription;
	}

	public void setApprovalTypeDescription(String approvalTypeDescription) {
		this.approvalTypeDescription = approvalTypeDescription;
	}*/

}
