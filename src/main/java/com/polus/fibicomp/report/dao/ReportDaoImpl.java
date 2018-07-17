package com.polus.fibicomp.report.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.ExpenditureByAwardView;
import com.polus.fibicomp.view.ProtocolView;

@Transactional
@Service(value = "reportDao")
public class ReportDaoImpl implements ReportDao {

	protected static Logger logger = Logger.getLogger(ReportDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<GrantCall> fetchOpenGrantIds() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		criteria.add(Restrictions.eq("grantStatusCode", Constants.GRANT_CALL_STATUS_CODE_OPEN));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCallId"), "grantCallId");
		projList.add(Projections.property("grantCallName"), "grantCallName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
		criteria.addOrder(Order.asc("grantCallId"));
		@SuppressWarnings("unchecked")
		List<GrantCall> grantIds = criteria.list();
		return grantIds;
	}

	@Override
	public ReportVO fetchApplicationByGrantCallId(ReportVO reportVO) {
		Integer grantCallId = reportVO.getGrantCallId();
		List<Integer> proposalStatus = new ArrayList<Integer>();
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_APPROVED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_SUBMITTED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_AWARDED);

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantCallId", grantCallId));
		criteria.add(Restrictions.in("statusCode", proposalStatus));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();	
		if (proposals != null && !proposals.isEmpty()) {
			reportVO.setProposalCount(proposals.size());
			reportVO.setProposals(proposals);
		}
		return reportVO;
	}

