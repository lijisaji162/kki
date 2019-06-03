package com.polus.fibicomp.questionnaire.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.dbengine.DBEngine;
import com.polus.fibicomp.dbengine.DBEngineConstants;
import com.polus.fibicomp.dbengine.Parameter;
import com.polus.fibicomp.questionnaire.dto.QuestionnaireDataBus;
import com.polus.fibicomp.questionnaire.service.QuestionnaireServiceImpl;

@Service
public class QuestionnaireDAO {
	protected static Logger logger = LogManager.getLogger(QuestionnaireServiceImpl.class.getName());

	@Autowired
	public DBEngine dbEngine;

	/*
	 * public QuestionnaireDAO() { dbEngine = new DBEngine(); }
	 */

	public synchronized Integer getNextQuestionnaireId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ID", "QUESTIONNAIRE_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ID", "QUESTIONNAIRE_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionnaireNumber() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_NUMBER", "QUESTIONNAIRE_NUMBER");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_NUMBER", "QUESTIONNAIRE_NUMBER", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getMaxQuestionnaireVersionNumber() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_VERSION_NUMBER", "QUESTIONNAIRE_ID");
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTION_ID", "QUESTION_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTION_ID", "QUESTION_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionNumber() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTION_NUMBER", "QUESTION_NUMBER");
			updateMaxColumnValue("UPDATE_MAX_QUESTION_NUMBER", "QUESTION_NUMBER", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionConditionId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTION_CONDITION_ID", "QUESTION_CONDITION_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTION_CONDITION_ID", "QUESTION_CONDITION_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionOptionId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTION_OPTION_ID", "QUESTION_OPTION_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTION_OPTION_ID", "QUESTION_OPTION_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionAnsHeaderId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ANS_HEADER_ID", "QUESTIONNAIRE_ANS_HEADER_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ANS_HEADER_ID", "QUESTIONNAIRE_ANS_HEADER_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionAnswerId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ANSWER_ID", "QUESTIONNAIRE_ANSWER_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ANSWER_ID", "QUESTIONNAIRE_ANSWER_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionAnswerAttachId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ANSWER_ATT_ID", "QUESTIONNAIRE_ANSWER_ATT_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ANSWER_ATT_ID", "QUESTIONNAIRE_ANSWER_ATT_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionnaireUsageId() {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_USAGE_ID", "QUESTIONNAIRE_USAGE_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_USAGE_ID", "QUESTIONNAIRE_USAGE_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}
	}

