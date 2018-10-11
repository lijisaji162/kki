package com.polus.fibicomp.proposal.dao;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
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

import com.polus.fibicomp.compilance.pojo.ProposalSpecialReview;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.pojo.ProposalType;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Transactional
@Service(value = "proposalDao")
public class ProposalDaoImpl implements ProposalDao {

	protected static Logger logger = Logger.getLogger(ProposalDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public ProposalStatus fetchStatusByStatusCode(Integer statusCode) {
		return hibernateTemplate.get(ProposalStatus.class, statusCode);
	}

	@Override
	public List<Protocol> fetchAllProtocols() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Protocol.class);
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("updateTimestamp"));
		@SuppressWarnings("unchecked")
		List<Protocol> protocols = criteria.list();
		return protocols;
	}

	@Override
	public List<ProposalAttachmentType> fetchAllProposalAttachmentTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalAttachmentType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("attachmentTypeCode"), "attachmentTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalAttachmentType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalAttachmentType> attachmentTypes = criteria.list();
		return attachmentTypes;
	}

	@Override
	public List<GrantCall> fetchAllGrantCalls() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		criteria.add(Restrictions.like("grantStatusCode", Constants.GRANT_CALL_STATUS_CODE_OPEN));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCallId"), "grantCallId");
		projList.add(Projections.property("grantCallName"), "grantCallName");
		projList.add(Projections.property("sponsor"), "sponsor");
		projList.add(Projections.property("fundingSourceType"), "fundingSourceType");
		projList.add(Projections.property("activityType"), "activityType");
		projList.add(Projections.property("grantTheme"), "grantTheme");
		projList.add(Projections.property("grantCallType"), "grantCallType");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
		criteria.addOrder(Order.asc("grantCallName"));
		@SuppressWarnings("unchecked")
		List<GrantCall> grantCalls = criteria.list();
		return grantCalls;
	}

	@Override
	public List<ProposalPersonRole> fetchAllProposalPersonRoles() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalPersonRole.class);
		criteria.add(Restrictions.like("sponsorHierarchyName", "DEFAULT"));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalPersonRole> proposalPersonRoles = criteria.list();
		return proposalPersonRoles;
	}

	@Override
	public List<ProposalResearchType> fetchAllProposalResearchTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalResearchType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchTypeCode"), "researchTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalResearchType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalResearchType> proposalResearchTypes = criteria.list();
		return proposalResearchTypes;
	}

	@Override
	public Proposal saveOrUpdateProposal(Proposal proposal) {
		hibernateTemplate.saveOrUpdate(proposal);
		return proposal;
	}

	@Override
	public Proposal fetchProposalById(Integer proposalId) {
		return hibernateTemplate.get(Proposal.class, proposalId);
	}

	@Override
	public List<ProposalExcellenceArea> fetchAllAreaOfExcellence() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalExcellenceArea.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("excellenceAreaCode"), "excellenceAreaCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalExcellenceArea.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalExcellenceArea> proposalExcellenceAreas = criteria.list();
		return proposalExcellenceAreas;
	}

	@Override
	public ProposalAttachment fetchAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(ProposalAttachment.class, attachmentId);
	}

	@Override
	public List<ProposalType> fetchAllProposalTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("typeCode"), "typeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalType> proposalTypes = criteria.list();
		return proposalTypes;
	}

	@Override
	public List<ActivityType> fetchAllActivityTypes() {
		return hibernateTemplate.loadAll(ActivityType.class);
	}

	@Override
	public List<Sponsor> fetchAllSponsors() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Sponsor.class);
		criteria.add(Restrictions.not(Restrictions.like("sponsorName", "%DO NOT%")));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("sponsorName"), "sponsorName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Sponsor.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("sponsorName"));
		@SuppressWarnings("unchecked")
		List<Sponsor> sponsors = criteria.list();
		return sponsors;
	}

	@Override
	public List<Unit> fetchLeadUnitsByUnitNumbers(Set<String> unitNumbers) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		criteria.add(Restrictions.in("unitNumber", unitNumbers));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Unit.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("unitName"));
		@SuppressWarnings("unchecked")
		List<Unit> units = criteria.list();
		return units;
	}

	@Override
	public ProposalSpecialReview deleteProposalSpecialReview(ProposalSpecialReview specialReview) {
		hibernateTemplate.delete(specialReview);
		return specialReview;
	}

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

	@Override
	public String fetchSponsorTypeCodeBySponsorCode(String sponsorCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		String hql = "SELECT s.sponsorTypeCode FROM Sponsor s where s.sponsorCode = :sponsorCode";
		Query query = session.createQuery(hql);
		query.setParameter("sponsorCode",sponsorCode);
		String sponsorTypeCode = (String) query.uniqueResult();
		return sponsorTypeCode;
	}

	@Override
	public List<Unit> fetchAllUnits() {
		return hibernateTemplate.loadAll(Unit.class);
	}

}
