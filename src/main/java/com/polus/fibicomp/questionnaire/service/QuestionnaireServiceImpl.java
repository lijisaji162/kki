package com.polus.fibicomp.questionnaire.service;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.polus.fibicomp.dbengine.DBEngine;
import com.polus.fibicomp.dbengine.DBEngineConstants;
import com.polus.fibicomp.dbengine.Parameter;
import com.polus.fibicomp.questionnaire.dao.QuestionnaireDAO;
import com.polus.fibicomp.questionnaire.dto.DeleteDto;
import com.polus.fibicomp.questionnaire.dto.QuestionnaireAnswerDto;
import com.polus.fibicomp.questionnaire.dto.QuestionnaireDataBus;
import com.polus.fibicomp.questionnaire.dto.QuestionnaireDto;

@Service(value = "questionnaireService")
public class QuestionnaireServiceImpl implements QuestionnaireService {

	protected static Logger logger = LogManager.getLogger(QuestionnaireServiceImpl.class.getName());

	@Autowired
	public DBEngine dbEngine;

	@Autowired
	public QuestionnaireDAO questionnaireDAO;// = new QuestionnaireDAO();

	private static final String Radio = "Radio";
	private static final String Checkbox = "Checkbox";
	private static final String Text = "Text";
	private static final String Textarea = "Textarea";
	private static final String Date = "Date";
	private static final String Attachment = "Attachment";
	private static final String YN = "Y/N";
	private static final String YNNA = "Y/N/NA";

	/*
	 * public QuestionnaireServiceImpl() { dbEngine = new DBEngine(); }
	 */

