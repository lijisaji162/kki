package com.polus.fibicomp.budget.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetDetailCalcAmount;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface BudgetService {

	/**
	 * This method is to fetch filtered proposal rates.
	 * @param proposal - Object of Proposal.
	 * @param rateClassTypes - type of rate class.
	 * @return list of FibiProposalRates.
	 */
	public List<FibiProposalRate> fetchFilteredProposalRates(Proposal proposal, Set<String> rateClassTypes);

	/**
	 * This method is to create a new budget for a proposal.
	 * @param vo - Object of ProposalVO.
	 * @return set of values to create a budget.
	 */
	public String createProposalBudget(ProposalVO vo);

	/**
	 * This method is to save or update proposal budget.
	 * @param vo - Object of ProposalVO.
	 * @return saved proposal.
	 */
	public Proposal saveOrUpdateProposalBudget(ProposalVO vo);

	/**
	 * This method is to get synched budget rates.
	 * @param vo - Object of ProposalVO.
	 * @return synched budget rates.
	 */
	public String getSyncBudgetRates(ProposalVO proposalVO);

	/**
	 * This method is to calculate budget.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after budget updation.
	 */
	public String autoCalculate(ProposalVO proposalVO);

	/**
	 * This method is generate budget periods.
	 * @param vo - Object of ProposalVO.
	 * @return list of budget periods.
	 */
	public List<BudgetPeriod> generateBudgetPeriods(BudgetHeader budget);

	/**
	 * This method is add a new budget period.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after budget period creation.
	 */
	public String addBudgetPeriod(ProposalVO vo);

	/**
	 * This method is to check whether budget line item exists.
	 * @param budget - Details of budget.
	 * @param budgetPeriod - Budget Period Value.
	 * @return flag based on condition checking.
	 */
	public boolean budgetLineItemExists(BudgetHeader budget, Integer budgetPeriod);

	/**
	 * This method is to generate all periods.
	 * @param budget - Details of budgetHeader.
	 */
	public void generateAllPeriods(BudgetHeader budget);

	/**
	 * This method is to calculate budget.
	 * @param budget - Details of budgetHeader.
	 */
	public void calculateBudget(BudgetHeader budget);

	/**
     * This method is recalculate the budget. For Proposal Budget, recalcuate is same as calculate.
     * For Award Budget, it removes Award Budget Sumamry Calc Amounts before the calculation.
     * @param budget.
     */
    public void recalculateBudget(BudgetHeader budget);

    /**
     * This method is to check whether Budget Summary calculated amounts for a BudgetPeriod.
     * have been modified on AwardBudgetSummary screen.
     * @return true if there is any change.
     */
    public boolean isRateOverridden(BudgetPeriod budgetPeriod);

    /**
	 * This method is to check whether there is leap days in budget period.
	 * @param sDate - Start Date.
	 * @param eDate - End date.
	 * @return flag based on condition checking.
	 */
    public boolean isLeapDaysInPeriod(Date sDate, Date eDate);

    /**
	 * This method is to get new start and end dates of budget.
	 * @param sDate - Start Date.
	 * @param eDate - End date.
	 * @param gap - gap.
	 * @param duration - duration between start and end date.
	 * @param prevDate - Previous Date.
	 * @param leapDayInPeriod - flag that tells whether there is leap day in period.
	 * @param leapDayInGap - flag that tells whether there is leap day in gap.
	 * @return list containing start date and end end date.
	 */
    public List<Date> getNewStartEndDates(List<Date> startEndDates, int gap, int duration, Date prevDate, boolean leapDayInPeriod, boolean leapDayInGap);

    /**
	 * This method is to system generated cost elements.
	 * @return list of cost elements.
	 */
    public List<CostElement> fetchSysGeneratedCostElements();

    /**
	 * This method is to reset proposal rates.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after reseting proposal rates.
	 */
	public String resetProposalRates(ProposalVO vo);

	/**
	 * This method is to delete budget period.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after deleting budget period.
	 */
	public String deleteBudgetPeriod(ProposalVO proposalVO);

	/**
	 * This method is to delete budget line item.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after deleting budget line item.
	 */
	public String deleteBudgetLineItem(ProposalVO proposalVO);

	/**
	 * This method is to copy budget period.
	 * @param vo - Object of ProposalVO.
	 * @return proposal after copying budget period.
	 */
	public String copyBudgetPeriod(ProposalVO vo);

	/**
	 * This method is to get new budget calculated amount.
	 * @param budgetPeriod - Contains budget period.
	 * @param budgetDetail - Contains budget detail.
	 * @param proposalRate - Proposal rate details.
	 * @return budget detail calculated amount.
	 */
	public BudgetDetailCalcAmount getNewBudgetCalculatedAmount(BudgetPeriod budgetPeriod, BudgetDetail budgetDetail, FibiProposalRate proposalRate);

	/**
	 * This method is to get new budget calculated amount.
	 * @param proposal - Proposal.
	 * @return propsal that contains calculated cost.
	 */
	public Proposal calculateCost(Proposal proposal);

	/**
	 * This method is to generate budget periods.
	 * @param vo - Object of ProposalVO.
	 * @return propsal that contains calculated cost.
	 */
	public String generateBudgetPeriods(ProposalVO vo);

}
