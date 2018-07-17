package com.polus.fibicomp.grantcall.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachType;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachment;
import com.polus.fibicomp.grantcall.pojo.GrantCallCriteria;
import com.polus.fibicomp.grantcall.pojo.GrantCallEligibilityType;
import com.polus.fibicomp.grantcall.pojo.GrantCallStatus;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;

@Transactional
@Service(value = "grantCallDao")
public class GrantCallDaoImpl implements GrantCallDao {

	protected static Logger logger = Logger.getLogger(GrantCallDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<GrantCallType> fetchAllGrantCallTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantTypeCode"), "grantTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCallType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<GrantCallType> grantCallTypes = criteria.list();
		return grantCallTypes;
	}

	@Override
	public List<GrantCallStatus> fetchAllGrantCallStatus() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallStatus.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantStatusCode"), "grantStatusCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCallStatus.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<GrantCallStatus> grantCallStatus = criteria.list();
		return grantCallStatus;
	}

	@Override
	public List<ScienceKeyword> fetchAllScienceKeywords() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScienceKeyword.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("code"), "code");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScienceKeyword.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ScienceKeyword> keywords = criteria.list();
		return keywords;
	}

	@Override
	public List<SponsorType> fetchAllSponsorTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SponsorType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("code"), "code");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(SponsorType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<SponsorType> sponsorTypes = criteria.list();
		return sponsorTypes;
	}

	@Override
	public List<Sponsor> fetchSponsorsBySponsorType(String sponsorTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Sponsor.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("sponsorName"), "sponsorName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Sponsor.class));
		criteria.add(Restrictions.eq("sponsorTypeCode", sponsorTypeCode));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("sponsorName"));
		@SuppressWarnings("unchecked")
		List<Sponsor> sponsors = criteria.list();
		return sponsors;
	}

	@Override
	public List<ActivityType> fetchAllResearchTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("code"), "code");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ActivityType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ActivityType> researchTypes = criteria.list();
		return researchTypes;
	}

	@Override
	public List<FundingSourceType> fetchAllFundingSourceTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FundingSourceType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("fundingSourceTypeCode"), "fundingSourceTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(FundingSourceType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<FundingSourceType> fundingSourceTypes = criteria.list();
		return fundingSourceTypes;
	}

	@Override
	public List<GrantCallCriteria> fetchAllGrantCallCriteria() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallCriteria.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCriteriaCode"), "grantCriteriaCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCallCriteria.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<GrantCallCriteria> grantCallCriterias = criteria.list();
		return grantCallCriterias;
	}

	@Override
	public List<GrantCallEligibilityType> fetchAllEligibilityTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallEligibilityType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantEligibilityTypeCode"), "grantEligibilityTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCallEligibilityType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<GrantCallEligibilityType> eligibilityTypes = criteria.list();
		return eligibilityTypes;
	}

	@Override
	public List<GrantCallAttachType> fetchAllGrantCallAttachTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallAttachType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantAttachmentTypeCode"), "grantAttachmentTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCallAttachType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<GrantCallAttachType> attachTypes = criteria.list();
		return attachTypes;
	}

	@Override
	public GrantCall fetchGrantCallById(Integer grantCallId) {
		GrantCall grantCall = null;
		grantCall = hibernateTemplate.get(GrantCall.class, grantCallId);
		return grantCall;
	}

	@Override
	public GrantCall saveOrUpdateGrantCall(GrantCall grantCall) {
		hibernateTemplate.saveOrUpdate(grantCall);
		return grantCall;
	}

	@Override
	public GrantCallStatus fetchStatusByStatusCode(Integer grantStatusCode) {
		return hibernateTemplate.get(GrantCallStatus.class, grantStatusCode);
	}

	@Override
	public GrantCallAttachment fetchAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(GrantCallAttachment.class, attachmentId);
	}

	@Override
	public GrantCallType fetchGrantCallTypeByGrantTypeCode(Integer grantTypeCode) {
		return hibernateTemplate.get(GrantCallType.class, grantTypeCode);
	}

}
