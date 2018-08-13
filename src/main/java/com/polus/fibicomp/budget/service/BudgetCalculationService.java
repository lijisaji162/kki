package com.polus.fibicomp.budget.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;

@Service
public interface BudgetCalculationService {

	/**
	 * 
	 * This method is for calculating the entire budget version and populate the
	 * appropriate values to session (Budget).
	 */
	void calculateBudget(BudgetHeader budget);

	/**
	 * 
	 * This method is for calculating the entire budget version and populate the
	 * appropriate values to session (Budget).
	 */
	void calculateBudgetPeriod(BudgetHeader budget, BudgetPeriod budgetPeriod);

	/**
	 * 
	 * This method for calculating non-personnel budget line item. This
	 * calculates all calculated amounts and sum it up to cost of the the line
	 * item. It populates the appropriate values to session as well.
	 * (BudgetLineItemCalculatedAmount, BudgetLineItem)
	 */
	void calculateBudgetLineItem(BudgetHeader budget, BudgetDetail budgetLineItem);
	
	void updateBudgetTotalCost(BudgetHeader budget);

	/**
     * This method is to apply budgetlineitem details to all later periods.
     */
    void applyToLaterPeriods(BudgetHeader budget, BudgetPeriod budgetPeriod, BudgetDetail budgetLineItem);

}
