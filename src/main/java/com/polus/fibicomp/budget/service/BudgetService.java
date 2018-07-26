package com.polus.fibicomp.budget.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface BudgetService {

	public List<FibiProposalRate> fetchFilteredProposalRates(BudgetHeader budget);

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

	public String fetchProposalRates(ProposalVO proposalVO);

	public String getSyncBudgetRates(ProposalVO proposalVO);

	public String autoCalculate(ProposalVO proposalVO);

}
