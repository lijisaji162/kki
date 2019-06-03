package com.polus.fibicomp.questionnaire.dto;

public class QuestionnaireAnswerDto {
	private Integer moduleItemId;
	private Integer moduleSubItemId;
	private Integer moduleItemCode;
	private Integer questionnaireAnswerId;
	private Integer questionnaireId;
	private Integer questionnaireAnswerHeaderId;
	private Integer answerNumber;
	private String answerLookupCode;
	private String explanation;
	private String updateTimeStamp;
	private String updateUser;
	private Integer questionId;
	private String selectedAnswer;
	private Integer optionNumber;
	private Integer attachmentId;
	private String acType;

	public Integer getQuestionnaireAnswerId() {
		return questionnaireAnswerId;
	}

	public void setQuestionnaireAnswerId(Integer questionnaireAnswerId) {
		this.questionnaireAnswerId = questionnaireAnswerId;
	}

	public Integer getQuestionnaireAnswerHeaderId() {
		return questionnaireAnswerHeaderId;
	}

	public void setQuestionnaireAnswerHeaderId(Integer questionnaireAnswerHeaderId) {
		this.questionnaireAnswerHeaderId = questionnaireAnswerHeaderId;
	}

	public Integer getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
	}

	public String getAnswerLookupCode() {
		return answerLookupCode;
	}

	public void setAnswerLookupCode(String answerLookupCode) {
		this.answerLookupCode = answerLookupCode;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(String updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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

	public String getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	public Integer getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(Integer optionNumber) {
		this.optionNumber = optionNumber;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getModuleItemId() {
		return moduleItemId;
	}

	public void setModuleItemId(Integer moduleItemId) {
		this.moduleItemId = moduleItemId;
	}

	public Integer getModuleSubItemId() {
		return moduleSubItemId;
	}

	public void setModuleSubItemId(Integer moduleSubItemId) {
		this.moduleSubItemId = moduleSubItemId;
	}

	public Integer getModuleItemCode() {
		return moduleItemCode;
	}

	public void setModuleItemCode(Integer moduleItemCode) {
		this.moduleItemCode = moduleItemCode;
	}

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

}
