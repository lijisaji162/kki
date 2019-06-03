package com.polus.fibicomp.questionnaire.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionnaireDataBus {

	private ArrayList<HashMap<String, Object>> applicableQuestionnaire;
	private Integer questionnaire_id;
	private String module_item_key;
	private String module_sub_item_key;
	private Integer module_item_code;
	private Integer module_sub_item_code;
	private Integer questionnaire_answer_header_id;
	private Integer questionnaire_ans_attachment_id;
	private String questionnaire_complete_flag;
	private String action_user_id;
	private String action_person_id;
	private String action_person_name;
	private String acType;
	private boolean new_questionnaire_version;
	private boolean question_editted;
	private ArrayList<HashMap<String, Object>> questionnaireList;
	private ArrayList<HashMap<String, Object>> questionnaireGroup;
	private HashMap<String, Object> header;
	private QuestionnaireDto questionnaire;
	private ArrayList<HashMap<String, Object>> usage;

	public String getQuestionnaire_complete_flag() {
		return questionnaire_complete_flag;
	}

	public void setQuestionnaire_complete_flag(String questionnaire_complete_flag) {
		this.questionnaire_complete_flag = questionnaire_complete_flag;
	}

	public String getAction_user_id() {
		return action_user_id;
	}

	public void setAction_user_id(String action_user_id) {
		this.action_user_id = action_user_id;
	}

	public String getAction_person_id() {
		return action_person_id;
	}

	public void setAction_person_id(String action_person_id) {
		this.action_person_id = action_person_id;
	}

	public String getAction_person_name() {
		return action_person_name;
	}

	public void setAction_person_name(String action_person_name) {
		this.action_person_name = action_person_name;
	}

	public String getModule_item_key() {
		return module_item_key;
	}

	public void setModule_item_key(String module_item_key) {
		this.module_item_key = module_item_key;
	}

	public String getModule_sub_item_key() {
		return module_sub_item_key;
	}

	public void setModule_sub_item_key(String module_sub_item_key) {
		this.module_sub_item_key = module_sub_item_key;
	}

	public ArrayList<HashMap<String, Object>> getApplicableQuestionnaire() {
		return applicableQuestionnaire;
	}

	public void setApplicableQuestionnaire(ArrayList<HashMap<String, Object>> applicableQuestionnaire) {
		this.applicableQuestionnaire = applicableQuestionnaire;
	}

	public ArrayList<HashMap<String, Object>> getUsage() {
		return usage;
	}

	public void setUsage(ArrayList<HashMap<String, Object>> usage) {
		this.usage = usage;
	}

	public boolean isNew_questionnaire_version() {
		return new_questionnaire_version;
	}

	public void setNew_questionnaire_version(boolean new_questionnaire_version) {
		this.new_questionnaire_version = new_questionnaire_version;
	}

	public boolean isQuestion_editted() {
		return question_editted;
	}

	public void setQuestion_editted(boolean question_editted) {
		this.question_editted = question_editted;
	}

	public Integer getModule_item_code() {
		return module_item_code;
	}

	public void setModule_item_code(Integer module_item_code) {
		this.module_item_code = module_item_code;
	}

	public Integer getModule_sub_item_code() {
		return module_sub_item_code;
	}

	public void setModule_sub_item_code(Integer module_sub_item_code) {
		this.module_sub_item_code = module_sub_item_code;
	}

	public Integer getQuestionnaire_answer_header_id() {
		return questionnaire_answer_header_id;
	}

	public void setQuestionnaire_answer_header_id(Integer questionnaire_answer_header_id) {
		this.questionnaire_answer_header_id = questionnaire_answer_header_id;
	}

	public HashMap<String, Object> getHeader() {
		return header;
	}

	public void setHeader(HashMap<String, Object> header) {
		this.header = header;
	}

	public QuestionnaireDto getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(QuestionnaireDto questionnaire) {
		this.questionnaire = questionnaire;
	}

	public Integer getQuestionnaire_id() {
		return questionnaire_id;
	}

	public void setQuestionnaire_id(Integer questionnaire_id) {
		this.questionnaire_id = questionnaire_id;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireList() {
		return questionnaireList;
	}

	public void setQuestionnaireList(ArrayList<HashMap<String, Object>> questionnaireList) {
		this.questionnaireList = questionnaireList;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireGroup() {
		return questionnaireGroup;
	}

	public void setQuestionnaireGroup(ArrayList<HashMap<String, Object>> questionnaireGroup) {
		this.questionnaireGroup = questionnaireGroup;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public Integer getQuestionnaire_ans_attachment_id() {
		return questionnaire_ans_attachment_id;
	}

	public void setQuestionnaire_ans_attachment_id(Integer questionnaire_ans_attachment_id) {
		this.questionnaire_ans_attachment_id = questionnaire_ans_attachment_id;
	}

}
