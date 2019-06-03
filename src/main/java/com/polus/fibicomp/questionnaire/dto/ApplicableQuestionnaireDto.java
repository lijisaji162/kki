package com.polus.fibicomp.questionnaire.dto;

public class ApplicableQuestionnaireDto {

	private Integer questionnaireId;
	private Integer questionnaireNumber;
	private Integer questionnaireVersion;
	private String questionnaire;
	private Integer questionnaireAnswerHeaderId;
	private String questionnaireCompleteflag;
	private Integer ruleId;

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public Integer getQuestionnaireNumber() {
		return questionnaireNumber;
	}

	public void setQuestionnaireNumber(Integer questionnaireNumber) {
		this.questionnaireNumber = questionnaireNumber;
	}

	public Integer getQuestionnaireVersion() {
		return questionnaireVersion;
	}

	public void setQuestionnaireVersion(Integer questionnaireVersion) {
		this.questionnaireVersion = questionnaireVersion;
	}

	public String getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(String questionnaireDescription) {
		this.questionnaire = questionnaireDescription;
	}

	public Integer getQuestionnaireAnswerHeaderId() {
		return questionnaireAnswerHeaderId;
	}

	public void setQuestionnaireAnswerHeaderId(Integer questionnaireAnswerHeaderId) {
		this.questionnaireAnswerHeaderId = questionnaireAnswerHeaderId;
	}

	public String getQuestionnaireCompleteflag() {
		return questionnaireCompleteflag;
	}

	public void setQuestionnaireCompleteflag(String questionnaireCompleteflag) {
		this.questionnaireCompleteflag = questionnaireCompleteflag;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

}
