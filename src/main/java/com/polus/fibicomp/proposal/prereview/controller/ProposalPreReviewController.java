package com.polus.fibicomp.proposal.prereview.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.proposal.prereview.service.ProposalPreReviewService;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@RestController
public class ProposalPreReviewController {

	protected static Logger logger = Logger.getLogger(ProposalPreReviewController.class.getName());

	@Autowired
	@Qualifier(value = "proposalPreReviewService")
	private ProposalPreReviewService proposalPreReviewService;

	@RequestMapping(value = "/createProposalPreReview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createProposalPreReview(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createProposalPreReview");
		return proposalPreReviewService.createProposalPreReview(vo);
	}

}
