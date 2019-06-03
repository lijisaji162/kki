package com.polus.fibicomp.questionnaire.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.questionnaire.dto.QuestionnaireDataBus;
import com.polus.fibicomp.questionnaire.service.QuestionnaireService;

@Controller
public class QuestionnaireController {

	protected static Logger logger = LogManager.getLogger(QuestionnaireController.class.getName());

	@Autowired
	QuestionnaireService questionnaireService;

	@RequestMapping(value = "/getApplicableQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> getApplicableQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) throws Exception {
		logger.info("Requesting for getApplicableQuestionnaire");
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		if (questionnaireDataBus != null) {
			questionnaireDataBus = questionnaireService.getApplicableQuestionnaire(questionnaireDataBus);
		}
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/getQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> getQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) throws Exception {
		logger.info("Requesting for getQuestionnaire");
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		if (questionnaireDataBus != null) {
			questionnaireDataBus = questionnaireService.getQuestionnaireDetails(questionnaireDataBus);
		}
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/saveQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> saveQuestionnaire(MultipartHttpServletRequest request, HttpServletResponse reponse)
			throws Exception {
		logger.info("Requesting for saveQuestionnaire");
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		QuestionnaireDataBus questionnaireDataBus = null;
		String formDataJson = request.getParameter("formDataJson");
		questionnaireDataBus = mapper.readValue(formDataJson, QuestionnaireDataBus.class);
		questionnaireDataBus = questionnaireService.saveQuestionnaireAnswers(questionnaireDataBus, request);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/showAllQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> showAllQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		questionnaireDataBus = questionnaireService.showAllQuestionnaire(questionnaireDataBus);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/createQuestionnaire", method = RequestMethod.GET)
	public ResponseEntity<String> modifyQuestionnaire(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		QuestionnaireDataBus questionnaireDataBus = new QuestionnaireDataBus();
		questionnaireDataBus = questionnaireService.createQuestionnaire(questionnaireDataBus);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/configureQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> configureQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		questionnaireDataBus = questionnaireService.configureQuestionnaire(questionnaireDataBus);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/editQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> editQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		questionnaireDataBus = questionnaireService.editQuestionnaire(questionnaireDataBus);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/downloadQuesAttachment", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadAttachment(HttpServletResponse response,
			@RequestBody QuestionnaireDataBus questionnaireDataBus) {
		return questionnaireService.downloadAttachments(questionnaireDataBus, response);
	}
}
