package com.polus.fibicomp.budget.dao;

import java.math.BigDecimal;
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
import com.polus.fibicomp.budget.pojo.BudgetHeader;
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
	public List<InstituteRate> filterInstituteRateByDateRange(Date startDate, Date endDate) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(InstituteRate.class);
		criteria.add(Restrictions.ge("startDate", startDate));
		criteria.add(Restrictions.lt("startDate", endDate));
		criteria.addOrder(Order.asc("startDate"));
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

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal fetchApplicableRateByStartDate(Date budgetStartDate) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		BigDecimal applicableRate = BigDecimal.ZERO;
		Criteria criteria = session.createCriteria(FibiProposalRate.class);
		criteria.add(Restrictions.le("startDate", budgetStartDate));
		criteria.addOrder(Order.desc("startDate"));
		List<FibiProposalRate> proposalrate = criteria.list();
		if(proposalrate != null && !proposalrate.isEmpty()) {
			applicableRate = proposalrate.get(0).getApplicableRate();
		}
		return applicableRate;
	}

	@Override
	public BudgetHeader fetchBudgetByBudgetId(Long budgetId) {
		return hibernateTemplate.get(BudgetHeader.class, budgetId);
	}

	@Override
	public BudgetHeader saveOrUpdateBudget(BudgetHeader budgetHeader) {
		hibernateTemplate.saveOrUpdate(budgetHeader);
		return budgetHeader;
	}

}
