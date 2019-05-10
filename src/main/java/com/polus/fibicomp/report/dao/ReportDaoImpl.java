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
		/*criteria.createAlias("proposalStatus", "proposalStatus");
		//criteria.createAlias("proposalCategory", "proposalCategory");
		criteria.createAlias("activityType", "activityType");
		criteria.createAlias("proposalType", "proposalType");

		criteria.add(Restrictions.eq("grantCall.grantTypeCode", grantCallTypeCode));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("proposalId"), "proposalId");
		projList.add(Projections.property("title"), "title");
		//projList.add(Projections.property("proposalCategory.description"), "applicationCategory");
		projList.add(Projections.property("activityType.description"), "applicationCategory");
		projList.add(Projections.property("proposalType.description"), "applicationType");
		projList.add(Projections.property("proposalStatus.description"), "applicationStatus");
		projList.add(Projections.property("sponsorDeadlineDate"), "sponsorDeadlineDate");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));*/
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

	@Override
	public ReportVO fetchProposalsByPI(ReportVO reportVO) {
		logger.info("----------- fetchProposalsByPI ------------");
		String personId = reportVO.getPersonId();
		String rolodexId = reportVO.getRolodexId();
		logger.info("personId : " + personId);
		logger.info("rolodexId : " + rolodexId);
		// Conjunction and = Restrictions.conjunction();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Proposal.class);
			criteria.createAlias("proposalPersons", "proposalPersons");
			// criteria.add(Restrictions.conjunction().add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE)).add(Restrictions.eq("proposalPersons.personId", personId)));
			criteria.add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
			if (personId != null) {
				criteria.add(Restrictions.eq("proposalPersons.personId", personId));
			} else {
				criteria.add(Restrictions.eq("proposalPersons.rolodexId", rolodexId));
			}			
			criteria.add(Restrictions.eq("isInactive", false));
			@SuppressWarnings("unchecked")
			List<Proposal> proposals = criteria.list();
			List<Proposal> proposalList = new ArrayList<>();
			if(proposals!=null && !proposals.isEmpty()) {
				for(Proposal proposalObject : proposals) {
					Proposal propObj = new Proposal();
					propObj.setProposalId(proposalObject.getProposalId());
					propObj.setTitle(proposalObject.getTitle());
					propObj.setApplicationActivityType(proposalObject.getActivityType().getDescription());
					propObj.setApplicationType(proposalObject.getProposalType().getDescription());
					propObj.setApplicationStatus(proposalObject.getProposalStatus().getDescription());
					propObj.setSponsorDeadlineDate(proposalObject.getSponsorDeadlineDate());
					propObj.setProposalPersons(proposalObject.getProposalPersons());
					propObj.setSponsorName(proposalObject.getSponsorName());
					propObj.setHomeUnitName(proposalObject.getHomeUnitName());
					propObj.setSubmitUser(proposalObject.getSubmitUser());
					if (proposalObject.getBudgetHeader() != null) {
						propObj.setTotalCost(proposalObject.getBudgetHeader().getTotalCost());
						propObj.setTotalDirectCost(proposalObject.getBudgetHeader().getTotalDirectCost());
						propObj.setTotalIndirectCost(proposalObject.getBudgetHeader().getTotalIndirectCost());
					} else {
						propObj.setTotalCost(BigDecimal.ZERO);
						propObj.setTotalDirectCost(BigDecimal.ZERO);
						propObj.setTotalIndirectCost(BigDecimal.ZERO);
					}					
					proposalList.add(propObj);
				}
			}
			reportVO.setProposals(proposalList);
		} catch (Exception e) {
			logger.error("Error in method fetchProposalsByPI");
			e.printStackTrace();
		}
		return reportVO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getReportDataOfProposalsByPIForDownload(String personId, List<Object[]> proposals) throws Exception {
		try {
			logger.info("----------- getReportDataOfProposalsByPIForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
						"select DISTINCT t1.PROPOSAL_ID, t1.title, t2.FULL_NAME as PI, t3.DESCRIPTION as activity_type, t4.DESCRIPTION AS PROPOSAL_TYPE, T5.DESCRIPTION AS STATUS, T6.SPONSOR_NAME AS SPONSOR, t1.SPONSOR_DEADLINE_DATE, T7.TOTAL_DIRECT_COST, T7.TOTAL_INDIRECT_COST, T7.TOTAL_COST from fibi_proposal t1 inner join fibi_proposal_persons t2 on t1.PROPOSAL_ID = t2.PROPOSAL_ID and t2.PROP_PERSON_ROLE_ID = 3 left outer join activity_type t3 on t1.ACTIVITY_TYPE_CODE = t3.ACTIVITY_TYPE_CODE left outer join fibi_proposal_type t4 on t1.TYPE_CODE = t4.TYPE_CODE LEFT OUTER JOIN FIBI_PROPOSAL_STATUS T5 ON T1.STATUS_CODE = T5.STATUS_CODE LEFT OUTER JOIN SPONSOR T6 ON T1.SPONSOR_CODE =T6.SPONSOR_CODE LEFT OUTER JOIN FIBI_BUDGET_HEADER T7 ON T1.BUDGET_HEADER_ID = T7.BUDGET_HEADER_ID where t2.PERSON_ID = :personId or t2.ROLODEX_ID = :personId AND T1.is_inactive = 'N'");			
			proposalList.setString("personId", personId);
			proposals = proposalList.list();
			logger.info("Proposals By PI : " + proposals);
		} catch (Exception e) {
			logger.error("Error in method getReportDataOfProposalsByPIForDownload");
			e.printStackTrace();
		}
		return proposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> getProposalBySponsorTypes(String personId, List<String> sponsorCodes) {
		List<Proposal> proposalBySponsorTypes = new ArrayList<Proposal>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE, t5.DESCRIPTION as sponsor_type FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE left outer join sponsor_type t5 on t2.SPONSOR_TYPE_CODE = t5.SPONSOR_TYPE_CODE WHERE t2.sponsor_type_code in (:sponsorCodes) AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId) AND T1.is_inactive = 'N' ORDER BY t5.DESCRIPTION");		
			proposalList.setString("personId", personId);
			proposalList.setParameterList("sponsorCodes", sponsorCodes);
			proposalBySponsorTypes = proposalList.list();
			logger.info("proposalsBySponsorTypes : " + proposalBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getProposalBySponsorTypes");
			e.printStackTrace();
		}
		return proposalBySponsorTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getReportDataOfProposalsBySponsorTypeForDownload(String personId, List<String> sponsorCodes, List<Object[]> proposals) throws Exception {
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t2.sponsor_name, t5.DESCRIPTION as sponsor_type, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE left outer join sponsor_type t5 on t2.SPONSOR_TYPE_CODE = t5.SPONSOR_TYPE_CODE WHERE t2.sponsor_type_code in (:sponsorCodes) AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId) AND T1.is_inactive = 'N' ORDER BY t5.DESCRIPTION");		
			proposalList.setString("personId", personId);
			proposalList.setParameterList("sponsorCodes", sponsorCodes);
			proposals = proposalList.list();
			logger.info("proposalsBySponsorTypes : " + proposals);
		} catch (Exception e) {
			logger.error("Error in method getProposalBySponsorTypes");
			e.printStackTrace();
		}
		return proposals;
	}

}
