package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.service.LoginService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class FibiMobileDashboardController {

	protected static Logger logger = Logger.getLogger(FibiMobileDashboardController.class.getName());

	@Autowired
	@Qualifier(value = "dashboardService")
	private DashboardService dashboardService;

	@Autowired
	@Qualifier(value = "loginService")
	private LoginService loginService;

	@RequestMapping(value = "/getFibiMobileSummary", method = RequestMethod.POST)
	public String requestFibiMobileSummary(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getFibiResearchSummary(vo.getPersonId());
	}

	@RequestMapping(value = "/searchFibiDataByModuleCode", method = RequestMethod.POST)
	public String requestFibiSearch(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getProposalsBySearchCriteria(vo);
	}

	@RequestMapping(value = "/getFibiResearchSummary", method = RequestMethod.POST)
	public String requestFibiResearchSummary(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getFibiResearchSummary(vo.getPersonId(), vo.getResearchSummaryIndex());
	}

	@RequestMapping(value = "/getProposalsForCertification", method = RequestMethod.POST)
	public String getProposalsForCertification(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getProposalsForCertification(vo.getPersonId());
	}

}
