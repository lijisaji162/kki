package com.polus.fibicomp.questionnaire.dto;

public class AnswerDto {
	private Integer answerNumber;
	private Integer optionNumber;
	private String answer;

	public Integer getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
	}

	public Integer getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(Integer optionNumber) {
		this.optionNumber = optionNumber;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
