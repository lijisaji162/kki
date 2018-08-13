package com.polus.fibicomp.budget.service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface BudgetService {

	public List<FibiProposalRate> fetchFilteredProposalRates(Proposal proposal, Set<String> rateClassTypes);

	/**
	 * This method is to create a new budget for a proposal
	 * @param vo
	 * @return set of values to create a budget
	 */
	public String createProposalBudget(ProposalVO vo);

	/**
	 * This method is to save or update proposal budget
	 * @param vo
	 * @return saved proposal
	 */
	public Proposal saveOrUpdateProposalBudget(ProposalVO vo);

	public String getSyncBudgetRates(ProposalVO proposalVO);

	public String autoCalculate(ProposalVO proposalVO);

	public List<BudgetPeriod> generateBudgetPeriods(BudgetHeader budget);

	public String addBudgetPeriod(ProposalVO vo);

	public boolean budgetLineItemExists(BudgetHeader budget, Integer budgetPeriod);

	public void generateAllPeriods(BudgetHeader budget);

	public void calculateBudget(BudgetHeader budget);

	/**
     * 
     * This method is recalculate the budget. For Proposal Budget, recalcuate is same as calculate. 
     * For Award Budget, it removes Award Budget Sumamry Calc Amounts before the calculation 
     * @param budget
     */
    public void recalculateBudget(BudgetHeader budget);

    /**
     * This method is to check whether Budget Summary calculated amounts for a BudgetPeriod 
     * have been modified on AwardBudgetSummary screen
     * @return true if there is any change
     */
    public boolean isRateOverridden(BudgetPeriod budgetPeriod);

    public boolean isLeapDaysInPeriod(Date sDate, Date eDate);

    public List<Date> getNewStartEndDates(List<Date> startEndDates, int gap, int duration, Date prevDate, boolean leapDayInPeriod, boolean leapDayInGap);

    public List<CostElement> fetchSysGeneratedCostElements(String activityTypeCode);

	public String resetProposalRates(ProposalVO vo);

	public String deleteBudgetPeriod(ProposalVO proposalVO);

	public String deleteBudgetLineItem(ProposalVO proposalVO);

}