	@Override
	public QuestionnaireDataBus getApplicableQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		String moduleItemKey = questionnaireDataBus.getModule_item_key();
		String moduleSubItemKey = questionnaireDataBus.getModule_sub_item_key();
		Integer moduleItemCode = questionnaireDataBus.getModule_item_code();
		Integer moduleSubItemCode = questionnaireDataBus.getModule_sub_item_code();
		logger.info("moduleItemKey : " + moduleItemKey);
		logger.info("moduleSubItemKey : " + moduleSubItemKey);
		logger.info("moduleItemCode : " + moduleItemCode);
		logger.info("moduleSubItemCode : " + moduleSubItemCode);
		ArrayList<HashMap<String, Object>> applicableQuestionnaire = questionnaireDAO.getApplicableQuestionnaireData(moduleItemKey, moduleSubItemKey, moduleItemCode, moduleSubItemCode);
		questionnaireDataBus.setApplicableQuestionnaire(applicableQuestionnaire);
		return questionnaireDataBus;
	}

	public QuestionnaireDataBus getQuestionnaireDetails(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		QuestionnaireDto questionnaire = new QuestionnaireDto();
		ArrayList<HashMap<String, Object>> answerList = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> questionsList = new ArrayList<HashMap<String, Object>>();
		Integer questionnaireAnswerHeaderId = questionnaireDataBus.getQuestionnaire_answer_header_id();
		Integer questionnaireId = questionnaireDataBus.getQuestionnaire_id();
		logger.info("questionnaireAnswerHeaderId : " + questionnaireAnswerHeaderId);
		logger.info("questionnaireId : " + questionnaireId);
		try {
			questionsList = getQuestionnaireQuestions(questionnaireId);
			if (questionnaireAnswerHeaderId != null) {
				answerList = getAnswer(questionnaireAnswerHeaderId);
			}
			questionsList = setAnswerToQuestionnaireQuestion(questionsList, answerList);
			questionnaire.setQuestions(questionsList);
			ArrayList<HashMap<String, Object>> header = questionnaireDAO.getQuestionnaireHeader(questionnaireId);
			ArrayList<HashMap<String, Object>> condition = questionnaireDAO.getQuestionnaireCondition(questionnaireId);
			ArrayList<HashMap<String, Object>> option = questionnaireDAO.getQuestionnaireOptions(questionnaireId);
			questionnaireDataBus.setHeader(header.get(0));
			questionnaire.setConditions(condition);
			questionnaire.setOptions(option);
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireDetails " + e.getMessage());
		}
		questionnaireDataBus.setQuestionnaire(questionnaire);
		return questionnaireDataBus;
	}

	@Override
	public QuestionnaireDataBus saveQuestionnaireAnswers(QuestionnaireDataBus questionnaireDataBus, MultipartHttpServletRequest request) throws Exception {
		Integer moduleCode = questionnaireDataBus.getModule_item_code();
		Integer moduleSubItemCode = questionnaireDataBus.getModule_sub_item_code();
		String moduleItemKey = questionnaireDataBus.getModule_item_key();
		String moduleSubItemKey = questionnaireDataBus.getModule_sub_item_key();
		Integer questionnaireId = questionnaireDataBus.getQuestionnaire_id();
		String userId = questionnaireDataBus.getAction_user_id();
		Integer questionnaireAnsHeaderId = questionnaireDataBus.getQuestionnaire_answer_header_id();
		logger.info("moduleItemKey : " + moduleItemKey);
		logger.info("moduleSubItemKey : " + moduleSubItemKey);
		logger.info("moduleItemCode : " + moduleCode);
		logger.info("moduleSubItemCode : " + moduleSubItemCode);
		logger.info("questionnaireId : " + questionnaireId);
		logger.info("userId : " + userId);
		logger.info("questionnaireAnswerHeaderId : " + questionnaireAnsHeaderId);
		try {
			questionnaireAnsHeaderId = questionnaireDataBus.getQuestionnaire_answer_header_id();
			String questionnaireCompletionFlag = questionnaireDataBus.getQuestionnaire_complete_flag();
			if (questionnaireAnsHeaderId == null) {
				questionnaireAnsHeaderId = generateQuesAnsHeaderId();
				insertQuestionnaireAnswerHeader(moduleCode, moduleSubItemCode, moduleItemKey, moduleSubItemKey, userId,
						questionnaireId, questionnaireAnsHeaderId);
				questionnaireDataBus.setQuestionnaire_answer_header_id(questionnaireAnsHeaderId);
			}
			updateQuestionnaireCompleteFlag(questionnaireAnsHeaderId, questionnaireCompletionFlag, userId);

			ArrayList<HashMap<String, Object>> questionlist = questionnaireDataBus.getQuestionnaire().getQuestions();
			for (HashMap<String, Object> question : questionlist) {
				saveAnswer(question, questionnaireDataBus,request);
			}
			// questionnaireDataBus =
			// getQuestionnaireDetails(questionnaireDataBus);
		} catch (Exception e) {
			logger.error("Exception in saveQuestionnaireAnswers " + e.getMessage());
		}
		return questionnaireDataBus;
	}

	private ArrayList<HashMap<String, Object>> getQuestionOptions(Integer questionId,
			ArrayList<HashMap<String, Object>> options) {
		ArrayList<HashMap<String, Object>> questionOption = new ArrayList<>();
		for (HashMap<String, Object> option : options) {
			if (questionId == Integer.parseInt(option.get("QUESTION_ID").toString())) {
				questionOption.add(option);
			}
		}
		return questionOption;

	}

	
	private void saveAnswer(HashMap<String, Object> question, QuestionnaireDataBus questionnaireDataBus, MultipartHttpServletRequest request)
			throws Exception {
		String updateUser = questionnaireDataBus.getAction_user_id();
		Integer answerHeaderId = questionnaireDataBus.getQuestionnaire_answer_header_id();
		HashMap<Integer, String> answerList = new HashMap<>();
		ArrayList<HashMap<String, Object>> option = questionnaireDataBus.getQuestionnaire().getOptions();
		Integer questionId = (Integer) question.get("QUESTION_ID");
		String acType = (String) question.get("AC_TYPE");
		if (Text.equals(question.get("ANSWER_TYPE"))) {
			Integer noOfAnswer = Integer.parseInt((question.get("NO_OF_ANSWERS").toString()));			
			if(question.get("ANSWERS") != null){
				 answerList =  (HashMap<Integer, String>)question.get("ANSWERS");
			}			
			for (Integer index = 1; index <= noOfAnswer; index++) {
				String answer = answerList.get(index.toString());
				saveAnswer(answerHeaderId,question, acType, answer, index, updateUser);
			}

		} else if (Checkbox.equals(question.get("ANSWER_TYPE"))) {
			Integer noOfAnswer = Integer.parseInt((question.get("NO_OF_ANSWERS").toString()));
			ArrayList<HashMap<String, Object>> questionOption = getQuestionOptions(questionId, option);
			answerList = (HashMap<Integer, String>) question.get("ANSWERS");
			for (HashMap<String, Object> opt : questionOption) {
				String answer = (String) opt.get("OPTION_LABEL");
				Object answerObj = answerList.get(answer);
				 /* (boolean) answerObj*/
				if(answerObj != null){
					boolean  boolenaAnswerObj = Boolean.valueOf(answerObj.toString());// (boolean) answerObj;
					boolean isAnswered = (answerObj == null ? false : boolenaAnswerObj);
					if (isAnswered)
						saveAnswer(answerHeaderId,question, acType, answer, noOfAnswer, updateUser);
				}
				
			}

		} else if (Attachment.equals(question.get("ANSWER_TYPE"))) {
			answerList = (HashMap<Integer, String>) question.get("ANSWERS");
			String answer = answerList.get("1");
			if ("D".equals(acType)) {
				saveAttachmentAnswer(answerHeaderId,question, acType, answer, 1, updateUser,request);
				saveAnswer(answerHeaderId,question, acType, answer, 1, updateUser);	
			}else{
				saveAnswer(answerHeaderId,question, acType, answer, 1, updateUser);	
				saveAttachmentAnswer(answerHeaderId,question, acType, answer, 1, updateUser,request);
			}		
		}else {			
			answerList = (HashMap<Integer, String>) question.get("ANSWERS");
			String answer = answerList.get("1");
			saveAnswer(answerHeaderId,question, acType, answer, 1, updateUser);
		}
	}

	private void saveAttachmentAnswer(Integer answerHeaderId, HashMap<String, Object> question, String acType,
			String answer, int answerNumber, String updateUser, MultipartHttpServletRequest request) throws Exception {
		/*if(answer == null || answer.length() == 0){
			return;
		}*/	
		MultipartFile file = getAttachmentFile(request,question);
		Integer questionnaireAnsAttachmentId = null;
		 if ("D".equals(acType)) {		 
			 questionnaireAnsAttachmentId = Integer.parseInt(question.get("ATTACHMENT_ID").toString());
			 deleteAttachmentAnswer(questionnaireAnsAttachmentId,question);
		} else if ("U".equals(acType)) {
			questionnaireAnsAttachmentId = Integer.parseInt(question.get("ATTACHMENT_ID").toString());
			updateAttachmentAnswer(questionnaireAnsAttachmentId,file,updateUser);
		} else if ("I".equals(acType))  {
			insertAttachmentAnswer(answerHeaderId,question, updateUser,file);
			question.put("AC_TYPE","U");
		} 		
	}

private MultipartFile getAttachmentFile(MultipartHttpServletRequest request, HashMap<String, Object> question) {
	MultipartFile file = null;
	for(String attachmentKey : request.getFileMap().keySet()){
		if(question.get("QUESTION_ID").toString().equalsIgnoreCase(attachmentKey)){
			file = request.getFileMap().get(attachmentKey);
		}
	}
		return file;
	}

private Integer deleteAttachmentAnswer(Integer questionnaireAnsAttachmentId, HashMap<String, Object> question) {
	Integer isUpdated = 0;
	try {
		ArrayList<Parameter> inParam = new ArrayList<>();
		inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ATT_ID>>", DBEngineConstants.TYPE_INTEGER,
				questionnaireAnsAttachmentId));
		isUpdated = dbEngine.executeUpdate(inParam, "DELETE_QUESTIONNAIRE_ATTACHMENT_ANSWER");
	} catch (Exception e) {
		logger.error("Exception in deleteQuestionnaireAnswer " + e.getMessage());
	}
	return isUpdated;
}

