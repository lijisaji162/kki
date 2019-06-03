package com.polus.fibicomp.questionnaire.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionnaireDto {

	private Integer maxGroupNumber;
	private ArrayList<HashMap<String, Object>> questions;
	private ArrayList<HashMap<String, Object>> conditions;
	private ArrayList<HashMap<String, Object>> options;
	// private ArrayList<HashMap<String, Object>> answers;
	private DeleteDto deleteList;

	// Need to delete fields starts
	private List<QuestionnaireQuestionDto> questionnaireQuestions;

	private ArrayList<HashMap<String, Object>> questionnaireConditions;
	private ArrayList<HashMap<String, Object>> questionnaireOptions;
	private ArrayList<HashMap<String, Object>> questionnaireAnswers;
	private List<QuestionnaireAttachmentDto> quesAttachmentList;
	// Need to delete fields ends

	public DeleteDto getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(DeleteDto deleteList) {
		this.deleteList = deleteList;
	}

	public Integer getMaxGroupNumber() {
		return maxGroupNumber;
	}

	public void setMaxGroupNumber(Integer maxGroupNumber) {
		this.maxGroupNumber = maxGroupNumber;
	}

	public ArrayList<HashMap<String, Object>> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<HashMap<String, Object>> questions) {
		this.questions = questions;
	}

	public ArrayList<HashMap<String, Object>> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<HashMap<String, Object>> conditions) {
		this.conditions = conditions;
	}

	public ArrayList<HashMap<String, Object>> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<HashMap<String, Object>> options) {
		this.options = options;
	}

	/*
	 * public ArrayList<HashMap<String, Object>> getAnswers() { return answers; }
	 * 
	 * public void setAnswers(ArrayList<HashMap<String, Object>> answers) {
	 * this.answers = answers; }
	 */
	public ArrayList<HashMap<String, Object>> getQuestionnaireConditions() {
		return questionnaireConditions;
	}

	public void setQuestionnaireConditions(ArrayList<HashMap<String, Object>> questionnaireConditions) {
		this.questionnaireConditions = questionnaireConditions;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireOptions() {
		return questionnaireOptions;
	}

	public void setQuestionnaireOptions(ArrayList<HashMap<String, Object>> questionnaireOptions) {
		this.questionnaireOptions = questionnaireOptions;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireAnswers() {
		return questionnaireAnswers;
	}

	public void setQuestionnaireAnswers(ArrayList<HashMap<String, Object>> questionnaireAnswers) {
		this.questionnaireAnswers = questionnaireAnswers;
	}

	public List<QuestionnaireQuestionDto> getQuestionnaireQuestions() {
		return questionnaireQuestions;
	}

	public void setQuestionnaireQuestions(List<QuestionnaireQuestionDto> questionnaireQuestions) {
		this.questionnaireQuestions = questionnaireQuestions;
	}

	public List<QuestionnaireAttachmentDto> getQuesAttachmentList() {
		return quesAttachmentList;
	}

	public void setQuesAttachmentList(List<QuestionnaireAttachmentDto> quesAttachmentList) {
		this.quesAttachmentList = quesAttachmentList;
	}
}
