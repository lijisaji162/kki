package com.polus.fibicomp.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.ParameterBo;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.DisclosureView;
import com.polus.fibicomp.view.ExpenditureVolume;
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.MobileProposalView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryPieChart;
import com.polus.fibicomp.view.ResearchSummaryView;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "dashboardDao")
@PropertySource("classpath:application.properties")
public class DashboardDaoImpl implements DashboardDao {

	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public String getDashBoardResearchSummary(String person_id) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ExpenditureVolume> expenditureVolumeChart = new ArrayList<ExpenditureVolume>();
		List<ResearchSummaryView> summaryTable = new ArrayList<ResearchSummaryView>();
		List<ResearchSummaryPieChart> summaryAwardPiechart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryProposalPiechart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryProposalDonutChart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryAwardDonutChart = new ArrayList<ResearchSummaryPieChart>();
		try {
			logger.info("---------- getDashBoardResearchSummary -----------");
			expenditureVolumeChart = getExpenditureVolumeChart(person_id, expenditureVolumeChart);
			logger.info("expenditureVolumeChart : " + expenditureVolumeChart);
			summaryTable = getSummaryTable(person_id, summaryTable);
			logger.info("summaryTable : " + summaryTable);
			summaryAwardPiechart = getSummaryAwardPieChart(person_id, summaryAwardPiechart);
			logger.info("summaryAwardPiechart : " + summaryAwardPiechart);
			summaryProposalPiechart = getSummaryProposalPieChart(person_id, summaryProposalPiechart);
			logger.info("summaryProposalPiechart : " + summaryProposalPiechart);
			summaryProposalDonutChart = getSummaryInProgressProposalDonutChart(person_id, summaryProposalDonutChart);
			logger.info("summaryProposalDonutChart : " + summaryProposalDonutChart);
			summaryAwardDonutChart = getSummaryAwardedProposalDonutChart(person_id, summaryAwardDonutChart);
			logger.info("summaryAwardDonutChart : " + summaryAwardDonutChart);

			dashBoardProfile.setExpenditureVolumes(expenditureVolumeChart);
			dashBoardProfile.setSummaryViews(summaryTable);
			dashBoardProfile.setSummaryAwardPieChart(summaryAwardPiechart);
			dashBoardProfile.setSummaryProposalPieChart(summaryProposalPiechart);
			dashBoardProfile.setSummaryProposalDonutChart(summaryProposalDonutChart);
			dashBoardProfile.setSummaryAwardDonutChart(summaryAwardDonutChart);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardResearchSummary");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureVolume> getExpenditureVolumeChart(String person_id,
			List<ExpenditureVolume> expenditureVolumeChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query expenditureVolume = null;
		if (oracledb.equals("Y")) {
			expenditureVolume = session.createSQLQuery(
					"SELECT to_char(t4.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(T4.total_direct_cost) AS Direct_Cost, Sum(T4.total_indirect_cost) AS FA FROM eps_proposal t1 INNER JOIN eps_proposal_budget_ext t2 ON t1.proposal_number = t2.proposal_number INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN budget_periods t4 ON t3.budget_id = t4.budget_id WHERE t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY TO_CHAR(t4.start_date, 'yyyy') ORDER BY To_Number(TO_CHAR(t4.start_date, 'yyyy'))");
		} else {
			expenditureVolume = session.createSQLQuery(
					"SELECT year(t4.start_date) AS BUDGET_PERIOD, Sum(T4.total_direct_cost) AS Direct_Cost, Sum(T4.total_indirect_cost) AS FA FROM eps_proposal t1 INNER JOIN eps_proposal_budget_ext t2 ON t1.proposal_number = t2.proposal_number INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN budget_periods t4 ON t3.budget_id = t4.budget_id WHERE t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY year(t4.start_date) ORDER BY year(t4.start_date)");
		}
		expenditureVolume.setString("person_id", person_id);
		expenditureVolumeChart = expenditureVolume.list();
		return expenditureVolumeChart;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryView> getSummaryTable(String person_id, List<ResearchSummaryView> summaryTable) {
		List<ResearchSummaryView> subPropCount = null;
		List<ResearchSummaryView> inPropCount = null;
		List<ResearchSummaryView> activeAwardsCount = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query submittedProposal = session.createSQLQuery(
				"select 'Submitted Application' as Submitted_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		submittedProposal.setString("person_id", person_id);
		subPropCount = submittedProposal.list();
		if (subPropCount != null && !subPropCount.isEmpty()) {
			summaryTable.addAll(subPropCount);
		}

		Query inprogressProposal = session.createSQLQuery(
				"select 'In Progress Application' as In_Progress_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=1 and  t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		inprogressProposal.setString("person_id", person_id);
		inPropCount = inprogressProposal.list();
		if (inPropCount != null && !inPropCount.isEmpty()) {
			summaryTable.addAll(inPropCount);
		}

		Query activeAwards = session.createSQLQuery(
				"select 'Active Projects' as Active_Award, count(t1.award_id),sum(t3.TOTAL_COST) as total_amount from AWARD t1 inner join AWARD_BUDGET_EXT t2 on t1.award_id=t2.award_id inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.award_sequence_status = 'ACTIVE' and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Award' and person_id = :person_id )");
		activeAwards.setString("person_id", person_id);
		activeAwardsCount = activeAwards.list();
		if (activeAwardsCount != null && !activeAwardsCount.isEmpty()) {
			summaryTable.addAll(activeAwardsCount);
		}
		return summaryTable;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryAwardPieChart(String person_id,
			List<ResearchSummaryPieChart> summaryAwardPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.award_sequence_status = 'ACTIVE' and t1.LEAD_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
		query.setString("person_id", person_id);
		return summaryAwardPiechart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryProposalPieChart(String person_id,
			List<ResearchSummaryPieChart> summaryProposalPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION as sponsor_type,count(1) from eps_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.OWNED_BY_UNIT in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION");
		query.setString("person_id", person_id);
		return summaryProposalPiechart = query.list();
	}

	public DashBoardProfile getDashBoardDataForAward(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("--------- getDashBoardDataForAward ---------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(AwardView.class);
			Criteria countCriteria = session.createCriteria(AwardView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("accountNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("unitName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("fullName", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			countCriteria.add(and);
			countCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<AwardView> awards = searchCriteria.list();
			dashBoardProfile.setAwardViews(awards);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForAward", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("---------- getDashBoardDataForProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProposalView.class);
			Criteria countCriteria = session.createCriteria(ProposalView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("proposalNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<ProposalView> proposals = searchCriteria.list();
			dashBoardProfile.setProposalViews(proposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForProposal", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getProtocolDashboardData(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("--------- getProtocolDashboardData -----------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProtocolView.class);
			Criteria countCriteria = session.createCriteria(ProtocolView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<ProtocolView> protocols = searchCriteria.list();
			dashBoardProfile.setProtocolViews(protocols);
		} catch (Exception e) {
			logger.error("Error in method getProtocolDashboardData", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForIacuc(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("---------- getDashBoardDataForIacuc -----------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(IacucView.class);
			Criteria countCriteria = session.createCriteria(IacucView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<IacucView> iacucProtocols = searchCriteria.list();
			dashBoardProfile.setIacucViews(iacucProtocols);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForIacuc");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForDisclosures(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForDisclosures ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(DisclosureView.class);
			Criteria countCriteria = session.createCriteria(DisclosureView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("coiDisclosureNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("fullName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("disclosureDisposition", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("moduleItemKey", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<DisclosureView> disclosures = searchCriteria.list();
			dashBoardProfile.setDisclosureViews(disclosures);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForDisclosures");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionItem> getUserNotification(String principalId) {
		List<ActionItem> actionLists = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActionItem.class);
		criteria.add(Restrictions.eq("principalId", principalId));
		actionLists = criteria.list();
		return actionLists;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAwardBySponsorTypes(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> awardBySponsorTypes = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query awardList = session.createSQLQuery(
					"SELECT t1.sequence_number, t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId)");
			awardList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awardBySponsorTypes = awardList.list();
			logger.info("awardsBySponsorTypes : " + awardBySponsorTypes);
			dashBoardProfile.setAwardViews(awardBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getPieChartAwardbySponsorTypes");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getProposalBySponsorTypes(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> proposalBySponsorTypes = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
					"SELECT t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI FROM eps_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN eps_prop_person t3 ON t1.proposal_number = t3.proposal_number AND t3.prop_person_role_id = 'PI' INNER JOIN proposal_type t4 ON t1.PROPOSAL_TYPE_CODE=t4.PROPOSAL_TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM   mitkc_user_right_mv WHERE  perm_nm = 'View Proposal' AND person_id = :personId)");
			proposalList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			proposalBySponsorTypes = proposalList.list();
			logger.info("proposalsBySponsorTypes : " + proposalBySponsorTypes);
			dashBoardProfile.setProposalViews(proposalBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getPieChartAwardbySponsorTypes");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getProposalsInProgress(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query progressProposalList = session.createSQLQuery(
					"select t1.document_number, t1.proposal_number, t1.title, t5.sponsor_name, t3.TOTAL_COST, t4.full_name AS PI ,t1.OWNED_BY_UNIT as unit_number,t6.UNIT_NAME from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.OWNED_BY_UNIT= t6.UNIT_NUMBER where t1.status_code=1 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :personId )");
			progressProposalList.setString("personId", personId);
			List<Object[]> proposals = progressProposalList.list();
			for (Object[] proposal : proposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setDocumentNumber(proposal[0].toString());
				proposalView.setProposalNumber(proposal[1].toString());
				proposalView.setTitle(proposal[2].toString());
				proposalView.setSponsor(proposal[3].toString());
				proposalView.setTotalCost(proposal[4].toString());
				proposalView.setFullName(proposal[5].toString());
				proposalView.setLeadUnit(proposal[7].toString());
				inProgressProposals.add(proposalView);
			}
			logger.info("getProposalsInProgress : " + inProgressProposals);
			dashBoardProfile.setProposalViews(inProgressProposals);
		} catch (Exception e) {
			logger.error("Error in method getProposalsInProgress");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getSubmittedProposals(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> submittedProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query subproposalList = session.createSQLQuery(
					"select t1.document_number, t1.proposal_number, t1.title, t5.sponsor_name, t3.TOTAL_COST, t4.full_name AS PI, t1.OWNED_BY_UNIT as unit_number,t6.UNIT_NAME from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.OWNED_BY_UNIT= t6.UNIT_NUMBER where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :personId)");
			subproposalList.setString("personId", personId);
			List<Object[]> subProposals = subproposalList.list();
			for (Object[] proposal : subProposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setDocumentNumber(proposal[0].toString());
				proposalView.setProposalNumber(proposal[1].toString());
				proposalView.setTitle(proposal[2].toString());
				proposalView.setSponsor(proposal[3].toString());
				proposalView.setTotalCost(proposal[4].toString());
				proposalView.setFullName(proposal[5].toString());
				proposalView.setLeadUnit(proposal[7].toString());
				submittedProposals.add(proposalView);
			}
			logger.info("SubmittedProposals : " + submittedProposals);
			dashBoardProfile.setProposalViews(submittedProposals);
		} catch (Exception e) {
			logger.error("Error in method getSubmittedProposals");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getActiveAwards(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> activeAwards = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query activeAwardList = session.createSQLQuery(
					"SELECT t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 INNER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId)");
			activeAwardList.setString("personId", personId);
			List<Object[]> activeAwardsList = activeAwardList.list();
			for (Object[] award : activeAwardsList) {
				AwardView awardView = new AwardView();
				awardView.setAwardId(Integer.valueOf(award[0].toString()));
				awardView.setDocumentNumber(award[1].toString());
				awardView.setAwardNumber(award[2].toString());
				if (award[3] != null) {
					awardView.setAccountNumber(award[3].toString());
				}
				awardView.setTitle(award[4].toString());
				if (award[5] != null) {
					awardView.setSponsor(award[5].toString());
				}
				if (award[6] != null) {
					awardView.setFullName(award[6].toString());
				}
				if (award[7] != null) {
					awardView.setTotal_cost(award[7].toString());
				}
				activeAwards.add(awardView);
			}
			logger.info("Active Awards : " + activeAwards);
			dashBoardProfile.setAwardViews(activeAwards);
		} catch (Exception e) {
			logger.error("Error in method getActiveAwards");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryAwardedProposalDonutChart(String person_id,
			List<ResearchSummaryPieChart> summaryAwardDonutChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from proposal t1 inner join SPONSOR t2 on t1.sponsor_code = t2.sponsor_code where t1.status_code = 2 and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id ) group by t1.sponsor_code,t2.SPONSOR_NAME");
		query.setString("person_id", person_id);
		return summaryAwardDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryInProgressProposalDonutChart(String person_id,
			List<ResearchSummaryPieChart> summaryProposalDonutChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from eps_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code where t1.status_code=1 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t1.sponsor_code,t2.SPONSOR_NAME");
		query.setString("person_id", person_id);
		return summaryProposalDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getInProgressProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = session.createSQLQuery(
					"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.PROPOSAL_TYPE_CODE, t5.DESCRIPTION as Proposal_Type, t7.TOTAL_COST as BUDGET from eps_proposal t1 LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN proposal_type t5 ON t1.PROPOSAL_TYPE_CODE=t5.PROPOSAL_TYPE_CODE LEFT OUTER JOIN eps_proposal_budget_ext t6 ON t1.proposal_number = t6.proposal_number LEFT OUTER JOIN budget t7 ON t6.budget_id = t7.budget_id and t7.final_version_flag = 'Y' where t1.status_code=1 and t1.sponsor_code = :sponsorCode and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId )");
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			inProgressProposal = proposalQuery.list();
			logger.info("inProgressProposal : " + inProgressProposal);
			dashBoardProfile.setProposalViews(inProgressProposal);
		} catch (Exception e) {
			logger.error("Error in method getInProgressProposalsBySponsor");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAwardedProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> awardedProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = session.createSQLQuery(
					"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.ACTIVITY_TYPE_CODE, t5.DESCRIPTION as ACTIVITY_TYPE, T1.PROPOSAL_TYPE_CODE, T6.description as PROPOSAL_TYPE, t1.sponsor_code from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId ) ");
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awardedProposal = proposalQuery.list();
			logger.info("awardedProposal : " + awardedProposal);
			dashBoardProfile.setProposalViews(awardedProposal);
		} catch (Exception e) {
			logger.error("Error in method getAwardedProposalsBySponsor");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getFibiSummaryTable(String personId, List<Object[]> summaryTable) {

		List<Object[]> subPropCount = null;
		List<Object[]> inPropCount = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query submittedProposal = session.createSQLQuery(
				"select 'Submitted Proposal' as Submitted_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		submittedProposal.setString("person_id", personId);
		subPropCount = submittedProposal.list();
		if (subPropCount != null && !subPropCount.isEmpty()) {
			summaryTable.addAll(subPropCount);
		}

		Query inprogressProposal = session.createSQLQuery(
				"select 'In Progress Proposal' as In_Progress_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=1 and  t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		inprogressProposal.setString("person_id", personId);
		inPropCount = inprogressProposal.list();
		if (inPropCount != null && !inPropCount.isEmpty()) {
			summaryTable.addAll(inPropCount);
		}

		return summaryTable;
	}

	@Override
	public List<MobileProposalView> getProposalsByParams(CommonVO vo) {
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		String personId = vo.getPersonId();
		List<MobileProposalView> proposalViews = null;

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("---------- getProposalsByParams ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProposalView.class);
			searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("proposalNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
			}
			searchCriteria.add(and);

			@SuppressWarnings("unchecked")
			List<ProposalView> proposals = searchCriteria.list();
			if (proposals != null && !proposals.isEmpty()) {
				proposalViews = new ArrayList<MobileProposalView>();
				for (ProposalView proposal : proposals) {
					if (proposal.getProposalPersonRoleCode() == null
							|| proposal.getProposalPersonRoleCode().equals("PI")) {
						MobileProposalView mobileProposal = new MobileProposalView();
						mobileProposal.setDocumentNo(proposal.getDocumentNumber());
						mobileProposal.setLeadUnit(proposal.getLeadUnit());
						mobileProposal.setLeadUnitNo(proposal.getLeadUnitNumber());
						mobileProposal.setPi(proposal.getFullName());
						mobileProposal.setProposalNo(proposal.getProposalNumber());
						mobileProposal.setSponsor(proposal.getSponsor());
						mobileProposal.setStatus(proposal.getStatus());
						mobileProposal.setTitle(proposal.getTitle());
						mobileProposal.setVersionNo(String.valueOf(proposal.getVersionNumber()));
						mobileProposal.setCertified(proposal.isCertified());
						mobileProposal.setProposalPersonRoleId(proposal.getProposalPersonRoleCode());
						if (proposal.getStatusCode() == 1 && proposal.getProposalPersonRoleCode() != null) {
							String hierarchyName = getSponsorHierarchy(proposal.getSponsorCode());
							Criteria roleCriteria = session.createCriteria(ProposalPersonRole.class);
							roleCriteria.add(Restrictions.eq("code", proposal.getProposalPersonRoleCode()));
							roleCriteria.add(Restrictions.eq("sponsorHierarchyName", hierarchyName));
							ProposalPersonRole personRole = (ProposalPersonRole) roleCriteria.uniqueResult();
							if (personRole != null) {
								mobileProposal.setCertificationRequired(personRole.getCertificationRequired());
								mobileProposal.setRoleName(personRole.getDescription());
							}
							mobileProposal.setActionRequestCode("C");
						}
						if (proposal.getStatusCode() == 2) {
							mobileProposal.setActionRequestCode("A");
						}
						mobileProposal.setPersonId(proposal.getPersonId());
						mobileProposal.setPersonName(
								hibernateTemplate.get(PrincipalBo.class, proposal.getPersonId()).getPrincipalName());
						mobileProposal.setProposalPersonRoleId(proposal.getProposalPersonRoleCode());
						proposalViews.add(mobileProposal);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in method getProposalsByParams", e);
			e.printStackTrace();
		}
		return proposalViews;
	}

	public String getSponsorHierarchy(String sponsorCode) {
		if (areAllSponsorsMultiPi()) {
			return Constants.NIH_MULTIPLE_PI_HIERARCHY;
		}
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		for (String hierarchyName : getRoleHierarchies()) {
			Query countQuery = session.createSQLQuery(
					"select count(1) from sponsor_hierarchy s where sponsor_code=:sponsorCode and hierarchy_name=:hierarchyName");
			countQuery.setString("sponsorCode", sponsorCode);
			countQuery.setString("hierarchyName", hierarchyName);
			BigDecimal count = (BigDecimal) countQuery.uniqueResult();
			if (count.intValue() > 0) {
				return hierarchyName;
			}
		}
		return Constants.DEFAULT_SPONSOR_HIERARCHY_NAME;
	}

	public Boolean areAllSponsorsMultiPi() {
		Boolean isMultiPI = false;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ParameterBo.class);
		criteria.add(Restrictions.eq("namespaceCode", Constants.MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT));
		criteria.add(Restrictions.eq("componentCode", Constants.DOCUMENT_COMPONENT));
		criteria.add(Restrictions.eq("name", Constants.ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI));
		criteria.add(Restrictions.eq("applicationId", Constants.KC));
		ParameterBo parameterBo = (ParameterBo) criteria.uniqueResult();
		String value = parameterBo != null ? parameterBo.getValue() : null;
		if (value == null) {
			isMultiPI = false;
		} else if (value.equalsIgnoreCase("N")) {
			isMultiPI = false;
		} else {
			isMultiPI = true;
		}
		return isMultiPI;
	}

	protected Collection<String> getRoleHierarchies() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ParameterBo.class);
		criteria.add(Restrictions.eq("namespaceCode", Constants.KC_GENERIC_PARAMETER_NAMESPACE));
		criteria.add(Restrictions.eq("componentCode", Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE));
		criteria.add(Restrictions.eq("name", Constants.SPONSOR_HIERARCHIES_PARM));
		criteria.add(Restrictions.eq("applicationId", Constants.KC));
		ParameterBo parameterBo = (ParameterBo) criteria.uniqueResult();
		String strValues = parameterBo.getValue();
		if (strValues == null || StringUtils.isBlank(strValues)) {
			return Collections.emptyList();
		}
		final Collection<String> values = new ArrayList<String>();
		for (String value : strValues.split(",")) {
			values.add(value.trim());
		}

		return Collections.unmodifiableCollection(values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MobileProposalView> getProposalsForCertification(String personId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalView.class);
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("certified", false));
		criteria.add(Restrictions.eq("statusCode", 1));
		List<ProposalView> proposalViews = criteria.list();
		List<MobileProposalView> mobileProposalViews = new ArrayList<MobileProposalView>();
		if (proposalViews != null && !proposalViews.isEmpty()) {
			for (ProposalView view : proposalViews) {
				if (view.getProposalPersonRoleCode() != null && view.getProposalPersonRoleCode().equals("PI")) {
					MobileProposalView proposalView = new MobileProposalView();
					proposalView.setDocumentNo(view.getDocumentNumber());
					proposalView.setTitle(view.getTitle());
					proposalView.setLeadUnit(view.getLeadUnit());
					proposalView.setProposalNo(view.getProposalNumber());
					proposalView.setPi(view.getFullName());
					proposalView.setSponsor(view.getSponsor());
					proposalView.setPersonId(view.getPersonId());
					proposalView.setPersonName(hibernateTemplate.get(PrincipalBo.class, personId).getPrincipalName());
					proposalView.setProposalPersonRoleId(view.getProposalPersonRoleCode());
					proposalView.setActionRequestCode("C");
					mobileProposalViews.add(proposalView);
				}
			}
		}
		return mobileProposalViews;
	}

	@Override
	public DashBoardProfile getDashBoardDataForCommittee(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForCommittee ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(Committee.class);
			Criteria countCriteria = session.createCriteria(Committee.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimestamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("committeeId", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("committeeName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("homeUnitNumber", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("homeUnitName", "%" + property4 + "%").ignoreCase());
			}

			searchCriteria.add(and);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("committeeId"), "committeeId");
			projList.add(Projections.property("committeeName"), "committeeName");
			projList.add(Projections.property("homeUnitNumber"), "homeUnitNumber");
			projList.add(Projections.property("homeUnitName"), "homeUnitName");
			projList.add(Projections.property("reviewTypeDescription"), "reviewTypeDescription");
			projList.add(Projections.property("description"), "description");
			searchCriteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Committee.class));
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<Committee> committees = searchCriteria.list();
			dashBoardProfile.setCommittees(committees);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForCommittee");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForCommitteeSchedule(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		Date filterStartDate = vo.getFilterStartDate();
		Date filterEndDate = vo.getFilterEndDate();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForCommitteeSchedule ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(CommitteeSchedule.class);
			searchCriteria.createAlias("committee", "committee", JoinType.LEFT_OUTER_JOIN);
			Criteria countCriteria = session.createCriteria(CommitteeSchedule.class);
			countCriteria.createAlias("committee", "committee", JoinType.LEFT_OUTER_JOIN);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimestamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("scheduleId", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("place", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("committee.committeeId", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("committee.committeeName", "%" + property4 + "%").ignoreCase());
			}

			searchCriteria.add(and);
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);

			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("scheduleId"), "scheduleId");
			projList.add(Projections.property("scheduledDate"), "scheduledDate");
			projList.add(Projections.property("place"), "place");
			projList.add(Projections.property("protocolSubDeadline"), "protocolSubDeadline");
			projList.add(Projections.property("committee"), "committee");
			projList.add(Projections.property("scheduleStatus"), "scheduleStatus");

			searchCriteria.setProjection(projList).setResultTransformer(new AliasToBeanResultTransformer(CommitteeSchedule.class));
			@SuppressWarnings("unchecked")
			List<CommitteeSchedule> committeeSchedules = searchCriteria.list();
			Date scheduleDate = null;
			if (filterStartDate != null && filterEndDate != null) {
				Date startDate = DateUtils.addDays(filterStartDate, -1);
				Date endDate = DateUtils.addDays(filterEndDate, 1);
				List<CommitteeSchedule> filteredSchedules = new ArrayList<CommitteeSchedule>();
				for (CommitteeSchedule schedule : committeeSchedules) {
					scheduleDate = schedule.getScheduledDate();
					if ((scheduleDate != null) && scheduleDate.after(startDate) && scheduleDate.before(endDate)) {
						filteredSchedules.add(schedule);
					}
				}
				dashBoardProfile.setCommitteeSchedules(filteredSchedules);
			} else {
				dashBoardProfile.setCommitteeSchedules(committeeSchedules);
			}
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForCommitteeSchedule");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForGrantCall(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		boolean isUnitAdmin = vo.getIsUnitAdmin();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForGrantCall ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(GrantCall.class);
			searchCriteria.createAlias("grantCallType", "grantCallType");
			searchCriteria.createAlias("sponsor", "sponsor", JoinType.LEFT_OUTER_JOIN);
			searchCriteria.createAlias("grantCallStatus", "grantCallStatus");

			Criteria countCriteria = session.createCriteria(GrantCall.class);
			countCriteria.createAlias("grantCallType", "grantCallType");
			countCriteria.createAlias("sponsor", "sponsor", JoinType.LEFT_OUTER_JOIN);
			countCriteria.createAlias("grantCallStatus", "grantCallStatus");
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				Integer grantCallId = Integer.valueOf(property1);
				and.add(Restrictions.like("grantCallId", grantCallId));
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("grantCallName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("grantCallType.description", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("sponsor.sponsorName", "%" + property4 + "%").ignoreCase());
			}

			if (!isUnitAdmin) {
				searchCriteria.add(Restrictions.eq("grantStatusCode", Constants.GRANT_CALL_STATUS_CODE_OPEN));
				countCriteria.add(Restrictions.eq("grantStatusCode", Constants.GRANT_CALL_STATUS_CODE_OPEN));
			}
			searchCriteria.add(and);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("grantCallId"), "grantCallId");
			projList.add(Projections.property("grantCallName"), "grantCallName");
			projList.add(Projections.property("grantCallType.description"), "grantCallTypeDesc");
			projList.add(Projections.property("openingDate"), "openingDate");
			projList.add(Projections.property("closingDate"), "closingDate");
			projList.add(Projections.property("sponsor.sponsorName"), "sponsorName");
			projList.add(Projections.property("grantCallStatus.description"), "grantCallStatusDesc");
			searchCriteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<GrantCall> grantCalls = searchCriteria.list();
			dashBoardProfile.setGrantCalls(grantCalls);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForGrantCall");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForSmuProposal(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();
		Boolean isUnitAdmin = vo.getIsUnitAdmin();
		boolean isProvost = vo.isProvost();
		boolean isReviewer = vo.isReviewer();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForSmuProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(Proposal.class);
			searchCriteria.createAlias("proposalStatus", "proposalStatus");
			searchCriteria.createAlias("proposalCategory", "proposalCategory");
			searchCriteria.createAlias("proposalType", "proposalType");

			Criteria countCriteria = session.createCriteria(Proposal.class);
			countCriteria.createAlias("proposalStatus", "proposalStatus");
			countCriteria.createAlias("proposalCategory", "proposalCategory");
			countCriteria.createAlias("proposalType", "proposalType");
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				if (!isProvost) {
					searchCriteria.addOrder(Order.desc("updateTimeStamp"));
				}
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				Integer proposalId = Integer.valueOf(property1);
				and.add(Restrictions.like("proposalId", proposalId));
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("proposalStatus.description", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("proposalCategory.description", "%" + property4 + "%").ignoreCase());
			}
			if (isProvost) {
				List<Integer> proposalStatusCode = new ArrayList<Integer>();
				proposalStatusCode.add(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT);
				proposalStatusCode.add(Constants.PROPOSAL_STATUS_CODE_AWARDED);
				searchCriteria.add(Restrictions.disjunction().add(Restrictions.in("statusCode", proposalStatusCode)).add(Restrictions.eq("createUser", vo.getUserName())));
				searchCriteria.addOrder(Order.asc("statusCode"));
				countCriteria.add(Restrictions.disjunction().add(Restrictions.in("statusCode", proposalStatusCode)).add(Restrictions.eq("createUser", vo.getUserName())));
			}
			if (isReviewer) {
				searchCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
				countCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
				searchCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.ne("statusCode", Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS)).add(Restrictions.eq("createUser", vo.getUserName())));
				//searchCriteria.addOrder(Order.desc("statusCode"));
				countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.ne("statusCode", Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS)).add(Restrictions.eq("createUser", vo.getUserName())));
				//searchCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())));
				//countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())));
			}
			if (personId != null && !personId.isEmpty()) {
				if(!isUnitAdmin && !isProvost && !isReviewer) {
					searchCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
					searchCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())));
					countCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
					countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())));
				}
			}
			searchCriteria.add(and);
			/*ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("proposalId"), "proposalId");
			projList.add(Projections.property("title"), "title");
			projList.add(Projections.property("proposalCategory.description"), "applicationCategory");
			projList.add(Projections.property("proposalType.description"), "applicationType");
			projList.add(Projections.property("proposalStatus.description"), "applicationStatus");
			projList.add(Projections.property("submissionDate"), "submissionDate");
			searchCriteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));*/
			countCriteria.add(and);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			@SuppressWarnings("unchecked")
			List<Proposal> proposals = searchCriteria.list();
			dashBoardProfile.setProposal(proposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForSmuProposal");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

}
