package com.polus.fibicomp.budget.dao;

import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.budget.common.pojo.ValidCeRateType;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;

@Transactional
@Service(value = "budgetDao")
public class BudgetDaoImpl implements BudgetDao {

	protected static Logger logger = Logger.getLogger(BudgetDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<InstituteRate> filterInstituteRateByDateRange(Date startDate, Date endDate, String activityTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(InstituteRate.class);
		criteria.add(Restrictions.eq("activityTypeCode", activityTypeCode));
		criteria.add(Restrictions.between("startDate", startDate, endDate));
		criteria.addOrder(Order.asc("startDate"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<InstituteRate> instituteRates = criteria.list();
		return instituteRates;
	}

	@Override
	public List<CostElement> getAllCostElements() {
		return hibernateTemplate.loadAll(CostElement.class);
	}

	@Override
	public RateType getOHRateTypeByParams(String rateClassCode, String rateTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(RateType.class);
		criteria.add(Restrictions.eq("rateClassCode", rateClassCode));
		criteria.add(Restrictions.eq("rateTypeCode", rateTypeCode));
		RateType rateType = (RateType) criteria.uniqueResult();
		return rateType;
	}

	@Override
	public FibiProposalRate fetchApplicableProposalRate(Integer budgetId, Date budgetStartDate, String rateClassCode, String rateTypeCode, String activityTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		FibiProposalRate applicableRate = null;
		Criteria criteria = session.createCriteria(FibiProposalRate.class);
		criteria.add(Restrictions.eq("budgetHeader.budgetId", budgetId));
		criteria.add(Restrictions.le("startDate", budgetStartDate));
		criteria.add(Restrictions.eq("rateClassCode", rateClassCode));
		criteria.add(Restrictions.eq("rateTypeCode", rateTypeCode));
		criteria.add(Restrictions.eq("activityTypeCode", activityTypeCode));
		criteria.addOrder(Order.desc("startDate"));
		@SuppressWarnings("unchecked")
		List<FibiProposalRate> proposalrate = criteria.list();
		if(proposalrate != null && !proposalrate.isEmpty()) {
			applicableRate = proposalrate.get(0);
		}
		return applicableRate;
	}

	@Override
	public BudgetHeader fetchBudgetByBudgetId(Integer budgetId) {
		return hibernateTemplate.get(BudgetHeader.class, budgetId);
	}

	@Override
	public void saveOrUpdateBudget(BudgetHeader budgetHeader) {
		hibernateTemplate.merge(budgetHeader);//(budgetHeader);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidCeRateType> fetchCostElementRateTypes(String costElement) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ValidCeRateType.class);
		criteria.add(Restrictions.eq("costElement", costElement));
		List<ValidCeRateType> ceRateTypes = criteria.list();
		return ceRateTypes;
	}

	@Override
	public InstituteRate fetchInstituteRateByDateLessthanMax(Date startDate, String activityTypeCode) {
		InstituteRate instituteRate = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(InstituteRate.class);
		criteria.add(Restrictions.eq("activityTypeCode", activityTypeCode));
		criteria.add(Restrictions.le("startDate", startDate));
		criteria.addOrder(Order.desc("startDate"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<InstituteRate> instituteRates = criteria.list();
		if (instituteRates != null && !instituteRates.isEmpty()) {
			instituteRate = instituteRates.get(0);
		}
		return instituteRate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CostElement> fetchCostElementsByIds(List<String> costElements) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CostElement.class);
		criteria.add(Restrictions.in("costElement", costElements));
		return criteria.list();
	}

	@Override
	public BudgetPeriod getMaxBudgetPeriodByBudgetId(Integer budgetId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BudgetPeriod.class);
		criteria.add(Restrictions.eq("budget.budgetId", budgetId));
		criteria.addOrder(Order.desc("budgetPeriod"));
		criteria.setMaxResults(1);
		BudgetPeriod budgetPeriod = (BudgetPeriod) criteria.uniqueResult();
		return budgetPeriod;
	}

	@Override
	public CostElement fetchCostElementsById(String costElement) {
		return hibernateTemplate.get(CostElement.class, costElement);
	}

}
