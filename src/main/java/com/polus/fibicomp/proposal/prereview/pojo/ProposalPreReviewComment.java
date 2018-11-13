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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "EPS_PROP_PRE_REVIEW_COMMENT")
public class ProposalPreReviewComment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "ProposalPreReviewCommentIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProposalPreReviewCommentIdGenerator")
	@Column(name = "PRE_REVIEW_COMMENT_ID")
	private Integer preReviewCommentId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "EPS_PRP_PRE_REVIEW_COMMENT_FK1"), name = "PRE_REVIEW_ID", referencedColumnName = "PRE_REVIEW_ID")
	private ProposalPreReview proposalPreReview;

	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;

	@Column(name = "REVIEW_COMMENT")
	private String reviewComment;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposalPreReviewComment", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalPreReviewAttachment> proposalPreReviewAttachments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public ProposalPreReviewComment() {
		proposalPreReviewAttachments = new ArrayList<>();
	}

	public Integer getPreReviewCommentId() {
		return preReviewCommentId;
	}

	public void setPreReviewCommentId(Integer preReviewCommentId) {
		this.preReviewCommentId = preReviewCommentId;
	}

	public ProposalPreReview getProposalPreReview() {
		return proposalPreReview;
	}

	public void setProposalPreReview(ProposalPreReview proposalPreReview) {
		this.proposalPreReview = proposalPreReview;
	}

	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
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

	public List<ProposalPreReviewAttachment> getProposalPreReviewAttachments() {
		return proposalPreReviewAttachments;
	}

	public void setProposalPreReviewAttachments(List<ProposalPreReviewAttachment> proposalPreReviewAttachments) {
		this.proposalPreReviewAttachments = proposalPreReviewAttachments;
	}
}
