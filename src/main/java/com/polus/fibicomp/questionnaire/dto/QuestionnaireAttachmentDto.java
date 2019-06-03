package com.polus.fibicomp.questionnaire.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class QuestionnaireAttachmentDto {
	private Integer attachmentId;
	private Integer requestId;
	private Integer actionLogId;
	private Integer attachmentTypeCode;
	private String attachmentType;
	private Integer attachmentNumber;
	private String isFinal;
	private Integer templeteTypeCode;
	private String templeteType;
	@JsonIgnore
	private byte[] attachment;
	private String description;
	private String fileName;
	private String contentType;
	private String updateUser;
	private String updateTimeStamp;
	private Integer questionId;
	private String acType;

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Integer getActionLogId() {
		return actionLogId;
	}

	public void setActionLogId(Integer actionLogId) {
		this.actionLogId = actionLogId;
	}

	public Integer getAttachmentTypeCode() {
		return attachmentTypeCode;
	}

	public void setAttachmentTypeCode(Integer attachmentTypeCode) {
		this.attachmentTypeCode = attachmentTypeCode;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getAttachmentNumber() {
		return attachmentNumber;
	}

	public void setAttachmentNumber(Integer attachmentNumber) {
		this.attachmentNumber = attachmentNumber;
	}

	public String getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(String isFinal) {
		this.isFinal = isFinal;
	}

	public Integer getTempleteTypeCode() {
		return templeteTypeCode;
	}

	public void setTempleteTypeCode(Integer templeteTypeCode) {
		this.templeteTypeCode = templeteTypeCode;
	}

	public String getTempleteType() {
		return templeteType;
	}

	public void setTempleteType(String templeteType) {
		this.templeteType = templeteType;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(String updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

}
