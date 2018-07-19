package com.polus.fibicomp.budget.dao;

import java.util.Date;
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
import com.polus.fibicomp.budget.pojo.CostElement;

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

}
