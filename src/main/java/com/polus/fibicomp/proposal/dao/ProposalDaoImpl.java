package com.polus.fibicomp.proposal.dao;

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
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalBudgetCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCostElement;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalInstituteCentreLab;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.pojo.ProposalType;

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
	public List<ProposalCategory> fetchAllCategories() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalCategory.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("categoryCode"), "categoryCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalCategory.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalCategory> categories = criteria.list();
		return categories;
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
	public List<ProposalBudgetCategory> fetchAllBudgetCategories() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalBudgetCategory.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("budgetCategoryCode"), "budgetCategoryCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalBudgetCategory.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalBudgetCategory> budgetCategories = criteria.list();
		return budgetCategories;
	}

	@Override
	public List<ProposalCostElement> fetchCostElementByBudgetCategory(String budgetCategoryCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalCostElement.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("costElement"), "costElement");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalCostElement.class));
		criteria.add(Restrictions.eq("budgetCategoryCode", budgetCategoryCode));
		// criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalCostElement> costElements = criteria.list();
		return costElements;
	}

	@Override
	public List<ProposalInstituteCentreLab> fetchAllInstituteCentrelabs() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalInstituteCentreLab.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("iclCode"), "iclCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalInstituteCentreLab.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalInstituteCentreLab> proposalInstituteCentreLabs = criteria.list();
		return proposalInstituteCentreLabs;
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
}
