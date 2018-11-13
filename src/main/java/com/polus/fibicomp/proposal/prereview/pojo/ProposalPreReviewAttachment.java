package com.polus.fibicomp.proposal.prereview.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "EPS_PROP_PRE_REVIEW_ATTACHMENT")
public class ProposalPreReviewAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "ProposalPreReviewAttachmentIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ProposalPreReviewAttachmentIdGenerator")
	@Column(name = "PRE_REVIEW_ATTACHMENT_ID")
	private Integer preReviewAttachmentId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "PRE_REVIEWER_ATTACHMENT_FK1"), name = "PRE_REVIEW_COMMENT_ID", referencedColumnName = "PRE_REVIEW_COMMENT_ID")
	private ProposalPreReviewComment proposalPreReviewComment;

	@Column(name = "PRE_REVIEW_ID")
	private Integer preReviewId;

	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Lob
	@Column(name = "ATTACHMENT")
	private byte[] attachment;

	@Column(name = "MIME_TYPE")
	private String mimeType;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getPreReviewAttachmentId() {
		return preReviewAttachmentId;
	}

	public void setPreReviewAttachmentId(Integer preReviewAttachmentId) {
		this.preReviewAttachmentId = preReviewAttachmentId;
	}

	public ProposalPreReviewComment getProposalPreReviewComment() {
		return proposalPreReviewComment;
	}

	public void setProposalPreReviewComment(ProposalPreReviewComment proposalPreReviewComment) {
		this.proposalPreReviewComment = proposalPreReviewComment;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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
