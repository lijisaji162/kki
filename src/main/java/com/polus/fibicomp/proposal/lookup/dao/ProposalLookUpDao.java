package com.polus.fibicomp.proposal.lookup.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.budget.pojo.BudgetCategory;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Service
public interface ProposalLookUpDao {

	/**
	 * This method is used to fetch filtered sponsors based on input string.
	 * @param searchString - input string.
	 * @return a list of sponsors.
	 */
	public List<SponsorSearchResult> findSponsor(String searchString);

	/**
	 * This method is used to get grant call details based on search string.
	 * @param searchString - input string.
	 * @return a list of grant calls.
	 */
	public List<GrantCall> getGrantCallList(String searchString);

	/**
	 * This method is used to get department details based on search string.
	 * @param searchString - input string.
	 * @return a list of departments.
	 */
	public List<Unit> getDepartmentList(String searchString);

	/**
	 * This method is used to get cost elements based on search string.
	 * @param searchString - input string.
	 * @param budgetCategoryCode - budget category code.
	 * @return a list of cost elements.
	 */
	public List<CostElement> findCostElementList(String searchString, String budgetCategoryCode);

	/**
	 * This method is used to get key words based on search string.
	 * @param searchString - input string.
	 * @return a list of key words.
	 */
	public List<ScienceKeyword> findKeyWordsList(String searchString);

	/**
	 * This method is used to get departments based on search string.
	 * @param unitNumbers - set of unit numbers.
	 * @param searchString - input string.
	 * @return a list of departments.
	 */
	public List<Unit> fetchLeadUnitsByUnitNumbers(Set<String> unitNumbers, String searchString);

	/**
	 * This method is used to get budget categories based on search string.
	 * @param searchString - input string.
	 * @return a list of budget categories.
	 */
	public List<BudgetCategory> findBudgetCategoryList(String searchString);

	/**
	 * This method is used to get non employee based on search string.
	 * @param searchString - input string.
	 * @return a list of rolodex.
	 */
	public List<Rolodex> findNonEmployeeList(String searchString);

}
