package com.polus.fibicomp.budget.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;

@Transactional
@Service(value = "budgetCalculationService")
public class BudgetCalculationServiceImpl implements BudgetCalculationService {

	protected static Logger logger = Logger.getLogger(BudgetCalculationServiceImpl.class.getName());

	@Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;

	@Override
	public void calculateBudget(BudgetHeader budget) {
		List<BudgetPeriod> budgetPeriods = budget.getBudgetPeriods();
        for (BudgetPeriod budgetPeriod : budgetPeriods) {
            calculateBudgetPeriod(budget, budgetPeriod);
        }
        /*if(!budgetPeriods.isEmpty()){
            syncCostsToBudget(budget);
        }*/
	}

	@Override
	public void calculateBudgetPeriod(BudgetHeader budget, BudgetPeriod budgetPeriod) {
		 if (isCalculationRequired(budget, budgetPeriod)){
	            budgetPeriodCalculate(budget, budgetPeriod);
	        }
	    	updateBudgetTotalCost(budget);
	}
	
	/**
     * Checks if a calculation is required where Budget periods must be synced in line items.
     *
     * @param budgetPeriod the current budget period.
     *
     * @return true if calculation is required false if not
     */
    protected boolean isCalculationRequired(BudgetHeader budget, final BudgetPeriod budgetPeriod){
        assert budgetPeriod != null : "The budget period is null";
        if(budgetService.isRateOverridden(budgetPeriod)){
            return false;
        }
        return true;
    }

	@Override
	public void calculateBudgetLineItem(BudgetHeader budget, BudgetDetail budgetLineItem) {
		
	}
    
	/**
     * 
     * This method calculates and sync the budget period.
     */
    public void budgetPeriodCalculate(BudgetHeader budget, BudgetPeriod budgetPeriod) {
        budgetPeriod.setTotalDirectCost(BigDecimal.ZERO);
        budgetPeriod.setTotalIndirectCost(BigDecimal.ZERO);
        budgetPeriod.setTotalCost(BigDecimal.ZERO);

        //avoiding java.util.ConcurrentModificationException by doing a defensive copy
        new ArrayList<>(budgetPeriod.getBudgetDetails()).forEach(budgetLineItem -> {
            calculateBudgetLineItem(budget, budgetLineItem);
            budgetPeriod.setTotalDirectCost(budgetPeriod.getTotalDirectCost().add(budgetLineItem.getLineItemCost()));
            budgetPeriod.setTotalIndirectCost(budgetPeriod.getTotalIndirectCost().add(budgetLineItem.getLineItemCost()));
            budgetPeriod.setTotalCost(budgetPeriod.getTotalCost().add(budgetPeriod.getTotalDirectCost().add(budgetPeriod.getTotalIndirectCost())));
        });
    }

	@Override
	public void updateBudgetTotalCost(BudgetHeader budget) {
		BigDecimal totalDirectCost = BigDecimal.ZERO;
		BigDecimal totalIndirectCost = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        for (BudgetPeriod budgetPeriod : budget.getBudgetPeriods()) {
            if (budgetPeriod.getTotalDirectCost().compareTo(BigDecimal.ZERO) > 0 || budgetPeriod.getTotalIndirectCost().compareTo(BigDecimal.ZERO) > 0) {
                budgetPeriod.setTotalCost(budgetPeriod.getTotalDirectCost().add(budgetPeriod.getTotalIndirectCost()));
            }
            totalDirectCost = totalDirectCost.add(budgetPeriod.getTotalDirectCost());
            totalIndirectCost = totalIndirectCost.add(budgetPeriod.getTotalIndirectCost());
            totalCost = totalCost.add(budgetPeriod.getTotalCost());
        }
        budget.setTotalDirectCost(totalDirectCost);
        budget.setTotalIndirectCost(totalIndirectCost);
        budget.setTotalCost(totalCost);
	}

	@Override
	public void applyToLaterPeriods(BudgetHeader budget, BudgetPeriod budgetPeriod, BudgetDetail budgetLineItem) {
		// TODO Auto-generated method stub
		
	}
}
