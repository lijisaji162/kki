package com.polus.fibicomp.questionnaire.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionnaireQuestionDto {
	private Integer questionId;
	private Integer questionNumber;
	private Integer questionVersionNumber;
	private String question;
	private String description;
	private String helpLink;
	private String answerType;
	private Integer answerLength;
	private Integer numofAnswers;
	private String lookUpType;
	private String lookUpName;
	private String lookUpFiled;
	private String groupName;
	private String groupLabel;
	private String hasCondition;
	private Boolean showQuestion;
	private Boolean completionFlag;
//	private String selectedAnswer;
	private Integer questionnaireId;
	private Integer attachmentId;
	private Integer questionnaireAnswerId;
	private Integer questionnaireAnswerHeaderId;
	private Integer answerNumber;
	private String answerLookupCode;
	private String explanation;
	private String updateTimeStamp;
	private String updateUser;
	private String acType;
	private ArrayList<AnswerDto> answer;
	private HashMap<Integer, String> selectedAnswer;

	public void setSelectedAnswer(HashMap<Integer, String> selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	public ArrayList<AnswerDto> getAnswer() {
		return answer;
	}

	public void setAnswer(ArrayList<AnswerDto> answer) {
		this.answer = answer;
	}

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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(String helpLink) {
		this.helpLink = helpLink;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public Integer getAnswerLength() {
		return answerLength;
	}

	public void setAnswerLength(Integer answerLength) {
		this.answerLength = answerLength;
	}

	public Integer getNumofAnswers() {
		return numofAnswers;
	}

	public void setNumofAnswers(Integer numofAnswers) {
		this.numofAnswers = numofAnswers;
	}

	public String getLookUpType() {
		return lookUpType;
	}

	public void setLookUpType(String lookUpType) {
		this.lookUpType = lookUpType;
	}

	public String getLookUpName() {
		return lookUpName;
	}

	public void setLookUpName(String lookUpName) {
		this.lookUpName = lookUpName;
	}

	public String getLookUpFiled() {
		return lookUpFiled;
	}

	public void setLookUpFiled(String lookUpFiled) {
		this.lookUpFiled = lookUpFiled;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getHasCondition() {
		return hasCondition;
	}

	public void setHasCondition(String hasCondition) {
		this.hasCondition = hasCondition;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Boolean getShowQuestion() {
		return showQuestion;
	}

	public void setShowQuestion(Boolean showQuestion) {
		this.showQuestion = showQuestion;
	}

	public Boolean getCompletionFlag() {
		return completionFlag;
	}

	public void setCompletionFlag(Boolean completionFlag) {
		this.completionFlag = completionFlag;
	}

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	/*
	 * public String getSelectedAnswer() { return selectedAnswer; }
	 * 
	 * public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer =
	 * selectedAnswer; }
	 */
	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getGroupLabel() {
		return groupLabel;
	}

	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}
}