	@Override
	public List<Proposal> fetchApplicationsByGrantCallType(Integer grantCallTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.createAlias("grantCall", "grantCall");
		criteria.createAlias("proposalStatus", "proposalStatus");
		criteria.createAlias("proposalCategory", "proposalCategory");
		criteria.createAlias("proposalType", "proposalType");

		criteria.add(Restrictions.eq("grantCall.grantTypeCode", grantCallTypeCode));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("proposalId"), "proposalId");
		projList.add(Projections.property("title"), "title");
		projList.add(Projections.property("proposalCategory.description"), "applicationCategory");
		projList.add(Projections.property("proposalType.description"), "applicationType");
		projList.add(Projections.property("proposalStatus.description"), "applicationStatus");
		projList.add(Projections.property("submissionDate"), "submissionDate");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		//Long applicationsCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		return proposals;
	}

	@Override
	public List<ProtocolView> fetchProtocolsByProtocolType(String protocolTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolView.class);
		criteria.add(Restrictions.eq("protocolTypeCode", protocolTypeCode));
		criteria.add(Restrictions.eq("statusCode", Constants.PROTOCOL_SATUS_CODE_ACTIVE_OPEN_TO_ENTROLLMENT));
		//Long protocolsCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		@SuppressWarnings("unchecked")
		List<ProtocolView> protocols = criteria.list();
		return protocols;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProtocolType> fetchAllProtocolTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolType.class);
		List<ProtocolType> protocolTypes = criteria.list();
		return protocolTypes;
	}

	@Override
	public List<GrantCallType> fetchAllGrantCallTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallType.class);
		@SuppressWarnings("unchecked")
		List<GrantCallType> grantCallTypes = criteria.list();
		return grantCallTypes;
	}

	@Override
	public List<Integer> fetchProposalIdByGrantTypeCode(Integer grantTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantTypeCode", grantTypeCode));
		criteria.add(Restrictions.eq("statusCode", Constants.PROPOSAL_STATUS_CODE_AWARDED));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("ipNumber"), "ipNumber");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		criteria.addOrder(Order.asc("ipNumber"));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		if (proposals != null && !proposals.isEmpty()) {
			List<String> ipNumbers = new ArrayList<String>();
			String ipNumber = null;
			for (Proposal proposal : proposals) {
				ipNumber = proposal.getIpNumber();
				ipNumbers.add(ipNumber);
			}
			if (ipNumbers != null && !ipNumbers.isEmpty()) {
				Query query = session.createSQLQuery("select PROPOSAL_ID from PROPOSAL where PROPOSAL_NUMBER in (:ids)");
				query.setParameterList("ids", ipNumbers);
				@SuppressWarnings("unchecked")
				List<Integer> proposalId = query.list();
				return proposalId;
			}
		}
		return new ArrayList<Integer>();
	}

	@Override
	public List<Integer> fetchAwardCountByGrantType(List<Integer> proposalId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		//Query query = session.createSQLQuery("select count(AWARD_ID) from AWARD_FUNDING_PROPOSALS where PROPOSAL_ID in (:ids)");
		Query query = session.createSQLQuery("select AWARD_ID from AWARD_FUNDING_PROPOSALS where PROPOSAL_ID in (:ids)");
		query.setParameterList("ids", proposalId);
		/*BigDecimal bigDecimal = (BigDecimal) query.uniqueResult();
		Long count = bigDecimal.longValue();*/
		@SuppressWarnings("unchecked")
		List<BigDecimal> ids = query.list();
		List<Integer> awardIds = new ArrayList<Integer>();
		if (ids != null && !ids.isEmpty()) {
			for (BigDecimal val : ids) {
				awardIds.add(val.intValue());
			}
		}
		return awardIds;
	}

	@Override
	public ReportVO fetchAwardByGrantCallId(ReportVO reportVO) {
		Integer grantCallId = reportVO.getGrantCallId();
		logger.info("grantCallId : " + grantCallId);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantCallId", grantCallId));
		criteria.add(Restrictions.eq("statusCode", Constants.PROPOSAL_STATUS_CODE_AWARDED));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("ipNumber"), "ipNumber");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		if (proposals != null && !proposals.isEmpty()) {
			List<String> ipNumbers = new ArrayList<String>();
			String ipNumber = null;
			for (Proposal proposal : proposals) {
				ipNumber = proposal.getIpNumber();
				ipNumbers.add(ipNumber);
			}
			if (ipNumbers != null && !ipNumbers.isEmpty()) {
				Query query = session.createSQLQuery(
						"select AWARD_ID from AWARD_FUNDING_PROPOSALS where PROPOSAL_ID in (select PROPOSAL_ID from PROPOSAL where PROPOSAL_NUMBER in (:ids))");
				query.setParameterList("ids", ipNumbers);
				@SuppressWarnings("unchecked")
				List<BigDecimal> awardId = query.list();
				if (awardId != null && !awardId.isEmpty()) {
					List<Integer> awardIds = new ArrayList<Integer>();
					for (BigDecimal val : awardId) {
						awardIds.add(val.intValue());
					}
					Criteria searchCriteria = session.createCriteria(AwardView.class);
					searchCriteria.add(Restrictions.in("awardId", awardIds));/*
					searchCriteria.add(Restrictions.eq("personId", reportVO.getPersonId()));*/
					@SuppressWarnings("unchecked")
					List<AwardView> awardList = searchCriteria.list();
					if (awardList != null && !awardList.isEmpty()) {
						reportVO.setAwardCount(awardList.size());
						reportVO.setAwards(awardList);
					}
				}
			}
		}
		return reportVO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReportVO fetchExpenditureByAward(ReportVO reportVO) {
		String awardNumber = reportVO.getAwardNumber();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t1.LINE_ITEM_COST, t1.COST_ELEMENT, t2.DESCRIPTION from MITKC_AB_DETAIL t1 left outer join MITKC_AB_COST_ELEMENT t2 on t1.COST_ELEMENT = t2.COST_ELEMENT where t1.award_number = :awardNumber");
		query.setParameter("awardNumber", awardNumber);
		List<ExpenditureByAwardView> expenditureList = query.list();
		reportVO.setExpenditureList(expenditureList);
		return reportVO;
	}

	@Override
	public List<AwardView> fetchAwardNumbers() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AwardView.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("awardNumber"), "awardNumber");
		projList.add(Projections.property("title"), "title");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(AwardView.class));
		criteria.addOrder(Order.asc("awardNumber"));
		@SuppressWarnings("unchecked")
		List<AwardView> awardNumbers = criteria.list();
		return awardNumbers;
	}

	@Override
	public List<AwardView> fetchAwardByAwardNumbers(List<Integer> awardIds) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AwardView.class);
		criteria.add(Restrictions.in("awardId", awardIds));
		@SuppressWarnings("unchecked")
		List<AwardView> awardList = criteria.list();
		return awardList;
	}

}
