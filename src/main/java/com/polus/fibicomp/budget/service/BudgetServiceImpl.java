package com.polus.fibicomp.budget.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "budgetService")
public class BudgetServiceImpl implements BudgetService {

	protected static Logger logger = Logger.getLogger(BudgetServiceImpl.class.getName());

	@Autowired
	private BudgetDao budgetDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Override
	public List<FibiProposalRate> fetchFilteredProposalRates(BudgetHeader budget) {
		List<InstituteRate> instituteRates = budgetDao.filterInstituteRateByDateRange(budget.getStartDate(), budget.getEndDate());
		if (instituteRates != null && !instituteRates.isEmpty()) {
			List<FibiProposalRate> proposalRates = new ArrayList<FibiProposalRate>();
			for (InstituteRate instituteRate : instituteRates) {
				FibiProposalRate proposalRate = new FibiProposalRate();
				proposalRate.setApplicableRate(instituteRate.getInstituteRate());
				proposalRate.setFiscalYear(instituteRate.getFiscalYear());
				proposalRate.setInstituteRate(instituteRate.getInstituteRate());
				proposalRate.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
				proposalRate.setBudgetHeader(budget);
				proposalRate.setRateClassCode(instituteRate.getRateClassCode());
				proposalRate.setRateTypeCode(instituteRate.getRateTypeCode());
				proposalRate.setStartDate(instituteRate.getStartDate());
				proposalRate.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				proposalRate.setUpdateUser(null);
				proposalRates.add(proposalRate);
			}
			return proposalRates;
		}
		return null;
	}

	public Proposal calculateBudget(Proposal proposal) {
		BudgetHeader budgetHeader = calculate(proposal, null);
		proposal.setBudgetHeader(budgetHeader);
		return proposal;
	}

	private BudgetHeader calculate(Proposal proposal, Integer period) {
		List<BudgetPeriod> budgetPeriodsList = proposal.getBudgetHeader().getBudgetPeriods();
		for(BudgetPeriod budgetPeriod: budgetPeriodsList) {
			BigDecimal totalFringeCost = BigDecimal.ZERO;
			BigDecimal totalFandACost = BigDecimal.ZERO;
			BigDecimal totalLineItemCost = BigDecimal.ZERO;
			List<BudgetDetail> budgetDetailsList = budgetPeriod.getBudgetDetails(); //BudgetDetail refers to LineItems
			if (budgetDetailsList != null && !budgetDetailsList.isEmpty()) {
				for(BudgetDetail budgetItemDetail: budgetDetailsList) {
					//if(budgetItemDetail.getIsSystemGeneratedCostElement() == false) {
						BigDecimal fringeCostForCE = BigDecimal.ZERO;
						BigDecimal fandACostForCE = BigDecimal.ZERO;
						BigDecimal lineItemCost = budgetItemDetail.getLineItemCost();
						totalLineItemCost = totalLineItemCost.add(lineItemCost);
						fringeCostForCE = calculateFringCostForCE(budgetPeriod, budgetItemDetail, lineItemCost);
						fandACostForCE = calculateFandACostForCE(budgetPeriod, budgetItemDetail, lineItemCost);
						totalFringeCost = totalFringeCost.add(fringeCostForCE);
						totalFandACost = totalFandACost.add(fandACostForCE);
					//}
				}
				/*for(BudgetDetail budgetItemDetail: budgetDetailsList) {
					if(budgetItemDetail.getIsSystemGeneratedCostElement() == true) {
						
					}
				}*/
				budgetPeriod.setTotalDirectCost(totalLineItemCost.add(totalFringeCost));
				budgetPeriod.setTotalIndirectCost(totalFandACost);
				budgetPeriod.setTotalCost(totalLineItemCost.add(totalFringeCost).add(totalFandACost));
			}
		}
		proposal.getBudgetHeader().setBudgetPeriods(budgetPeriodsList);
		BudgetHeader budget = updateBudgetHeader(proposal);
		return budget;
	}

	private BudgetHeader updateBudgetHeader(Proposal proposal) {
		BudgetHeader budget = proposal.getBudgetHeader();
		List<BudgetPeriod> budgetPeriodList = proposal.getBudgetHeader().getBudgetPeriods();
		BigDecimal totalDirectCost = BigDecimal.ZERO;
		BigDecimal totalIndirectCost = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		if (budgetPeriodList != null && !budgetPeriodList.isEmpty()) {
			for(BudgetPeriod period: budgetPeriodList) {
				totalDirectCost = totalDirectCost.add(period.getTotalDirectCost());
				totalIndirectCost = totalIndirectCost.add(period.getTotalIndirectCost());
				totalCost = totalCost.add(period.getTotalCost());
			}
			budget.setTotalDirectCost(totalDirectCost);
			budget.setTotalIndirectCost(totalIndirectCost);
			budget.setTotalCost(totalCost);
		}
		return budget;
	}

