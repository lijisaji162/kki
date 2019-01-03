package com.polus.fibicomp.proposal.lookup.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.pojo.BudgetCategory;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.lookup.dao.ProposalLookUpDao;
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Transactional
@Configuration
@Service(value = "proposalLookUpService")
public class ProposalLookUpServiceImpl implements ProposalLookUpService {

	protected static Logger logger = Logger.getLogger(ProposalLookUpServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "proposalLookUpDao")
	private ProposalLookUpDao proposalLookUpDao;

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	@Qualifier(value = "roleDao")
	private RoleDao roleDao;

	@Override
	public List<SponsorSearchResult> findSponsor(String searchString) {
		return proposalLookUpDao.findSponsor(searchString);
	}

	@Override
	public List<GrantCall> getGrantCallList(String searchString) {
		return proposalLookUpDao.getGrantCallList(searchString);
	}

	@Override
	public List<Unit> getDepartmentList(String searchString) {
		return proposalLookUpDao.getDepartmentList(searchString);
	}

	@Override
	public List<CostElement> findCostElementList(String searchString, String budgetCategoryCode) {
		return proposalLookUpDao.findCostElementList(searchString, budgetCategoryCode);
	}

	@Override
	public List<ScienceKeyword> findKeyWordsList(String searchString) {
		return proposalLookUpDao.findKeyWordsList(searchString);
	}

	@Override
	public List<Unit> findLeadUnitsList(String searchString, String personId) {
		List<RoleMemberBo> memberBos = roleDao.fetchCreateProposalPersonRole(personId, "10013");
		List<Unit> unitList = null;
		if (memberBos != null && !memberBos.isEmpty()) {
			Set<String> unitNumbers = new HashSet<>();
			for (RoleMemberBo memberBo : memberBos) {
				List<RoleMemberAttributeDataBo> attributeDataBos = memberBo.getAttributeDetails();
				if (attributeDataBos != null && !attributeDataBos.isEmpty()) {
					for (RoleMemberAttributeDataBo bo : attributeDataBos) {
						unitNumbers.add(bo.getAttributeValue());
					}
				}
			}

			logger.info("create proposal unitNumber size : " + unitNumbers.size());
			if (!unitNumbers.isEmpty()) {
				unitList = proposalLookUpDao.fetchLeadUnitsByUnitNumbers(unitNumbers, searchString);
			}
		}
		return unitList;
	}

	@Override
	public List<BudgetCategory> findBudgetCategoryList(String searchString) {
		return proposalLookUpDao.findBudgetCategoryList(searchString);
	}

	@Override
	public List<Rolodex> findNonEmployeeList(String searchString) {
		return proposalLookUpDao.findNonEmployeeList(searchString);
	}

}
