package com.polus.fibicomp.grantcall.pojo;

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
@Table(name = "FIBI_GRANT_CALL_ATTACHMENTS")
public class GrantCallAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "attachmentIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "attachmentIdGenerator")
	@Column(name = "ATTACHMENT_ID", updatable = false, nullable = false)
	private Integer attachmentId;

	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_GRANT_CALL_ATTACHMNTS"), name = "GRANT_HEADER_ID", referencedColumnName = "GRANT_HEADER_ID")
	private GrantCall grantCall;

	@Column(name = "GRANT_ATTACHMNT_TYPE_CODE")
	private Integer grantAttachmentTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_GRANT_CALL_ATTACHMNTS"), name = "GRANT_ATTACHMNT_TYPE_CODE", referencedColumnName = "GRANT_ATTACHMNT_TYPE_CODE", insertable = false, updatable = false)
	private GrantCallAttachType grantCallAttachType;

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

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public Integer getGrantAttachmentTypeCode() {
		return grantAttachmentTypeCode;
	}

	public void setGrantAttachmentTypeCode(Integer grantAttachmentTypeCode) {
		this.grantAttachmentTypeCode = grantAttachmentTypeCode;
	}

	public GrantCallAttachType getGrantCallAttachType() {
		return grantCallAttachType;
	}

	public void setGrantCallAttachType(GrantCallAttachType grantCallAttachType) {
		this.grantCallAttachType = grantCallAttachType;
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
