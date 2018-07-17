package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "FIBI_COMM_SCHEDULE_ATTACHMENTS")
public class CommitteeScheduleAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "attachmentIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "attachmentIdGererator")
	@Column(name = "COMM_SCHEDULE_ATTACH_ID", updatable = false, nullable = false)
	private Integer commScheduleAttachId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_SCHEDULE_ATTACHMENT"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID")
	private CommitteeSchedule committeeSchedule;

	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_SCHE_ATTACH_TYPE"), name = "ATTACHMENT_TYPE_CODE", referencedColumnName = "ATTACHMENT_TYPE_CODE", insertable = false, updatable = false)
	private CommitteeScheduleAttachType attachmentType;

	@Column(name = "ATTACHMENT_ID")
	private Integer attachmentId;

	@Column(name = "ATTACHMENT_TYPE_CODE")
	private Integer attachmentTypeCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "ATTACHMENT")
	private byte[] attachment;

	@Column(name = "MIME_TYPE")
	private String mimeType;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommScheduleAttachId() {
		return commScheduleAttachId;
	}

	public void setCommScheduleAttachId(Integer commScheduleAttachId) {
		this.commScheduleAttachId = commScheduleAttachId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getAttachmentTypeCode() {
		return attachmentTypeCode;
	}

	public void setAttachmentTypeCode(Integer attachmentTypeCode) {
		this.attachmentTypeCode = attachmentTypeCode;
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

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public CommitteeScheduleAttachType getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(CommitteeScheduleAttachType attachmentType) {
		this.attachmentType = attachmentType;
	}
}
