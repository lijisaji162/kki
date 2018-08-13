package com.polus.fibicomp.budget.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.budget.common.pojo.ValidCeRateType;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;

@Service
public interface BudgetDao {

	public List<InstituteRate> filterInstituteRateByDateRange(Date startDate, Date endDate, String activityTypeCode);

	public List<CostElement> getAllCostElements();

	public RateType getOHRateTypeByParams(String rateClassCode, String rateTypeCode);

	public FibiProposalRate fetchApplicableProposalRate(Integer budgetId, Date budgetStartDate, String rateClassCode, String rateTypeCode, String activityTypeCode);

	public BudgetHeader fetchBudgetByBudgetId(Integer budgetId);

	public void saveOrUpdateBudget(BudgetHeader budgetHeader);

	public List<ValidCeRateType> fetchCostElementRateTypes(String costElement);

	public InstituteRate fetchInstituteRateByDateLessthanMax(Date startDate, String activityTypeCode);

	public List<CostElement> fetchCostElementsByIds(List<String> costElements);

	public CostElement fetchCostElementsById(String costElement);

	public BudgetPeriod getMaxBudgetPeriodByBudgetId(Integer budgetId);

}