	public synchronized Integer getNextQuestionnaireAnswerHeaderId() throws Exception {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ANS_HEADER_ID", "QUESTIONNAIRE_ANS_HEADER_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ANS_HEADER_ID", "QUESTIONNAIRE_ANS_HEADER_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}		
		
	}
	
	public synchronized Integer getNextQuestionnaireAnswerId() throws Exception {
		Integer columnId = null;
		try {
			columnId = getMaxColumnValue("GET_MAX_QUESTIONNAIRE_ANSWER_ID", "QUESTIONNAIRE_ANSWER_ID");
			updateMaxColumnValue("UPDATE_MAX_QUESTIONNAIRE_ANSWER_ID", "QUESTIONNAIRE_ANSWER_ID", columnId);
			return columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return 0;
		}	
		
	}


	private Integer getMaxColumnValue(String SqlId, String columnName) {
		Integer columnId = null;
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			ArrayList<HashMap<String, Object>> result = dbEngine.executeQuery(inputParam, SqlId);
			HashMap<String, Object> hmResult = result.get(0);
			if (hmResult.get(columnName) == null) {
				return 0;
			} else {
				columnId = Integer.parseInt(hmResult.get(columnName).toString());
			}
			return ++columnId;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());
			return null;
		}
	}

	private void updateMaxColumnValue(String SqlId, String columnName, Integer maxColumnValue) {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(new Parameter(columnName, DBEngineConstants.TYPE_INTEGER, maxColumnValue));
			int result = dbEngine.executeUpdate(inputParam, SqlId);
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireId" + e.getMessage());

		}
	}

	public void insertHeader(QuestionnaireDataBus questionnaireDataBus) {
		try {
			HashMap<String, Object> hmHeader = questionnaireDataBus.getHeader();
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmHeader.get("QUESTIONNAIRE_ID")));
			inParam.add(new Parameter("<<QUESTIONNAIRE_NUMBER>>", DBEngineConstants.TYPE_INTEGER,
					hmHeader.get("QUESTIONNAIRE_NUMBER")));
			inParam.add(new Parameter("<<VERSION_NUMBER>>", DBEngineConstants.TYPE_INTEGER,
					hmHeader.get("QUESTIONNAIRE_VERSION")));
			inParam.add(new Parameter("<<QUESTIONNAIRE>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUESTIONNAIRE_NAME")));
			inParam.add(new Parameter("<<DESCRIPTION>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUESTIONNAIRE_DESCRIPTION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmHeader.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUEST_GROUP_TYPE_CODE>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUEST_GROUP_TYPE_CODE")));
			inParam.add(new Parameter("<<IS_FINAL>>", DBEngineConstants.TYPE_STRING, (hmHeader.get("IS_FINAL") == null ? "N" : ((Boolean)hmHeader.get("IS_FINAL") == true ? "Y": "N"))));
			Integer isInserted = dbEngine.executeUpdate(inParam, "INSERT_QUESTIONNAIRE_HEADER");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}
	}

	public void updateHeader(QuestionnaireDataBus questionnaireDataBus) {
		try {
			HashMap<String, Object> hmHeader = questionnaireDataBus.getHeader();
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUESTIONNAIRE_NAME")));
			inParam.add(new Parameter("<<DESCRIPTION>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUESTIONNAIRE_DESCRIPTION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmHeader.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUEST_GROUP_TYPE_CODE>>", DBEngineConstants.TYPE_STRING,
					hmHeader.get("QUEST_GROUP_TYPE_CODE")));
			inParam.add(new Parameter("<<IS_FINAL>>", DBEngineConstants.TYPE_STRING, (hmHeader.get("IS_FINAL") == null ? "N" : ((Boolean)hmHeader.get("IS_FINAL") == true ? "Y": "N"))));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmHeader.get("QUESTIONNAIRE_ID")));
			Integer isUpdated = dbEngine.executeUpdate(inParam, "UPDATE_QUESTIONNAIRE_HEADER");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}

	}

	public void insertQuestion(HashMap<String, Object> hmQuestion) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(
					new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER, hmQuestion.get("QUESTION_ID")));
			inParam.add(new Parameter("<<QUESTION_NUMBER>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("QUESTION_NUMBER")));
			inParam.add(new Parameter("<<QUESTION_VERSION_NUMBER>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("QUESTION_VERSION_NUMBER")));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("QUESTIONNAIRE_ID")));
			inParam.add(new Parameter("<<SORT_ORDER>>", DBEngineConstants.TYPE_INTEGER, hmQuestion.get("SORT_ORDER")));
			inParam.add(new Parameter("<<QUESTION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("QUESTION")));
			inParam.add(new Parameter("<<DESCRIPTION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("DESCRIPTION")));
			inParam.add(new Parameter("<<PARENT_QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("PARENT_QUESTION_ID")));
			inParam.add(new Parameter("<<HELP_LINK>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("HELP_LINK")));
			inParam.add(new Parameter("<<ANSWER_TYPE>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("ANSWER_TYPE")));
			inParam.add(new Parameter("<<ANSWER_LENGTH>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("ANSWER_LENGTH")));
			inParam.add(new Parameter("<<NO_OF_ANSWERS>>", DBEngineConstants.TYPE_INTEGER,
					hmQuestion.get("NO_OF_ANSWERS")));
			inParam.add(new Parameter("<<LOOKUP_TYPE>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_TYPE")));
			inParam.add(new Parameter("<<LOOKUP_NAME>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_NAME")));
			inParam.add(
					new Parameter("<<LOOKUP_FIELD>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_FIELD")));
			inParam.add(new Parameter("<<GROUP_NAME>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("GROUP_NAME")));
			inParam.add(new Parameter("<<GROUP_LABEL>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("GROUP_LABEL")));
			inParam.add(
					new Parameter("<<HAS_CONDITION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("HAS_CONDITION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("UPDATE_USER")));
			Integer isInserted = dbEngine.executeUpdate(inParam, "INSERT_QUESTION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}

	}

	public void updateQuestion(HashMap<String, Object> hmQuestion) {
		try {			
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("QUESTION")));
			inParam.add(new Parameter("<<DESCRIPTION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("DESCRIPTION")));
			inParam.add(new Parameter("<<PARENT_QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER,	hmQuestion.get("PARENT_QUESTION_ID")));
			inParam.add(new Parameter("<<HELP_LINK>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("HELP_LINK")));
			inParam.add(new Parameter("<<ANSWER_TYPE>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("ANSWER_TYPE")));
			inParam.add(new Parameter("<<ANSWER_LENGTH>>", DBEngineConstants.TYPE_INTEGER,hmQuestion.get("ANSWER_LENGTH")));
			inParam.add(new Parameter("<<NO_OF_ANSWERS>>", DBEngineConstants.TYPE_INTEGER,hmQuestion.get("NO_OF_ANSWERS")));
			inParam.add(new Parameter("<<LOOKUP_TYPE>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_TYPE")));
			inParam.add(new Parameter("<<LOOKUP_NAME>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_NAME")));
			inParam.add(new Parameter("<<LOOKUP_FIELD>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("LOOKUP_FIELD")));
			inParam.add(new Parameter("<<GROUP_NAME>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("GROUP_NAME")));
			inParam.add(new Parameter("<<GROUP_LABEL>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("GROUP_LABEL")));
			inParam.add(new Parameter("<<HAS_CONDITION>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("HAS_CONDITION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmQuestion.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER, hmQuestion.get("QUESTION_ID")));
			Integer isUpdated = dbEngine.executeUpdate(inParam, "UPDATE_QUESTION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}

	}

	public void deleteQuestion(Integer questionID) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<question_id>>", DBEngineConstants.TYPE_INTEGER, questionID));
			Integer isUpdate = dbEngine.executeUpdate(inParam, "delete_questionnaire_question");
		} catch (Exception e) {
			logger.error("Exception in deleteQuestion " + e.getMessage());
		}
	}

	public void insertCondition(HashMap<String, Object> hmCondition) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTION_CONDITION_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmCondition.get("QUESTION_CONDITION_ID")));
			inParam.add(
					new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER, hmCondition.get("QUESTION_ID")));
			inParam.add(new Parameter("<<CONDITION_TYPE>>", DBEngineConstants.TYPE_STRING,
					hmCondition.get("CONDITION_TYPE")));
			inParam.add(new Parameter("<<CONDITION_VALUE>>", DBEngineConstants.TYPE_STRING,
					hmCondition.get("CONDITION_VALUE")));
			inParam.add(new Parameter("<<GROUP_NAME>>", DBEngineConstants.TYPE_STRING, hmCondition.get("GROUP_NAME")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(
					new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmCondition.get("UPDATE_USER")));
			Integer isInserted = dbEngine.executeUpdate(inParam, "INSERT_QUESTION_CONDITION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage() + hmCondition.get("QUESTION_ID"));
		}

	}

	public void updateCondition(HashMap<String, Object> hmCondition) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<CONDITION_TYPE>>", DBEngineConstants.TYPE_STRING,
					hmCondition.get("CONDITION_TYPE")));
			inParam.add(new Parameter("<<CONDITION_VALUE>>", DBEngineConstants.TYPE_STRING,
					hmCondition.get("CONDITION_VALUE")));
			inParam.add(new Parameter("<<GROUP_NAME>>", DBEngineConstants.TYPE_STRING, hmCondition.get("GROUP_NAME")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(
					new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmCondition.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUESTION_CONDITION_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmCondition.get("QUESTION_CONDITION_ID")));
			Integer isUpdated = dbEngine.executeUpdate(inParam, "UPDATE_QUESTION_CONDITION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}
	}

	public void deleteCondition(Integer conditionId) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<question_condition_id>>", DBEngineConstants.TYPE_INTEGER, conditionId));
			Integer isUpdate = dbEngine.executeUpdate(inParam, "delete_condition");
		} catch (Exception e) {
			logger.error("Exception in deleteCondition" + e.getMessage());
		}
	}

	public void deleteConditionForQuestion(Integer questionId) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<question_id>>", DBEngineConstants.TYPE_INTEGER, questionId));
			dbEngine.executeUpdate(inParam, "delete_question_condition");
		} catch (Exception e) {
			logger.error("Exception in deleteConditionForQuestion" + e.getMessage());
		}
	}

	public void insertOption(HashMap<String, Object> hmOption) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTION_OPTION_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmOption.get("QUESTION_OPTION_ID")));
			inParam.add(new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER, hmOption.get("QUESTION_ID")));
			inParam.add(
					new Parameter("<<OPTION_NUMBER>>", DBEngineConstants.TYPE_INTEGER, hmOption.get("OPTION_NUMBER")));
			inParam.add(new Parameter("<<OPTION_LABEL>>", DBEngineConstants.TYPE_STRING, hmOption.get("OPTION_LABEL")));
			inParam.add(new Parameter("<<REQUIRE_EXPLANATION>>", DBEngineConstants.TYPE_STRING,
					(hmOption.get("REQUIRE_EXPLANATION") == null ? "N":hmOption.get("REQUIRE_EXPLANATION"))));
			inParam.add(new Parameter("<<EXPLANTION_LABEL>>", DBEngineConstants.TYPE_STRING,
					hmOption.get("EXPLANTION_LABEL")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmOption.get("UPDATE_USER")));
			dbEngine.executeUpdate(inParam, "INSERT_QUESTION_OPTION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage() + hmOption.get("QUESTION_ID"));
		}

	}

	public void updateOption(HashMap<String, Object> hmOption) {
		// UPDATE_QUESTION_OPTION
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<OPTION_LABEL>>", DBEngineConstants.TYPE_STRING, hmOption.get("OPTION_LABEL")));
			inParam.add(new Parameter("<<REQUIRE_EXPLANATION>>", DBEngineConstants.TYPE_STRING,
					hmOption.get("REQUIRE_EXPLANATION")));
			inParam.add(new Parameter("<<EXPLANTION_LABEL>>", DBEngineConstants.TYPE_STRING,
					hmOption.get("EXPLANTION_LABEL")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmOption.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUESTION_OPTION_ID>>", DBEngineConstants.TYPE_INTEGER,
					hmOption.get("QUESTION_OPTION_ID")));
			dbEngine.executeUpdate(inParam, "UPDATE_QUESTION_OPTION");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}
	}

	public void deleteOption(Integer optionId) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTION_OPTION_ID>>", DBEngineConstants.TYPE_INTEGER, optionId));
			dbEngine.executeUpdate(inParam, "delete_option");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer" + e.getMessage());
		}
	}

	public void deleteOptionForQuestion(Integer questionID) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<question_id>>", DBEngineConstants.TYPE_INTEGER, questionID));
			dbEngine.executeUpdate(inParam, "delete_question_option");
		} catch (Exception e) {
			logger.error("Exception in delete_question_option" + e.getMessage());
		}
	}

	private Date getCurrentDate() {
		long millis = System.currentTimeMillis();
		return new java.sql.Date(millis);
	}

	public void insertUsage(HashMap<String, Object> hmUsage) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE_USAGE_ID>>", DBEngineConstants.TYPE_INTEGER,hmUsage.get("QUESTIONNAIRE_USAGE_ID")));
			inParam.add(new Parameter("<<MODULE_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER,Integer.parseInt(hmUsage.get("MODULE_ITEM_CODE").toString())));
			inParam.add(new Parameter("<<MODULE_SUB_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER,(hmUsage.get("MODULE_SUB_ITEM_CODE") == null ? 0 
					:Integer.parseInt(hmUsage.get("MODULE_SUB_ITEM_CODE").toString()))));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, hmUsage.get("QUESTIONNAIRE_ID")));
			inParam.add(new Parameter("<<QUESTIONNAIRE_LABEL>>", DBEngineConstants.TYPE_STRING,hmUsage.get("QUESTIONNAIRE_LABEL")));
			inParam.add(new Parameter("<<IS_MANDATORY>>", DBEngineConstants.TYPE_STRING,( (hmUsage.get("IS_MANDATORY") == null ? "N":((Boolean)hmUsage.get("IS_MANDATORY")? "Y":"N"))) )  );
			inParam.add(new Parameter("<<RULE_ID>>", DBEngineConstants.TYPE_STRING,hmUsage.get("RULE_ID")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmUsage.get("UPDATE_USER")));
			dbEngine.executeUpdate(inParam, "insert_questionnaire_usage");
		} catch (Exception e) {
			logger.error("Exception in insertUsage" + e.getMessage());
		}
		
	}

	public void deleteUsage(HashMap<String, Object> hmUsage) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE_USAGE_ID>>", DBEngineConstants.TYPE_INTEGER, hmUsage.get("QUESTIONNAIRE_USAGE_ID")));
			dbEngine.executeUpdate(inParam, "delete_questionnaire_usage");
		} catch (Exception e) {
			logger.error("Exception in deleteUsage" + e.getMessage());
		}
		
	}

	public void updateUsage(HashMap<String, Object> hmUsage) {
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<MODULE_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER,Integer.parseInt(hmUsage.get("MODULE_ITEM_CODE").toString())));
			inParam.add(new Parameter("<<MODULE_SUB_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER,(hmUsage.get("MODULE_SUB_ITEM_CODE") == null ? 0 
						:Integer.parseInt(hmUsage.get("MODULE_SUB_ITEM_CODE").toString()))));
			inParam.add(new Parameter("<<QUESTIONNAIRE_LABEL>>", DBEngineConstants.TYPE_STRING,hmUsage.get("QUESTIONNAIRE_LABEL")));
			inParam.add(new Parameter("<<IS_MANDATORY>>", DBEngineConstants.TYPE_STRING,((Boolean)hmUsage.get("IS_MANDATORY")? "Y":"N")));
			inParam.add(new Parameter("<<RULE_ID>>", DBEngineConstants.TYPE_STRING,hmUsage.get("RULE_ID")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, hmUsage.get("UPDATE_USER")));
			inParam.add(new Parameter("<<QUESTIONNAIRE_USAGE_ID>>", DBEngineConstants.TYPE_INTEGER,hmUsage.get("QUESTIONNAIRE_USAGE_ID")));
			dbEngine.executeUpdate(inParam, "update_questionnaire_usage");
		} catch (Exception e) {
			logger.error("Exception in updateUsage" + e.getMessage());
		}
		
	}
	
	public ArrayList<HashMap<String, Object>> getApplicableQuestionnaireData(String moduleItemKey,String moduleSubItemKey,
			Integer moduleItemCode, Integer moduleSubItemCode) throws Exception {		
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<MODULE_ITEM_KEY>>", DBEngineConstants.TYPE_STRING, moduleItemKey));
	        inParam.add(new Parameter("<<MODULE_SUB_ITEM_KEY>>", DBEngineConstants.TYPE_STRING, moduleSubItemKey));
			inParam.add(new Parameter("<<MODULE_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER, moduleItemCode));
			inParam.add(new Parameter("<<MODULE_SUB_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER, moduleSubItemCode));
			output = dbEngine.executeQuery(inParam, "GET_APPLICABLE_QUESTIONNAIRE");		
		} catch (Exception e) {
			logger.error("Exception in getApplicableQuestionnaireData" + e.getMessage());

		}
		return output;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireQuestion(Integer questionnaireId) {
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		ArrayList<Parameter> inParam = new ArrayList<>();		
		inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
		try {
			output = dbEngine.executeQuery(inParam, "GET_QUESTIONNAIRE_QUESTIONS");			
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireQuestion " + e.getMessage());
		}
		return output;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireUsage(Integer questionnaireId) {
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		ArrayList<Parameter> inParam = new ArrayList<>();
		inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
		try {
			output = dbEngine.executeQuery(inParam, "GET_QUESTIONNAIRE_HEADER_USAGE");
			if (output != null && !output.isEmpty()) {
			output.forEach(hmResult -> {	
				hmResult.put("AC_TYPE", "U");
				if ("Y".equals(hmResult.get("IS_MANDATORY"))) {
					hmResult.put("IS_MANDATORY", true);
				} else {
					hmResult.put("IS_MANDATORY", false);
				}
			});
			}			
			
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireUsage " + e.getMessage());
		}
		return output;
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireHeader(Integer questionnaireId) {
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		ArrayList<Parameter> inParam = new ArrayList<>();
		inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
		try {
			output = dbEngine.executeQuery(inParam, "GET_QUESTIONNAIRE_HEADER_INFO");
			if (output != null && !output.isEmpty()) {
				output.forEach(hmResult -> {	
					hmResult.put("AC_TYPE", "U");
					if ("Y".equals(hmResult.get("IS_FINAL"))) {
						hmResult.put("IS_FINAL", true);
					} else {
						hmResult.put("IS_FINAL", false);
					}
				});
			}
			
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireHeader " + e.getMessage());
		}
		return output;
	}
	
	public ArrayList<HashMap<String, Object>> getAllQuestionnaire() {
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();

			output = dbEngine.executeQuery(inParam, "GET_ALL_QUESTIONNAIRE");	
		} catch (Exception e) {
			logger.error("Exception in getAllQuestionnaire " + e.getMessage());

		}

		return output;
	}
	
	public ArrayList<HashMap<String, Object>> getQuestionnaireGroup() {
		ArrayList<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			output = dbEngine.executeQuery(inParam, "GET_QUESTIONNAIRE_GROUP");
		} catch (Exception e) {
			logger.error("Exception in getAllQuestionnaire " + e.getMessage());
		}
		return output;
	}
	
	public ArrayList<HashMap<String, Object>> getQuestionnaireCondition(Integer questionnaireId)
			throws Exception {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(new Parameter("<<AV_QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
			ArrayList<HashMap<String, Object>> QuestionnaireAnswerDtoMap = dbEngine.executeQuery(inputParam,
					"GET_QUESTIONNAIRE_CONDITIONS");
			return QuestionnaireAnswerDtoMap;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireQuestionsCondition" + e.getMessage());
			return new ArrayList<HashMap<String, Object>>();
		}
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireOptions(Integer questionnaireId)
			throws Exception {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(new Parameter("<<AV_QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
			ArrayList<HashMap<String, Object>> QuestionnaireAnswerDtoMap = dbEngine.executeQuery(inputParam,
					"GET_QUESTIONNAIRE_OPTIONS");
			return QuestionnaireAnswerDtoMap;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireQuestionsOptions" + e.getMessage());
			return new ArrayList<HashMap<String, Object>>();
		}
	}

	public ArrayList<HashMap<String, Object>> getQuestionnaireAttachment(Integer questionnaire_ans_attachment_id) 
		throws Exception {
			try {
				ArrayList<Parameter> inputParam = new ArrayList<>();
				inputParam.add(new Parameter("<<AV_QUESTIONNAIRE_ANS_ATTACHMENT_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaire_ans_attachment_id));
				ArrayList<HashMap<String, Object>> questionnaireAttachmentMap = dbEngine.executeQuery(inputParam,
						"GET_QUESTIONNAIRE_ATTACHMENT");
				return questionnaireAttachmentMap;
			} catch (Exception e) {
				logger.error("Exception in getQuestionnaireQuestionsOptions" + e.getMessage());
				return new ArrayList<HashMap<String, Object>>();
			}
		}
}
