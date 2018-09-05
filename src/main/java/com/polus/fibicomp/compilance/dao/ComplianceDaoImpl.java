package com.polus.fibicomp.compilance.dao;

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

import com.polus.fibicomp.compilance.pojo.SpecialReviewApprovalType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewUsage;
import com.polus.fibicomp.constants.Constants;

@Transactional
@Service(value = "complianceDao")
public class ComplianceDaoImpl implements ComplianceDao {

	protected static Logger logger = Logger.getLogger(ComplianceDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<SpecialReviewType> fetchAllSpecialReviewType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SpecialReviewType.class);
		criteria.addOrder(Order.asc("sortId"));
		@SuppressWarnings("unchecked")
		List<SpecialReviewType> specialReviewTypes = criteria.list();
		return specialReviewTypes;
	}

	@Override
	public List<SpecialReviewUsage> fetchSpecialReviewUsageByModuleCode(String moduleCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SpecialReviewUsage.class);
		criteria.add(Restrictions.eq("moduleCode", moduleCode));
		criteria.add(Restrictions.eq("active", true));
		@SuppressWarnings("unchecked")
		List<SpecialReviewUsage> specialReviewUsages = criteria.list();
		return specialReviewUsages;
	}

	@Override
	public List<SpecialReviewApprovalType> fetchSpecialReviewApprovalTypeNotInCodes(List<String> approvalTypeCodes) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SpecialReviewApprovalType.class);
		criteria.add(Restrictions.not(Restrictions.in("approvalTypeCode", approvalTypeCodes)));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<SpecialReviewApprovalType> approvalTypes = criteria.list();
		return approvalTypes;
	}

}
