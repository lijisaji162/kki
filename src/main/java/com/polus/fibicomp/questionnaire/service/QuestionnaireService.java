package com.polus.fibicomp.questionnaire.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.polus.fibicomp.questionnaire.dto.QuestionnaireDataBus;

public interface QuestionnaireService {

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus getApplicableQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : The list of questionnaire questions,its
	 *         condition,options and its answers
	 * @throws Exception
	 */
	QuestionnaireDataBus getQuestionnaireDetails(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param request
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus saveQuestionnaireAnswers(QuestionnaireDataBus questionnaireDataBus,
			MultipartHttpServletRequest request) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return Boolean : Check if the questionnaire is complete or not
	 * @throws Exception
	 */
	boolean isQuestionnaireComplete(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus configureQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus editQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus showAllQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireDataBus createQuestionnaire(QuestionnaireDataBus questionnaireDataBus) throws Exception;

	/**
	 * @param questionnaireDataBus
	 * @param response
	 * @return download attachments
	 */
	ResponseEntity<byte[]> downloadAttachments(QuestionnaireDataBus questionnaireDataBus, HttpServletResponse response);

}
