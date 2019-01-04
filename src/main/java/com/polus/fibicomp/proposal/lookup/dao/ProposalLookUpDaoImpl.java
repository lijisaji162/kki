package com.polus.fibicomp.proposal.lookup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.pojo.BudgetCategory;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Transactional
@Service(value = "proposalLookUpDao")
public class ProposalLookUpDaoImpl implements ProposalLookUpDao {

	protected static Logger logger = Logger.getLogger(ProposalLookUpDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<SponsorSearchResult> findSponsor(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		final String likeCriteria = "%" + searchString.toUpperCase() + "%";
		Query query = session.createQuery("SELECT NEW com.polus.fibicomp.vo.SponsorSearchResult(t.sponsorCode, t.sponsorName) " +
                "FROM Sponsor t " +
                "WHERE UPPER(t.sponsorCode) like :likeCriteria OR UPPER(t.acronym) like :likeCriteria or UPPER(t.sponsorName) like :likeCriteria");
		query.setParameter("likeCriteria", likeCriteria);
		return ListUtils.emptyIfNull(query.setMaxResults(25).list());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrantCall> getGrantCallList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		Conjunction and = Restrictions.conjunction();
		and.add(Restrictions.like("grantStatusCode", Constants.GRANT_CALL_STATUS_CODE_OPEN));
		and.add(Restrictions.like("grantCallName", "%" + searchString + "%").ignoreCase());
		criteria.addOrder(Order.asc("grantCallName"));
		criteria.add(and);
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<GrantCall> grantCall = criteria.list();
		List<GrantCall> grantCallList = new ArrayList<>();
		if(grantCall !=null && !grantCall.isEmpty()) {
			for(GrantCall grantCallObject : grantCall) {
				GrantCall grantCallObj = new GrantCall();
				grantCallObj.setGrantCallId(grantCallObject.getGrantCallId());
				grantCallObj.setGrantCallName(grantCallObject.getGrantCallName());
				grantCallObj.setSponsor(grantCallObject.getSponsor());
				grantCallObj.setFundingSourceType(grantCallObject.getFundingSourceType());
				grantCallObj.setActivityType(grantCallObject.getActivityType());
				grantCallObj.setGrantTheme(grantCallObject.getGrantTheme());
				grantCallObj.setGrantCallType(grantCallObject.getGrantCallType());
				grantCallList.add(grantCallObj);
			}
		}
		return grantCallList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> getDepartmentList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.like("unitName", "%" + searchString + "%").ignoreCase());
		or.add(Restrictions.like("unitNumber", "%" + searchString + "%").ignoreCase());
		criteria.add(or);
		criteria.addOrder(Order.asc("unitNumber"));
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Unit> departments = criteria.list();		
		return departments;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CostElement> findCostElementList(String searchString, String budgetCategoryCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CostElement.class);
		if(!budgetCategoryCode.isEmpty()) {
			criteria.add(Restrictions.eq("budgetCategoryCode", budgetCategoryCode).ignoreCase());
		}
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.like("description", "%" + searchString + "%").ignoreCase());
		or.add(Restrictions.like("costElement", "%" + searchString + "%").ignoreCase());
		criteria.add(or);
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CostElement> costElementList = criteria.list();
		return costElementList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScienceKeyword> findKeyWordsList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScienceKeyword.class);
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		criteria.add(Restrictions.like("description", "%" + searchString + "%").ignoreCase());
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ScienceKeyword> keyWordsList = criteria.list();
		return keyWordsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> fetchLeadUnitsByUnitNumbers(Set<String> unitNumbers, String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		criteria.add(Restrictions.in("unitNumber", unitNumbers));
		criteria.add(Restrictions.like("unitName", "%" + searchString + "%").ignoreCase());
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("unitName"));
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Unit> units = criteria.list();
		List<Unit>  unitList = new ArrayList<>();
		if(units !=null && !units.isEmpty()) {
			for(Unit unitObject : units) {
				Unit unitObj = new Unit();
				unitObj.setUnitNumber(unitObject.getUnitNumber());
				unitObj.setUnitName(unitObject.getUnitName());
				unitList.add(unitObj);
			}
		}
		return unitList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BudgetCategory> findBudgetCategoryList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BudgetCategory.class);
		criteria.add(Restrictions.like("description", "%" + searchString + "%").ignoreCase());
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<BudgetCategory> budgetCategories = criteria.list();
		return budgetCategories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rolodex> findNonEmployeeList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Rolodex.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.like("firstName", "%" + searchString + "%").ignoreCase());
		or.add(Restrictions.like("lastName", "%" + searchString + "%").ignoreCase());
		or.add(Restrictions.like("middleName", "%" + searchString + "%").ignoreCase());
		// or.add(Restrictions.like("organization", "%" + searchString + "%").ignoreCase());
		criteria.add(or);
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Rolodex> rolodex = criteria.list();
		List<Rolodex> rolodexList = new ArrayList<>();
		if(rolodex !=null && !rolodex.isEmpty()) {
			for(Rolodex rolodexObject : rolodex) {
				Rolodex rolodexObj = new Rolodex();
				rolodexObj.setRolodexId(rolodexObject.getRolodexId());
				rolodexObj.setFirstName(rolodexObject.getFirstName());
				rolodexObj.setLastName(rolodexObject.getLastName());
				rolodexObj.setMiddleName(rolodexObject.getMiddleName());
				rolodexObj.setOrganization(rolodexObject.getOrganization());
				rolodexObj.setPrefix(rolodexObject.getPrefix());
				rolodexList.add(rolodexObj);
			}
		}
		return rolodexList;
	}

}
