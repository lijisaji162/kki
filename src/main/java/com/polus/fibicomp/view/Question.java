package com.polus.fibicomp.view;

public class Question {

	private String questionnaireID;

	private String questionId;

	private String questionDesc;

	private String parentQuestionId;

	private String answerType;

	public String getQuestionnaireID() {
		return questionnaireID;
	}

	public void setQuestionnaireID(String questionnaireID) {
		this.questionnaireID = questionnaireID;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionDesc() {
		return questionDesc;
	}

	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}

	public String getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(String parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
}
