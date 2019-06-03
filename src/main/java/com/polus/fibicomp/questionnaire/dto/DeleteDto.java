package com.polus.fibicomp.questionnaire.dto;

import java.util.ArrayList;

public class DeleteDto {
	private ArrayList<Integer> condition = new ArrayList<>();
	private ArrayList<Integer> option = new ArrayList<>();
	private ArrayList<Integer> question = new ArrayList<>();
	public ArrayList<Integer> getCondition() {
		return condition;
	}
	public void setCondition(ArrayList<Integer> condition) {
		this.condition = condition;
	}
	public ArrayList<Integer> getOption() {
		return option;
	}
	public void setOption(ArrayList<Integer> option) {
		this.option = option;
	}
	public ArrayList<Integer> getQuestion() {
		return question;
	}
	public void setQuestion(ArrayList<Integer> question) {
		this.question = question;
	}
	
	
	
}
