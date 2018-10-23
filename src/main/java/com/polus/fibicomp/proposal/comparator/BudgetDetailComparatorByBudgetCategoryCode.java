package com.polus.fibicomp.proposal.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.polus.fibicomp.budget.pojo.BudgetDetail;

public class BudgetDetailComparatorByBudgetCategoryCode implements Comparator<BudgetDetail> {

	@Override
	public int compare(BudgetDetail bd1, BudgetDetail bd2) {
		return new CompareToBuilder().append(bd1.getBudgetCategoryCode(), bd2.getBudgetCategoryCode()).toComparison();
	}

}
