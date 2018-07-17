package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.service.AwardService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class AwardController {

	protected static Logger logger = Logger.getLogger(AwardController.class.getName());

	@Autowired
	@Qualifier(value = "awardService")
	private AwardService awardService;

	@RequestMapping(value = "/getAwardSummary", method = RequestMethod.POST)
	public String fetchAwardSummary(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getAwardSummaryData(vo.getAwardId());
	}

	@RequestMapping(value = "/getAwardReportsAndTerms", method = RequestMethod.POST)
	public String getAwardReportsAndTerms(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getAwardReportsAndTerms(vo.getAwardId());
	}

	@RequestMapping(value = "/getAwardHierarchy", method = RequestMethod.POST)
	public String fetchAwardHierarchy(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getAwardHierarchyData(vo.getAwardNumber(), vo.getSelectedAwardNumber());
	}

	@RequestMapping(value = "/getAwardCommitments", method = RequestMethod.POST)
	public String fetchAwardCommitments(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getAwardCommitmentsData(vo.getAwardId());
	}

	@RequestMapping(value = "/createProjectVariationRequest", method = RequestMethod.POST)
	public String createProjectVariationRequest(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.createProjectVariationRequest(vo);
	}

	@RequestMapping(value = "/viewTemplate", method = RequestMethod.POST)
	public String viewTemplate(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.viewTemplate(vo);
	}

	@RequestMapping(value = "/getContractAdmin", method = RequestMethod.POST)
	public String getContractAdmin(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getContractAdmin(vo);
	}

	@RequestMapping(value = "/submitOSTDetails", method = RequestMethod.POST)
	public String submitOSTDetails(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		logger.info("Request for submitOSTDetails");
		return awardService.submitOSTDetails(vo);
	}

}
