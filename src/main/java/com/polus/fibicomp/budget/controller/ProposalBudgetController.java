package com.polus.fibicomp.budget.controller;

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

import com.polus.fibicomp.budget.service.BudgetService;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@RestController
public class ProposalBudgetController {

	protected static Logger logger = Logger.getLogger(ProposalBudgetController.class.getName());

	@Autowired
	@Qualifier(value = "budgetService")
	private BudgetService budgetService;

	@RequestMapping(value = "/addBudgetPeriod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addBudgetPeriod(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addBudgetPeriod");
		return budgetService.addBudgetPeriod(vo);
	}

	@RequestMapping(value = "/resetBudgetRates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getBudgetRates(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for resetBudgetRates");
		return budgetService.resetProposalRates(vo);
	}

	@RequestMapping(value = "/getSyncBudgetRates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getSyncBudgetRates(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for getSyncBudgetRates");
		return budgetService.getSyncBudgetRates(vo);
	}

	@RequestMapping(value = "/autoCalculate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String autoCalculate(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for autoCalculate");
		return budgetService.autoCalculate(vo);
	}

	@RequestMapping(value = "/deleteBudgetPeriod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteBudgetPeriod(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteBudgetPeriod");
		logger.info("budgetPeriodId : " + vo.getBudgetPeriodId());
		return budgetService.deleteBudgetPeriod(vo);
	}

	@RequestMapping(value = "/deleteBudgetLineItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteBudgetLineItem(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteBudgetLineItem");
		logger.info("budgetPeriodId : " + vo.getBudgetPeriodId());
		logger.info("budgetDetailId : " + vo.getBudgetDetailId());
		return budgetService.deleteBudgetLineItem(vo);
	}

}