private void updateAttachmentAnswer(Integer questionnaireAnsAttachmentId, MultipartFile file, String updateUser) {
	try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<ATTACHMENT>>", DBEngineConstants.TYPE_BLOB,file.getBytes()));
			inParam.add(new Parameter("<<FILE_NAME>>", DBEngineConstants.TYPE_STRING, file.getOriginalFilename()));
			inParam.add(new Parameter("<<CONTENT_TYPE>>", DBEngineConstants.TYPE_STRING, file.getContentType()));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, updateUser));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ATT_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnsAttachmentId));
			dbEngine.executeUpdate(inParam, "UPDATE_QUESTIONNAIRE_ATTACHMENT_ANSWER");	
	} catch (Exception e) {
		logger.error("Exception in updateAttachmentAnswer " + e.getMessage());
	}
}

	private void saveAnswer(Integer answerHeaderId,HashMap<String, Object> question, String acType, String answer, Integer answerNumber,
			String updateUser) throws Exception {
		/*if(answer == null || answer.length() == 0){
			return;
		}*/		
		if ("I".equals(acType)) {
			Integer questionnaireAnswerId = insertAnswer(answerHeaderId,question, answer, answerNumber, updateUser);
			question.put("AC_TYPE","U");
			question.put("QUESTIONNAIRE_ANS_ID",questionnaireAnswerId);
		} else if ("U".equals(acType)) {
			updateAnswer(answerHeaderId,question, answer, answerNumber, updateUser);
		} else if ("D".equals(acType)) {
			deleteAnswer(answerHeaderId,question);
		}

	}

	private Integer insertAttachmentAnswer(Integer answerHeaderId, HashMap<String, Object> question, String updateUser, MultipartFile file)throws Exception {
		try {

				ArrayList<Parameter> inParam = new ArrayList<>();
				Integer questionnaireAnsAttachmentId = questionnaireDAO.getNextQuestionAnswerAttachId();
				inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ATT_ID>>", DBEngineConstants.TYPE_INTEGER,
						questionnaireAnsAttachmentId));
				inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ID>>", DBEngineConstants.TYPE_INTEGER,
						question.get("QUESTIONNAIRE_ANS_ID")));
				inParam.add(new Parameter("<<ATTACHMENT>>", DBEngineConstants.TYPE_BLOB,file.getBytes()));
				inParam.add(new Parameter("<<FILE_NAME>>", DBEngineConstants.TYPE_STRING, file.getOriginalFilename()));
				inParam.add(new Parameter("<<CONTENT_TYPE>>", DBEngineConstants.TYPE_STRING, file.getContentType()));
				inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
				inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, updateUser));
				dbEngine.executeUpdate(inParam, "INSERT_QUESTIONNAIRE_ATTACHMENT_ANSWER");
				question.put("ATTACHMENT_ID",questionnaireAnsAttachmentId);
				
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer " + e.getMessage());
		}
		return 1;
	}
	
	private Integer insertAnswer(Integer answerHeaderId,HashMap<String, Object> question, String answer, Integer answerNumber,
			String updateUser) throws Exception {
		Integer questionnaireAnswerId = questionnaireDAO.getNextQuestionnaireAnswerId();
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnswerId));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ANS_HEADER_ID>>", DBEngineConstants.TYPE_INTEGER,
					answerHeaderId));
			inParam.add(new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER, question.get("QUESTION_ID")));
			inParam.add(new Parameter("<<ANSWER_NUMBER>>", DBEngineConstants.TYPE_INTEGER, answerNumber));
			inParam.add(new Parameter("<<ANSWER>>", DBEngineConstants.TYPE_STRING, answer));
			inParam.add(new Parameter("<<ANSWER_LOOKUP_CODE>>", DBEngineConstants.TYPE_STRING,
					question.get("ANSWER_LOOKUP_CODE")));
			inParam.add(new Parameter("<<EXPLANATION>>", DBEngineConstants.TYPE_STRING, question.get("EXPLANATION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, updateUser));
			dbEngine.executeUpdate(inParam, "INSERT_QUESTIONNAIRE_ANSWER");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswer " + e.getMessage());
		}
		return questionnaireAnswerId;
	}

	private Integer updateAnswer(Integer answerHeaderId,HashMap<String, Object> question, String answer, Integer answerNumber,
			String updateUser) throws Exception {
		try {

			ArrayList<Parameter> inParam = new ArrayList<>();
//			inParam.add(new Parameter("<<QUESTIONNAIRE_ANS_HEADER_ID>>", DBEngineConstants.TYPE_INTEGER,
//					answerHeaderId));	
			inParam.add(new Parameter("<<ANSWER>>", DBEngineConstants.TYPE_STRING, answer));
			inParam.add(new Parameter("<<ANSWER_LOOKUP_CODE>>", DBEngineConstants.TYPE_STRING,
					question.get("ANSWER_LOOKUP_CODE")));
			inParam.add(new Parameter("<<EXPLANATION>>", DBEngineConstants.TYPE_STRING, question.get("EXPLANATION")));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, updateUser));
			inParam.add(new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER,question.get("QUESTION_ID")));
			inParam.add(new Parameter("<<ANSWER_NUMBER>>", DBEngineConstants.TYPE_INTEGER,answerNumber));
			dbEngine.executeUpdate(inParam, "UPDATE_QUESTIONNAIRE_ANSWER");
		} catch (Exception e) {
			logger.error("Exception in updateQnrAnswer " + e.getMessage());
		}
		return 1;
	}

	public Integer deleteAnswer(Integer answerHeaderId,HashMap<String, Object> question) throws Exception {
		Integer isUpdated = 0;
		try {
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTION_ID>>", DBEngineConstants.TYPE_INTEGER,
					question.get("QUESTION_ID")));
			isUpdated = dbEngine.executeUpdate(inParam, "DELETE_QUESTIONNAIRE_ANSWER");
		} catch (Exception e) {
			logger.error("Exception in deleteQuestionnaireAnswer " + e.getMessage());
		}
		return isUpdated;
	}

	private ArrayList<HashMap<String, Object>> getAnswer(Integer questionnaireAnswerHeaderId) throws Exception {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(new Parameter("<<AV_QNR_ANS_HEADER_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnswerHeaderId));
			ArrayList<HashMap<String, Object>> questionnaireAnswer = dbEngine.executeQuery(inputParam,
					"GET_QUESTIONNAIRE_ANSWER");
			return questionnaireAnswer;
		} catch (Exception e) {
			logger.error("Exception in getAnswer " + e.getMessage());
			return new ArrayList<HashMap<String, Object>>();
		}
	}

	private ArrayList<HashMap<String, Object>> getQuestionnaireQuestions(Integer questionnaireId) throws Exception {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(new Parameter("<<AV_QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
			ArrayList<HashMap<String, Object>> questionnaireQuestions = dbEngine.executeQuery(inputParam,
					"GET_QUESTIONNAIRE_QUESTIONS");
			return questionnaireQuestions;
		} catch (Exception e) {
			logger.error("Exception in getQuestionnaireQuestions " + e.getMessage());
			return new ArrayList<HashMap<String, Object>>();
		}
	}

	private ArrayList<HashMap<String, Object>> setAnswerToQuestionnaireQuestion(
			ArrayList<HashMap<String, Object>> questionnaireQuestions, ArrayList<HashMap<String, Object>> answerList) {	
		Integer noOfAnswer = null;
		if (questionnaireQuestions != null && !questionnaireQuestions.isEmpty()) {
			for (HashMap<String, Object> question : questionnaireQuestions) {
				boolean isQuestionAnswered = false;
				Integer questionId = Integer.parseInt((question.get("QUESTION_ID").toString()));				
				HashMap<String, Object> questionAnswer = new HashMap<>();
				for (HashMap<String, Object> answer : answerList) {					
					if (Integer.parseInt((answer.get("QUESTION_ID").toString())) == questionId) {	
												
						isQuestionAnswered = true;						
						if(Text.equals(question.get("ANSWER_TYPE"))){
							questionAnswer.put(answer.get("ANSWER_NUMBER").toString(), (answer.get("ANSWER")  == null ? "":answer.get("ANSWER").toString()));
						} else if (Checkbox.equals(question.get("ANSWER_TYPE"))) {
							questionAnswer.put(answer.get("ANSWER").toString(), "true");
						}else{
							if (answer.get("ANSWER") != null) {
								questionAnswer.put("1", answer.get("ANSWER").toString());
							}
							if(answer.get("ATTACHMENT_ID") != null){
								question.put("ATTACHMENT_ID", answer.get("ATTACHMENT_ID").toString());
							}							
						}
					}
				}
				if (isQuestionAnswered) {
					question.put("ANSWERS", questionAnswer);	
					question.put("AC_TYPE", "U");
					
				} else {
					if (question.get("NO_OF_ANSWERS") != null) {
						noOfAnswer = Integer.parseInt((question.get("NO_OF_ANSWERS").toString()));
						question.put("ANSWERS", buildAnswerMap(noOfAnswer));
					}
				}
				
				if ("G0".equals(question.get("GROUP_NAME")) || isQuestionAnswered) {
					question.put("SHOW_QUESTION", true);
				} 
			}
		}
		return questionnaireQuestions;
	}

	private HashMap<Integer, String> buildAnswerMap(Integer noOfAnswers) {
		HashMap<Integer, String> answerList = new HashMap<>();
		for (Integer index = 1; index <= noOfAnswers; index++) {
			answerList.put(index, "");
		}
		return answerList;
	}
	private void insertQuestionnaireAnswerHeader(Integer moduleCode, Integer moduleSubItemCode, String moduleItemKey,
			String moduleSubItemKey, String userId, Integer questionnaireId, Integer questionnaireAnsHeaderId)
			throws Exception {
		ArrayList<Parameter> inParam = new ArrayList<>();
		try {
			inParam.add(new Parameter("<<QUESTIONNAIRE_ANS_HEADER_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnsHeaderId));
			inParam.add(new Parameter("<<QUESTIONNAIRE_ID>>", DBEngineConstants.TYPE_INTEGER, questionnaireId));
			inParam.add(new Parameter("<<MODULE_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER, moduleCode));
			inParam.add(new Parameter("<<MODULE_SUB_ITEM_CODE>>", DBEngineConstants.TYPE_INTEGER, moduleSubItemCode));
			inParam.add(new Parameter("<<MODULE_ITEM_KEY>>", DBEngineConstants.TYPE_STRING, moduleItemKey));
			inParam.add(new Parameter("<<MODULE_SUB_ITEM_KEY>>", DBEngineConstants.TYPE_STRING, moduleSubItemKey));
			inParam.add(new Parameter("<<QUESTIONNAIRE_COMPLETED_FLAG>>", DBEngineConstants.TYPE_STRING, "N"));
			inParam.add(new Parameter("<<UPDATE_TIMESTAMP>>", DBEngineConstants.TYPE_DATE, getCurrentDate()));
			inParam.add(new Parameter("<<UPDATE_USER>>", DBEngineConstants.TYPE_STRING, userId));
			dbEngine.executeUpdate(inParam, "INSERT_QUESTIONNAIRE_ANS_HEADER");
		} catch (Exception e) {
			logger.error("Exception in insertQuestionnaireAnswerHeader " + e.getMessage());
		}
	}

	private void updateQuestionnaireCompleteFlag(Integer questionnaireAnsHeaderId, String completionFlag, String userId)
			throws Exception {
		try {
			ArrayList<Parameter> inputParam = new ArrayList<>();
			inputParam.add(
					new Parameter("<<QUESTIONNAIRE_COMPLETED_FLAG>>", DBEngineConstants.TYPE_STRING, completionFlag));
			inputParam.add(new Parameter("<<QUESTIONNAIRE_ANS_HEADER_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnsHeaderId));
			int isUpdated = dbEngine.executeUpdate(inputParam, "UPDATE_QUESTIONNAIRE_COMPLETE_FLAG");
		} catch (Exception e) {
			logger.error("Exception in updateQuestionnaireCompleteFlag " + e.getMessage());
		}
	}

	private Integer generateQuesAnsHeaderId() {
		Integer questionnaireAnsHeaderId = null;
		try {			
			questionnaireAnsHeaderId = questionnaireDAO.getNextQuestionnaireAnswerHeaderId();
		} catch (Exception e) {
			logger.error("Exception in generateQuesAnsHeaderId " + e.getMessage());
		}
		return questionnaireAnsHeaderId;
	}

	public Integer deleteQuestionnaireAnswer(QuestionnaireAnswerDto questionnaireAnswerDto) throws Exception {
		Integer isUpdated = 0;
		try {			
			ArrayList<Parameter> inParam = new ArrayList<>();
			inParam.add(new Parameter("<<QUESTIONNAIRE_ANSWER_ID>>", DBEngineConstants.TYPE_INTEGER,
					questionnaireAnswerDto.getQuestionnaireAnswerId()));
			isUpdated = dbEngine.executeUpdate(inParam, "DELETE_QUESTIONNAIRE_ANSWER");
		} catch (Exception e) {
			logger.error("Exception in deleteQuestionnaireAnswer " + e.getMessage());
		}
		return isUpdated;
	}

	@Override
	public boolean isQuestionnaireComplete(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private Date getCurrentDate() {
		return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	}

	@Override
	public QuestionnaireDataBus configureQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		questionnaireDataBus = saveQuestionnaireHeader(questionnaireDataBus);
		questionnaireDataBus = saveQuestion(questionnaireDataBus);
		questionnaireDataBus = saveUsage(questionnaireDataBus);
		return questionnaireDataBus;
	}

	private QuestionnaireDataBus saveQuestionnaireHeader(QuestionnaireDataBus questionnaireDataBus) {
		if ("I".equals(questionnaireDataBus.getAcType())) {
			questionnaireDataBus = setQuestionnaireHeaderInfo(questionnaireDataBus);
		}
		saveHeaderInfo(questionnaireDataBus);
		return questionnaireDataBus;
	}

	private QuestionnaireDataBus setQuestionnaireHeaderInfo(QuestionnaireDataBus questionnaireDataBus) {
		Integer questionnaireId = questionnaireDAO.getNextQuestionnaireId();
		Integer questionnaireVersionNumber = 0;
		Integer questionnaireNumber = (Integer) questionnaireDataBus.getHeader().get("QUESTIONNAIRE_NUMBER");
		if (questionnaireNumber == null) {
			questionnaireNumber = questionnaireDAO.getNextQuestionnaireNumber();
			questionnaireVersionNumber = 1;

		} else {
			questionnaireNumber = questionnaireDAO.getMaxQuestionnaireVersionNumber();
			++questionnaireNumber;
		}
		HashMap<String, Object> hmHeader = questionnaireDataBus.getHeader();
		hmHeader.put("QUESTIONNAIRE_ID", questionnaireId);
		hmHeader.put("QUESTIONNAIRE_NUMBER", questionnaireNumber);
		hmHeader.put("QUESTIONNAIRE_VERSION", questionnaireVersionNumber);
		questionnaireDataBus.setQuestionnaire_id(questionnaireId);	
		return questionnaireDataBus;
	}

	private void saveHeaderInfo(QuestionnaireDataBus questionnaireDataBus) {
		if ("I".equals(questionnaireDataBus.getAcType())) {
			questionnaireDAO.insertHeader(questionnaireDataBus);
			questionnaireDataBus.setAcType("U");
		} else {
			questionnaireDAO.updateHeader(questionnaireDataBus);
		}
	}

	private QuestionnaireDataBus saveUsage(QuestionnaireDataBus questionnaireDataBus) {
		ArrayList<HashMap<String, Object>> usage = questionnaireDataBus.getUsage();
		for (HashMap<String, Object> hmUsage : usage) {
			if ("I".equals(hmUsage.get("AC_TYPE"))) {
				Integer questionnaireOptionId = questionnaireDAO.getNextQuestionnaireUsageId();
				hmUsage.put("QUESTIONNAIRE_USAGE_ID", questionnaireOptionId);
				hmUsage.put("QUESTIONNAIRE_ID", questionnaireDataBus.getQuestionnaire_id());
				questionnaireDAO.insertUsage(hmUsage);
				hmUsage.put("AC_TYPE", "U");
			} else if ("D".equals(hmUsage.get("AC_TYPE"))) {
				questionnaireDAO.deleteUsage(hmUsage);
				usage.remove(hmUsage);

			} else {
				questionnaireDAO.updateUsage(hmUsage);
			}
		}
		return questionnaireDataBus;
	}

	private QuestionnaireDataBus saveQuestion(QuestionnaireDataBus questionnaireDataBus) {
		HashMap<Integer, Integer> hmQuestionMapping = new HashMap<Integer, Integer>();
		ArrayList<HashMap<String, Object>> questions = questionnaireDataBus.getQuestionnaire().getQuestions();
		Integer sortOrder = 0;
		Integer maxGroupNumber = 0;
		/*if (!"I".equals(questionnaireDataBus.getAcType())) {
			sortOrder = (questions == null ? 0 : questions.size());
		}*/
		for (HashMap<String, Object> hmQuestion : questions) {
			if ("I".equals(hmQuestion.get("AC_TYPE"))) {
				Integer questionId = questionnaireDAO.getNextQuestionId();
				Integer questionNumber = questionnaireDAO.getNextQuestionNumber();
				Integer questionVersionNumber = 1;
				hmQuestionMapping.put((Integer) hmQuestion.get("QUESTION_ID"), questionId);

				hmQuestion = setParentQuestionId(hmQuestion, hmQuestionMapping);

				hmQuestion.put("QUESTION_ID", questionId);
				hmQuestion.put("QUESTION_NUMBER", questionNumber);
				hmQuestion.put("QUESTION_VERSION_NUMBER", questionVersionNumber);
				hmQuestion.put("QUESTIONNAIRE_ID", questionnaireDataBus.getHeader().get("QUESTIONNAIRE_ID"));
				hmQuestion.put("QUESTION_ID", questionId);
				hmQuestion.put("SORT_ORDER", ++sortOrder);
				questionnaireDAO.insertQuestion(hmQuestion);
				hmQuestion.put("AC_TYPE", "U");
			} else {
				if (questionnaireDataBus.isNew_questionnaire_version()) {
					Integer questionVersionNumber = (Integer) hmQuestion.get("QUESTION_VERSION_NUMBER");
					++questionVersionNumber;
					hmQuestion.put("QUESTION_VERSION_NUMBER", questionVersionNumber);
				}
				hmQuestion.put("SORT_ORDER", ++sortOrder);
				hmQuestion.put("AC_TYPE", "U");
				questionnaireDAO.updateQuestion(hmQuestion);
			}

			if (hmQuestion.get("GROUP_NAME") != null) {
				if (Integer.parseInt(hmQuestion.get("GROUP_NAME").toString().substring(1)) > maxGroupNumber) {
					maxGroupNumber = Integer.parseInt(hmQuestion.get("GROUP_NAME").toString().substring(1));
				}
			}
		}
		// delete question Id
		ArrayList<Integer> deleteList = questionnaireDataBus.getQuestionnaire().getDeleteList().getQuestion();
		if (deleteList != null && !deleteList.isEmpty()) {
			for (Integer id : deleteList) {
				questionnaireDAO.deleteConditionForQuestion(id);
				questionnaireDAO.deleteOptionForQuestion(id);
				questionnaireDAO.deleteQuestion(id);
			}
		}

		questionnaireDataBus.getQuestionnaire().setMaxGroupNumber(maxGroupNumber);
		questionnaireDataBus = saveConditions(questionnaireDataBus, hmQuestionMapping);
		questionnaireDataBus = saveOptions(questionnaireDataBus, hmQuestionMapping);
		return questionnaireDataBus;
	}

	private HashMap<String, Object> setParentQuestionId(HashMap<String, Object> hmQuestion,
			HashMap<Integer, Integer> hmQuestionMapping) {
		// Temporary questionId generated from Front End will be less than 1000
		// PARENT_QUESTION_ID is used to set both actual and temp question id
		if (hmQuestion.get("PARENT_QUESTION_ID") != null && (Integer) hmQuestion.get("PARENT_QUESTION_ID") <= 1000) {
			hmQuestion.put("PARENT_QUESTION_ID", hmQuestionMapping.get((Integer) hmQuestion.get("PARENT_QUESTION_ID")));
		}

		return hmQuestion;
	}

	private Integer getQuestionId(HashMap<String, Object> hmQuestion,
			HashMap<Integer, Integer> hmQuestionMapping) {
		// Temporary questionId generated from Front End will be less than 1000
		Integer questionId = (Integer) hmQuestion.get("QUESTION_ID");		
		if (hmQuestion.get("QUESTION_ID") != null && (Integer) hmQuestion.get("QUESTION_ID") <= 1000) {
			questionId =  hmQuestionMapping.get((Integer) hmQuestion.get("QUESTION_ID"));
		}

		return questionId;
	}
	private QuestionnaireDataBus saveOptions(QuestionnaireDataBus questionnaireDataBus,
			HashMap<Integer, Integer> hmQuestionMapping) {
		ArrayList<HashMap<String, Object>> options = questionnaireDataBus.getQuestionnaire().getOptions();
		for (HashMap<String, Object> hmOption : options) {
			if ("I".equals((String) hmOption.get("AC_TYPE"))) {
				Integer questionnaireOptionId = questionnaireDAO.getNextQuestionOptionId();
				hmOption.put("QUESTION_OPTION_ID", questionnaireOptionId);
				hmOption.put("QUESTION_ID", getQuestionId(hmOption,hmQuestionMapping));
				questionnaireDAO.insertOption(hmOption);
				hmOption.put("AC_TYPE", "U");
			} else {
				questionnaireDAO.updateOption(hmOption);
			}
		}	
		// delete option
		ArrayList<Integer> deleteList = questionnaireDataBus.getQuestionnaire().getDeleteList().getOption();
		if (deleteList != null && !deleteList.isEmpty()) {
			for (Integer id : deleteList) {
				questionnaireDAO.deleteOption(id);
			}
		}
		return questionnaireDataBus;
	}
	

	private QuestionnaireDataBus saveConditions(QuestionnaireDataBus questionnaireDataBus,
			HashMap<Integer, Integer> hmQuestionMapping) {
		ArrayList<HashMap<String, Object>> conditions = questionnaireDataBus.getQuestionnaire().getConditions();
		for (HashMap<String, Object> hmCondition : conditions) {
			if ("I".equals((String) hmCondition.get("AC_TYPE"))) {
				Integer questionnaireConditionId = questionnaireDAO.getNextQuestionConditionId();
				hmCondition.put("QUESTION_CONDITION_ID", questionnaireConditionId);
				hmCondition.put("QUESTION_ID", getQuestionId(hmCondition,hmQuestionMapping));				
				questionnaireDAO.insertCondition(hmCondition);
				hmCondition.put("AC_TYPE", "U");
			} else {
				questionnaireDAO.updateCondition(hmCondition);
			}
		}

		// delete condition
		ArrayList<Integer> deleteList = questionnaireDataBus.getQuestionnaire().getDeleteList().getCondition();
		if (deleteList != null && !deleteList.isEmpty()) {
			for (Integer id : deleteList) {
				questionnaireDAO.deleteCondition(id);
			}
		}

		return questionnaireDataBus;
	}

	@Override
	public QuestionnaireDataBus showAllQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		ArrayList<HashMap<String, Object>> questionnaireList = questionnaireDAO.getAllQuestionnaire();
		ArrayList<HashMap<String, Object>> questionnaireGroup = questionnaireDAO.getQuestionnaireGroup();
		questionnaireDataBus.setQuestionnaireList(questionnaireList);
		questionnaireDataBus.setQuestionnaireGroup(questionnaireGroup);
		return questionnaireDataBus;
	}

	@Override
	public QuestionnaireDataBus createQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		Integer questionnaireId = -1;
		HashMap<String, Object> header = new HashMap<>();
		// ArrayList<HashMap<String, Object>> usage =
		// getQuestionnaireUsage(questionnaireId);
		ArrayList<HashMap<String, Object>> questions = questionnaireDAO.getQuestionnaireQuestion(questionnaireId);
		ArrayList<HashMap<String, Object>> condition = questionnaireDAO.getQuestionnaireCondition(questionnaireId);
		ArrayList<HashMap<String, Object>> option = questionnaireDAO.getQuestionnaireOptions(questionnaireId);
		ArrayList<HashMap<String, Object>> questionnaireGroup = questionnaireDAO.getQuestionnaireGroup();
		QuestionnaireDto questionnaire = new QuestionnaireDto();
		header.put("QUESTIONNAIRE_ID", null);
		header.put("QUESTIONNAIRE_NUMBER", null);
		header.put("QUESTIONNAIRE_VERSION", null);
		header.put("QUESTIONNAIRE_NAME", null);
		header.put("QUESTIONNAIRE_DESCRIPTION", null);
		header.put("QUEST_GROUP_TYPE_CODE", null);
		header.put("IS_FINAL", null);
		header.put("UPDATE_USER", null);
		header.put("UPDATE_TIMESTAMP", null);
		questionnaireDataBus.setHeader(header);

		ArrayList<HashMap<String, Object>> usage = new ArrayList<>();
		HashMap<String, Object> hmUsage = new HashMap<>();
		hmUsage.put("QUESTIONNAIRE_LABEL", null);
		hmUsage.put("QUESTIONNAIRE_USAGE_ID", null);
		hmUsage.put("MODULE_ITEM_CODE", null);
		hmUsage.put("MODULE_SUB_ITEM_CODE", null);
		hmUsage.put("QUESTIONNAIRE_LABEL", null);
		hmUsage.put("IS_MANDATORY", null);
		hmUsage.put("RULE_ID", null);
		hmUsage.put("AC_TYPE", "I");
		usage.add(hmUsage);
		questionnaireDataBus.setUsage(usage);
		questionnaire.setQuestions(questions);
		questionnaire.setConditions(condition);
		questionnaire.setOptions(option);
		questionnaire = setAdditionalAttributes(questionnaire);
		questionnaireDataBus.setQuestionnaireGroup(questionnaireGroup);
		questionnaireDataBus.setQuestionnaire(questionnaire);
		DeleteDto deleteDto = new DeleteDto();
		questionnaire.setDeleteList(deleteDto);
		questionnaireDataBus.setAcType("I");
		return questionnaireDataBus;

		/*
		 * HashMap<String, Object> hmHeader = new HashMap<>();
		 * hmHeader.put("QUESTIONNAIRE_ID", null);
		 * hmHeader.put("QUESTIONNAIRE_NUMBER", null);
		 * hmHeader.put("VERSION_NUMBER AS QUESTIONNAIRE_VERSION", null);
		 * hmHeader.put("QUESTIONNAIRE AS QUESTIONNAIRE_NAME", null);
		 * hmHeader.put("DESCRIPTION as QUESTIONNAIRE_DESCRIPTION", null);
		 * hmHeader.put("QUEST_GROUP_TYPE_CODE", null); hmHeader.put("IS_FINAL",
		 * null); hmHeader.put("UPDATE_USER", null);
		 * hmHeader.put("UPDATE_TIMESTAMP", null);
		 * questionnaireDataBus.setHeader(hmHeader);
		 * 
		 * 
		 * ArrayList<HashMap<String, Object>> usage = new ArrayList<>();
		 * HashMap<String, Object> hmUsage = new HashMap<>();
		 * hmUsage.put("MODULE_ITEM_CODE", null);
		 * hmUsage.put("MODULE_SUB_ITEM_CODE", null);
		 * hmUsage.put("QUESTIONNAIRE_LABEL", null); hmUsage.put("IS_MANDATORY",
		 * null); hmUsage.put("RULE_ID", null); usage.add(hmUsage);
		 * questionnaireDataBus.setUsage(usage);
		 * 
		 * ArrayList<HashMap<String, Object>> questions = new ArrayList<>();
		 * HashMap<String, Object> hmQuestions = new HashMap<>();
		 * hmQuestions.put("QUESTION_ID", null);
		 * hmQuestions.put("QUESTION_NUMBER", null);
		 * hmQuestions.put("QUESTION_VERSION_NUMBER", null);
		 * hmQuestions.put("SORT_ORDER", null); hmQuestions.put("QUESTION",
		 * null); hmQuestions.put("DESCRIPTION", null);
		 * hmQuestions.put("PARENT_QUESTION_ID", null);
		 * hmQuestions.put("HELP_LINK", null); hmQuestions.put("ANSWER_TYPE",
		 * null); hmQuestions.put("ANSWER_LENGTH", null);
		 * hmQuestions.put("NO_OF_ANSWERS", null);
		 * hmQuestions.put("LOOKUP_TYPE", null); hmQuestions.put("LOOKUP_NAME",
		 * null); hmQuestions.put("LOOKUP_FIELD", null);
		 * hmQuestions.put("GROUP_NAME", null); hmQuestions.put("GROUP_LABEL",
		 * null); hmQuestions.put("HAS_CONDITION", null);
		 * hmQuestions.put("UPDATE_USER", null);
		 * hmQuestions.put("UPDATE_TIMESTAMP", null);
		 * hmQuestions.put("HIDE_QUESTION", false); hmQuestions.put("ANSWERS",
		 * new HashMap<>()); hmQuestions.put("SHOW_QUESTION", true);
		 * questions.add(hmQuestions); questionnaire.setQuestions(questions);
		 * 
		 * ArrayList<HashMap<String, Object>> condition = new ArrayList<>();
		 * HashMap<String, Object> hmCondition = new HashMap<>();
		 * hmCondition.put("QUESTION_CONDITION_ID", null);
		 * hmCondition.put("QUESTION_ID", null);
		 * hmCondition.put("CONDITION_TYPE", null);
		 * hmCondition.put("CONDITION_VALUE", null);
		 * hmCondition.put("GROUP_NAME", null);
		 * hmCondition.put("UPDATE_TIMESTAMP", null);
		 * hmCondition.put("UPDATE_USER", null); condition.add(hmCondition);
		 * questionnaire.setConditions(condition);
		 * 
		 * ArrayList<HashMap<String, Object>> option = new ArrayList<>();
		 * HashMap<String, Object> hmOption = new HashMap<>();
		 * hmOption.put("QUESTION_OPTION_ID", null); hmOption.put("QUESTION_ID",
		 * null); hmOption.put("OPTION_NUMBER", null);
		 * hmOption.put("OPTION_LABEL", null);
		 * hmOption.put("REQUIRE_EXPLANATION", null);
		 * hmOption.put("EXPLANTION_LABEL", null);
		 * hmOption.put("UPDATE_TIMESTAMP", null); hmOption.put("UPDATE_USER",
		 * null); option.add(hmOption); questionnaire.setOptions(option);
		 */

	}

	@Override
	public QuestionnaireDataBus editQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception {
		Integer questionnaireId = (Integer) questionnaireDataBus.getQuestionnaire_id();
		ArrayList<HashMap<String, Object>> header = questionnaireDAO.getQuestionnaireHeader(questionnaireId);
		ArrayList<HashMap<String, Object>> usage = questionnaireDAO.getQuestionnaireUsage(questionnaireId);
		ArrayList<HashMap<String, Object>> questions = questionnaireDAO.getQuestionnaireQuestion(questionnaireId);
		ArrayList<HashMap<String, Object>> condition = questionnaireDAO.getQuestionnaireCondition(questionnaireId);
		ArrayList<HashMap<String, Object>> option = questionnaireDAO.getQuestionnaireOptions(questionnaireId);
		ArrayList<HashMap<String, Object>> questionnaireGroup = questionnaireDAO.getQuestionnaireGroup();
		QuestionnaireDto questionnaire = new QuestionnaireDto();
		questionnaireDataBus.setHeader(header.get(0));
		questionnaireDataBus.setUsage(usage);
		questionnaire.setQuestions(questions);
		questionnaire.setConditions(condition);
		questionnaire = setAdditionalAttributes(questionnaire);
		questionnaire.setOptions(option);
		questionnaireDataBus.setQuestionnaireGroup(questionnaireGroup);
		questionnaireDataBus.setQuestionnaire(questionnaire);

		DeleteDto deleteDto = new DeleteDto();
		ArrayList<Integer> deleteList = new ArrayList<>();
		/*
		 * ArrayList<Integer> conditions = new ArrayList<>(); conditions.add(1);
		 * conditions.add(2); conditions.add(3); ArrayList<Integer> options =
		 * new ArrayList<>(); options.add(1); options.add(2); options.add(3);
		 * ArrayList<Integer> question = new ArrayList<>(); question.add(1);
		 * question.add(2); question.add(3);
		 * 
		 * 
		 * deleteDto.setCondition(conditions); deleteDto.setOption(options);
		 * deleteDto.setQuestion(question);
		 */
		questionnaire.setDeleteList(deleteDto);

		return questionnaireDataBus;
	}

	private QuestionnaireDto setAdditionalAttributes(QuestionnaireDto questionnaire) {
		ArrayList<HashMap<String, Object>> output = questionnaire.getQuestions();
		Integer maxGroupNumber = 0;
		if (output != null && !output.isEmpty()) {
			for (HashMap<String, Object> hmResult : output) {
				hmResult.put("HIDE_QUESTION", false);
				hmResult.put("ANSWERS", new HashMap<>());
				if ("G0".equals(hmResult.get("GROUP_NAME"))) {
					hmResult.put("SHOW_QUESTION", true);
				} else {
					hmResult.put("SHOW_QUESTION", false);
				}
			}
		}

		ArrayList<HashMap<String, Object>> conditions = questionnaire.getConditions();
		if (conditions != null && !conditions.isEmpty()) {
			for (HashMap<String, Object> hmResult : conditions) {
				if (hmResult.get("GROUP_NAME") != null) {
					if (Integer.parseInt(hmResult.get("GROUP_NAME").toString().substring(1)) > maxGroupNumber) {
						maxGroupNumber = Integer.parseInt(hmResult.get("GROUP_NAME").toString().substring(1));
					}
				}
			}
		}
		questionnaire.setMaxGroupNumber(maxGroupNumber);
		return questionnaire;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(QuestionnaireDataBus questionnaireDataBus,
			HttpServletResponse response) {
		ResponseEntity<byte[]> attachmentData = null;
		try {			
			ArrayList<HashMap<String, Object>> questionnaireAttachment = questionnaireDAO.getQuestionnaireAttachment(
					questionnaireDataBus.getQuestionnaire_ans_attachment_id());
			if(questionnaireAttachment.get(0).get("ATTACHMENT") != null){
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) questionnaireAttachment.get(0).get("ATTACHMENT");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();headers.setContentType(MediaType.parseMediaType(questionnaireAttachment.get(0).get("CONTENT_TYPE").toString()));
				String filename = questionnaireAttachment.get(0).get("FILE_NAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in downloadAttachments " + e.getMessage());
		}
		return attachmentData;
	}

}