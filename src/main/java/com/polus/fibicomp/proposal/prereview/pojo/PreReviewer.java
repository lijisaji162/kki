package com.polus.fibicomp.proposal.prereview.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EPS_REVIEWERS")
public class PreReviewer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EPS_REVIEWERS_ID")
	private Integer reviewerId;

	@Column(name = "PRE_REVIEW_TYPE_CODE")
	private String reviewTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "EPS_REVIEWERS_FK1"), name = "PRE_REVIEW_TYPE_CODE", referencedColumnName = "PRE_REVIEW_TYPE_CODE", insertable = false, updatable = false)
	private PreReviewType preReviewType;

	@Column(name = "REVIEWER_PERSON_ID")
	private String reviewerPersonId;

	@Column(name = "REVIEWER_FULLNAME")
	private String reviewerFullName;

	@Column(name = "REVIEWER_EMAIL")
	private String reviewerEmail;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
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

	public String getReviewerEmail() {
		return reviewerEmail;
	}

	public void setReviewerEmail(String reviewerEmail) {
		this.reviewerEmail = reviewerEmail;
	}
}
