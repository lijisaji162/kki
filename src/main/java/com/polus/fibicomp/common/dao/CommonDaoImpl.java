package com.polus.fibicomp.common.dao;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.pojo.ParameterBo;
import com.polus.fibicomp.util.Truth;

@Transactional
@Service(value = "commonDao")
public class CommonDaoImpl implements CommonDao {

	protected static Logger logger = Logger.getLogger(CommonDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public Long getNextSequenceNumber(String sequenceName) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		String seqQuery = "select " + sequenceName + ".nextval as num from dual";
		SQLQuery query = session.createSQLQuery(seqQuery).addScalar("num", StandardBasicTypes.BIG_INTEGER);
		return ((BigInteger) query.uniqueResult()).longValue();
	}

	@Override
	public boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ParameterBo.class);
		criteria.add(Restrictions.eq("namespaceCode", namespaceCode));
		criteria.add(Restrictions.eq("componentCode", componentCode));
		criteria.add(Restrictions.eq("name", parameterName));
		ParameterBo parameterBo = (ParameterBo) criteria.uniqueResult();
		String value = parameterBo != null ? parameterBo.getValue() : null;
		return Truth.strToBooleanIgnoreCase(value);
	}

	@Override
	public Integer getParameter(String namespaceCode, String componentCode, String parameterName) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ParameterBo.class);
		criteria.add(Restrictions.eq("namespaceCode", namespaceCode));
		criteria.add(Restrictions.eq("componentCode", componentCode));
		criteria.add(Restrictions.eq("name", parameterName));
		ParameterBo parameterBo = (ParameterBo) criteria.uniqueResult();
		return Integer.parseInt(parameterBo.getValue());	
	}

	@Override
	public String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ParameterBo.class);
		criteria.add(Restrictions.eq("namespaceCode", namespaceCode));
		criteria.add(Restrictions.eq("componentCode", componentCode));
		criteria.add(Restrictions.eq("name", parameterName));
		ParameterBo parameterBo = (ParameterBo) criteria.uniqueResult();
		return parameterBo != null ? parameterBo.getValue() : null;
	}

}
