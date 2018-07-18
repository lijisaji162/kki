package com.polus.fibicomp.budget.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.budget.vo.BudgetVO;

@Service
public interface BudgetService {

	public List<FibiProposalRate> fetchFilteredProposalRates(BudgetVO budgetVO);

}
