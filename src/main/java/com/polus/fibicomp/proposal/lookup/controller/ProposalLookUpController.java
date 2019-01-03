package com.polus.fibicomp.proposal.lookup.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.budget.pojo.BudgetCategory;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.lookup.service.ProposalLookUpService;
import com.polus.fibicomp.vo.SponsorSearchResult;

@RestController
public class ProposalLookUpController {

	protected static Logger logger = Logger.getLogger(ProposalLookUpController.class.getName());

	@Autowired
	@Qualifier(value = "proposalLookUpService")
	private ProposalLookUpService proposalLookUpService;

	@RequestMapping(value = "/findSponsors", method = RequestMethod.GET)
	public List<SponsorSearchResult> getNext(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for findSponsors");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.findSponsor(searchString);
	}

	@RequestMapping(value = "/findGrantCall", method = RequestMethod.GET)
	public List<GrantCall> getGrantCall(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for getGrantCall");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.getGrantCallList(searchString);
	}

	@RequestMapping(value = "/findDepartment", method = RequestMethod.GET)
	public List<Unit> getDepartment(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for getDepartment");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.getDepartmentList(searchString);
	}

	@RequestMapping(value = "/findCostElement", method = RequestMethod.GET)
	public List<CostElement> findCostElement(HttpServletRequest request, HttpServletResponse response, @RequestHeader("searchString") String searchString,
			@RequestHeader("budgetCategoryCode") String budgetCategoryCode) {
		logger.info("Requesting for findCostElement");
		logger.info("searchString : " + searchString);
		logger.info("budgetCategoryCode : " + budgetCategoryCode);
		return proposalLookUpService.findCostElementList(searchString, budgetCategoryCode);
	}

	@RequestMapping(value = "/findKeyWords", method = RequestMethod.GET)
	public List<ScienceKeyword> findKeyWords(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for findKeyWords");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.findKeyWordsList(searchString);
	}

	@RequestMapping(value = "/findLeadUnits", method = RequestMethod.GET)
	public List<Unit> findLeadUnits(HttpServletRequest request, HttpServletResponse response, @RequestHeader("searchString") String searchString, @RequestHeader("personId") String personId) {
		logger.info("Requesting for findLeadUnits");
		logger.info("searchString : " + searchString);
		logger.info("personId : " + personId);
		return proposalLookUpService.findLeadUnitsList(searchString, personId);
	}

	@RequestMapping(value = "/findBudgetCategory", method = RequestMethod.GET)
	public List<BudgetCategory> findBudgetCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for getBudgetCategory");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.findBudgetCategoryList(searchString);
	}

	@RequestMapping(value = "/findNonEmployee", method = RequestMethod.GET)
	public List<Rolodex> getNonEmployee(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for getNonEmployee");
		logger.info("searchString : " + searchString);
		return proposalLookUpService.findNonEmployeeList(searchString);
	}

}
