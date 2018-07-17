package com.polus.fibicomp.ip.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.common.service.CommonService;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.ip.service.InstitutionalProposalService;

@RestController
public class InstitutionalProposalController {

	protected static Logger logger = Logger.getLogger(InstitutionalProposalController.class.getName());

	@Autowired
	@Qualifier(value = "institutionalProposalService")
	private InstitutionalProposalService institutionalProposalService;

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/getNext", method = RequestMethod.GET)
	public Long getNext(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for getNext");
		return commonService.getNextSequenceNumber(Constants.INSTITUTIONAL_PROPSAL_PROPSAL_NUMBER_SEQUENCE);
	}
}