	private BigDecimal calculateFandACostForCE(BudgetPeriod budgetPeriod, BudgetDetail budgetItemDetail,
			BigDecimal lineItemCost) {
		BigDecimal fandACost = BigDecimal.ZERO;
		Date budgetStartDate = budgetPeriod.getStartDate();
		Date budgetEndDate = budgetPeriod.getEndDate();
		BigDecimal perDayCost = lineItemCost.divide(new BigDecimal(((budgetEndDate.getTime() - budgetStartDate.getTime())/86400000 + 1)));
		BigDecimal applicableRate = budgetDao.fetchApplicableRateByStartDate(budgetStartDate);
		int numberOfDays = (int) (budgetEndDate.getTime() - budgetStartDate.getTime())/86400000;
		fandACost = fandACost.add((perDayCost.multiply(applicableRate)).multiply(new BigDecimal(numberOfDays)));
		return fandACost;
	}

	private BigDecimal calculateFringCostForCE(BudgetPeriod budgetPeriod, BudgetDetail budgetItemDetail,
			BigDecimal lineItemCost) {
		BigDecimal fringeCost = BigDecimal.ZERO;
		Date budgetStartDate = budgetPeriod.getStartDate();
		Date budgetEndDate = budgetPeriod.getEndDate();
		BigDecimal perDayCost = lineItemCost.divide(new BigDecimal(((budgetEndDate.getTime() - budgetStartDate.getTime())/86400000 + 1)));
		BigDecimal applicableRate = budgetDao.fetchApplicableRateByStartDate(budgetStartDate);
		int numberOfDays = (int) (budgetEndDate.getTime() - budgetStartDate.getTime())/86400000;
		fringeCost = fringeCost.add((perDayCost.multiply(applicableRate)).multiply(new BigDecimal(numberOfDays)));
		return fringeCost;
	}

	@Override
	public String createProposalBudget(ProposalVO vo) {
		BudgetHeader budget = new BudgetHeader();
		String rateClassCode = Constants.DEFAULT_RATE_CLASS_CODE;
		String rateTypeCode = Constants.DEFAULT_RATE_TYPE_CODE;
		RateType rateType = budgetDao.getOHRateTypeByParams(rateClassCode, rateTypeCode);
		budget.setRateType(rateType);
		budget.setRateClassCode(rateType.getRateClassCode());
		budget.setRateTypeCode(rateType.getRateTypeCode());
		budget.setCreateTimeStamp(committeeDao.getCurrentTimestamp());
		budget.setCreateUser(vo.getUserName());
		budget.setCreateUserName(vo.getUserFullName());
		budget.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		budget.setUpdateUser(vo.getUserName());
		budget.setUpdateUserName(vo.getUserFullName());
		Proposal proposal = vo.getProposal();
		proposal.setBudgetHeader(budget);
		loadBudgetInitialData(vo);
		vo.setProposal(proposal);
		return committeeDao.convertObjectToJSON(vo);
	}

	private void loadBudgetInitialData(ProposalVO vo) {
		List<CostElement> costElements = budgetDao.getAllCostElements();
		vo.setCostElements(costElements);
	}

	@Override
	public Proposal saveOrUpdateProposalBudget(ProposalVO vo) {
		//String actionType = vo.getActionType();
		Proposal proposal = vo.getProposal();
		BudgetHeader proposalBudget = vo.getProposal().getBudgetHeader();
		List<FibiProposalRate> fibiProposalRates = fetchFilteredProposalRates(proposalBudget);
		if(fibiProposalRates != null && !fibiProposalRates.isEmpty()) {
			proposalBudget.setProposalRates(fibiProposalRates);
			proposal.setBudgetHeader(proposalBudget);
		}
		if(proposal.getProposalId() == null) {
			//saveProposal
			proposal = proposalDao.saveOrUpdateProposal(proposal);
		} else {
			if(proposalBudget != null && proposalBudget.getIsAutoCalc()) {
				proposal = calculateBudget(proposal);
			}
		}
		return proposal;
	}

}
