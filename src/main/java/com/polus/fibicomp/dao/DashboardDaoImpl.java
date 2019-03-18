package com.polus.fibicomp.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;
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
import com.polus.fibicomp.workflow.comparator.WorkflowComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.service.WorkflowService;

@Transactional
@Service(value = "dashboardDao")
@PropertySource("classpath:application.properties")
public class DashboardDaoImpl implements DashboardDao {

	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private RoleDao roleDao;

	public String getDashBoardResearchSummary(String person_id, String unitNumber, boolean isAdmin, String userName) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		// List<ExpenditureVolume> expenditureVolumeChart = new ArrayList<ExpenditureVolume>();
		List<ResearchSummaryView> summaryTable = new ArrayList<ResearchSummaryView>();
		List<ResearchSummaryPieChart> summaryAwardPiechart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryProposalPiechart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryProposalDonutChart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryAwardDonutChart = new ArrayList<ResearchSummaryPieChart>();
		try {
			logger.info("---------- getDashBoardResearchSummary -----------");
			// expenditureVolumeChart = getExpenditureVolumeChart(person_id, unitNumber, isAdmin, expenditureVolumeChart);
			// logger.info("expenditureVolumeChart : " + expenditureVolumeChart);
			summaryTable = getSummaryTable(person_id, unitNumber, isAdmin, userName, summaryTable);
			logger.info("summaryTable : " + summaryTable);
			summaryAwardPiechart = getSummaryAwardPieChart(person_id, unitNumber, isAdmin, summaryAwardPiechart);
			logger.info("summaryAwardPiechart : " + summaryAwardPiechart);
			summaryProposalPiechart = getSummaryProposalPieChart(person_id, unitNumber, isAdmin, summaryProposalPiechart);
			logger.info("summaryProposalPiechart : " + summaryProposalPiechart);
			summaryProposalDonutChart = getSummaryInProgressProposalDonutChart(person_id, unitNumber, isAdmin, summaryProposalDonutChart);
			logger.info("summaryProposalDonutChart : " + summaryProposalDonutChart);
			summaryAwardDonutChart = getSummaryAwardedProposalDonutChart(person_id, unitNumber, isAdmin, summaryAwardDonutChart);
			logger.info("summaryAwardDonutChart : " + summaryAwardDonutChart);

			// dashBoardProfile.setExpenditureVolumes(expenditureVolumeChart);
			dashBoardProfile.setSummaryViews(summaryTable);
			dashBoardProfile.setSummaryAwardPieChart(summaryAwardPiechart);
			dashBoardProfile.setSummaryProposalPieChart(summaryProposalPiechart);
			dashBoardProfile.setSummaryProposalDonutChart(summaryProposalDonutChart);
			dashBoardProfile.setSummaryAwardDonutChart(summaryAwardDonutChart);
			// dashBoardProfile.setUnitAdministrators(loginDao.isUnitAdmin(person_id));
			dashBoardProfile.setUnitAdminDetails(loginDao.isUnitAdminDetail(person_id));
		} catch (Exception e) {
			logger.error("Error in method getDashBoardResearchSummary");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureVolume> getExpenditureVolumeChart(String person_id, String unitNumber, boolean isAdmin,
			List<ExpenditureVolume> expenditureVolumeChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query expenditureVolume = null;
		if (oracledb.equals("Y")) {
			if(isAdmin) {
				if(unitNumber != null) {
					expenditureVolume = session.createSQLQuery(
							"SELECT to_char(t3.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id AND unit_number = :unitNumber) OR t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber)) GROUP BY TO_CHAR(t3.start_date,'yyyy') ORDER BY TO_CHAR(t3.start_date,'yyyy')");
					expenditureVolume.setString("unitNumber", unitNumber);
				} else {
					expenditureVolume = session.createSQLQuery(
							"SELECT to_char(t3.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) OR t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3))) GROUP BY TO_CHAR(t3.start_date,'yyyy') ORDER BY TO_CHAR(t3.start_date,'yyyy')");
				}
			} else {
				expenditureVolume = session.createSQLQuery(
					"SELECT to_char(t3.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) OR t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3))) GROUP BY TO_CHAR(t3.start_date,'yyyy') ORDER BY TO_CHAR(t3.start_date,'yyyy')");
			}
		} else {
			if(isAdmin) {
				if(unitNumber != null) {
					expenditureVolume = session.createSQLQuery(
							"SELECT date_format(t3.start_date, '%Y') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id AND unit_number = :unitNumber) OR t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber)) GROUP BY year(t3.start_date) ORDER BY year(t3.start_date)");
					expenditureVolume.setString("unitNumber", unitNumber);
				} else {
					expenditureVolume = session.createSQLQuery(
							"SELECT date_format(t3.start_date, '%Y') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY year(t3.start_date) ORDER BY year(t3.start_date)");
				}
			} else {
				expenditureVolume = session.createSQLQuery(
					"SELECT date_format(t3.start_date, '%Y') AS BUDGET_PERIOD, Sum(t3.total_direct_cost) AS Direct_Cost, Sum(t3.total_indirect_cost) AS FA FROM fibi_proposal t1 INNER JOIN fibi_budget_header t2 ON t1.budget_header_id = t2.budget_header_id INNER JOIN fibi_budget_period t3 ON t2.budget_header_id = t3.budget_header_id WHERE t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY year(t3.start_date) ORDER BY year(t3.start_date)");
			}
		}
		expenditureVolume.setString("person_id", person_id);
		expenditureVolumeChart = expenditureVolume.list();
		return expenditureVolumeChart;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryView> getSummaryTable(String person_id, String unitNumber, boolean isAdmin, String userName, List<ResearchSummaryView> summaryTable) {
		List<ResearchSummaryView> subPropCount = null;
		List<ResearchSummaryView> inPropCount = null;
		List<ResearchSummaryView> activeAwardsCount = null;
		Query submittedProposal = null;
		Query inprogressProposal = null;
		Query activeAwards = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();	
		if(isAdmin) {
			if(unitNumber != null) {
				submittedProposal = session.createSQLQuery(
						"select 'Submitted Proposals' as Submitted_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code=2 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER =:userName)");
				submittedProposal.setString("unitNumber", unitNumber);
			} else {
				submittedProposal = session.createSQLQuery(
						"select 'Submitted Proposals' as Submitted_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code=2 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
			}
		} else {
			submittedProposal = session.createSQLQuery(
					"select 'Submitted Proposals' as Submitted_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code=2 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
		}
		submittedProposal.setString("userName", userName);
		submittedProposal.setString("person_id", person_id);	
		subPropCount = submittedProposal.list();
		if (subPropCount != null && !subPropCount.isEmpty()) {
			summaryTable.addAll(subPropCount);
		}

		if(isAdmin) {
			if(unitNumber != null) {
				inprogressProposal = session.createSQLQuery(
						"select 'In Progress Proposals' as In_Progress_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code = 1 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER =:userName)");
				inprogressProposal.setString("unitNumber", unitNumber);
			} else {
				inprogressProposal = session.createSQLQuery(
						"select 'In Progress Proposals' as In_Progress_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code = 1 and ( t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
			}
		} else {
			inprogressProposal = session.createSQLQuery(
					"select 'In Progress Proposals' as In_Progress_Proposal, count(t1.proposal_id) as count, sum(t2.TOTAL_COST) as total_amount from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id where t1.status_code=1 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
		}
		inprogressProposal.setString("userName", userName);
		inprogressProposal.setString("person_id", person_id);
		inPropCount = inprogressProposal.list();
		if (inPropCount != null && !inPropCount.isEmpty()) {
			summaryTable.addAll(inPropCount);
		}

		if(isAdmin) {
			if(unitNumber != null) {
				activeAwards = session.createSQLQuery(
						"select 'Active Awards' as Active_Award, count(t1.award_id),sum(t3.TOTAL_COST) as total_amount from AWARD t1 left outer join AWARD_BUDGET_EXT t2 on t1.award_id=t2.award_id left outer join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.award_sequence_status = 'ACTIVE' and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Award' and person_id = :person_id and unit_number = :unitNumber) or T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				activeAwards.setString("unitNumber", unitNumber);
			} else {
				activeAwards = session.createSQLQuery(
						"select 'Active Awards' as Active_Award, count(t1.award_id),sum(t3.TOTAL_COST) as total_amount from AWARD t1 left outer join AWARD_BUDGET_EXT t2 on t1.award_id=t2.award_id left outer join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.award_sequence_status = 'ACTIVE' and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Award' and person_id = :person_id) or T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
			}
		} else {
			activeAwards = session.createSQLQuery(
					"select 'Active Awards' as Active_Award, count(t1.award_id),sum(t3.TOTAL_COST) as total_amount from AWARD t1 left outer join AWARD_BUDGET_EXT t2 on t1.award_id=t2.award_id left outer join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.award_sequence_status = 'ACTIVE' and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Award' and person_id = :person_id)");
		}
		activeAwards.setString("person_id", person_id);
		activeAwardsCount = activeAwards.list();
		if (activeAwardsCount != null && !activeAwardsCount.isEmpty()) {
			summaryTable.addAll(activeAwardsCount);
		}
		return summaryTable;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryAwardPieChart(String person_id, String unitNumber, boolean isAdmin,
			List<ResearchSummaryPieChart> summaryAwardPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = null;
		if(isAdmin) {
			if(unitNumber != null) {
				query = session.createSQLQuery(
						"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.award_sequence_status = 'ACTIVE' and (t1.LEAD_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id and unit_number = :unitNumber) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP'))) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
				query.setString("unitNumber", unitNumber);
			} else {
				query = session.createSQLQuery(
						"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.award_sequence_status = 'ACTIVE' and (t1.LEAD_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP'))) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
			}
		} else {
			query = session.createSQLQuery(
					"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.award_sequence_status = 'ACTIVE' and t1.LEAD_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
		}
		query.setString("person_id", person_id);
		return summaryAwardPiechart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryProposalPieChart(String person_id, String unitNumber, boolean isAdmin,
			List<ResearchSummaryPieChart> summaryProposalPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = null;
		if(isAdmin) {
			if(unitNumber != null) {
				query = session.createSQLQuery(
						"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where (t1.HOME_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber)) group by t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION");
				query.setString("unitNumber", unitNumber);
			} else {
				query = session.createSQLQuery(
						"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where (t1.HOME_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3))) group by t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION");
			}
		} else {
			query = session.createSQLQuery(
					"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.HOME_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION");
		}
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

	/*public DashBoardProfile getDashBoardDataForProposal(CommonVO vo) {
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
	}*/

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
	public String getAwardBySponsorTypes(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> awardBySponsorTypes = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query awardList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					awardList = session.createSQLQuery(
							"SELECT t1.sequence_number, t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND (t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId and unit_number = :unitNumber) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :personId AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
					awardList.setString("unitNumber", unitNumber);
				} else {
					awardList = session.createSQLQuery(
							"SELECT t1.sequence_number, t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND (t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				awardList = session.createSQLQuery(
						"SELECT t1.sequence_number, t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId)");
			}
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
	public String getProposalBySponsorTypes(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> proposalBySponsorTypes = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber))");
					proposalList.setString("unitNumber", unitNumber);
				} else {
					proposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)))");
				}
			} else {
				proposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId)");
			}
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
	public DashBoardProfile getProposalsInProgress(String personId, boolean isAdmin, String unitNumber, String userName) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query progressProposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					progressProposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=1 and (t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER =:userName)");
					progressProposalList.setString("unitNumber", unitNumber);
				} else {
					progressProposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=1 and (t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
				}
			} else {
				progressProposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=1 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
			}
			progressProposalList.setString("personId", personId);
			progressProposalList.setString("userName", userName);
			List<Object[]> proposals = progressProposalList.list();
			for (Object[] proposal : proposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setProposalNumber(proposal[0].toString());
				proposalView.setTitle(proposal[1].toString());
				proposalView.setSponsor(proposal[2].toString());
				if (proposal[3] != null) {
					proposalView.setTotalCost(proposal[3].toString());					
				} else {
					proposalView.setTotalCost("0.00");
				}
				proposalView.setFullName(proposal[4].toString());
				proposalView.setLeadUnit(proposal[6].toString());
				Object deadLineObj = proposal[7];
				if (deadLineObj != null) {
					String deadLineDate = deadLineObj.toString();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					java.util.Date utilDeadLineDate = sdf1.parse(deadLineDate);
					java.sql.Date sqlDeadLineDate = new java.sql.Date(utilDeadLineDate.getTime());
					proposalView.setDeadLinedate(sqlDeadLineDate);
				}
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
	public DashBoardProfile getSubmittedProposals(String personId, boolean isAdmin, String unitNumber, String userName) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> submittedProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query subproposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					subproposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and (t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER =:userName)");
					subproposalList.setString("unitNumber", unitNumber);
				} else {
					subproposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and ( t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
				}
			} else {
				subproposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.HOME_UNIT_NUMBER as unit_number, t6.UNIT_NAME, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
			}
			subproposalList.setString("personId", personId);
			subproposalList.setString("userName", userName);
			List<Object[]> subProposals = subproposalList.list();
			for (Object[] proposal : subProposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setProposalNumber(proposal[0].toString());
				proposalView.setTitle(proposal[1].toString());
				proposalView.setSponsor(proposal[2].toString());
				if (proposal[3] != null) {
					proposalView.setTotalCost(proposal[3].toString());
				} else {
					proposalView.setTotalCost("0.00");
				}
				proposalView.setFullName(proposal[4].toString());
				proposalView.setLeadUnit(proposal[6].toString());
				Object deadLineObj = proposal[7];
				if (deadLineObj != null) {
					String deadLineDate = deadLineObj.toString();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					java.util.Date utilDeadLineDate = sdf1.parse(deadLineDate);
					java.sql.Date sqlDeadLineDate = new java.sql.Date(utilDeadLineDate.getTime());
					proposalView.setDeadLinedate(sqlDeadLineDate);
				}
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
	public DashBoardProfile getActiveAwards(String personId, boolean isAdmin, String unitNumber) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> activeAwards = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query activeAwardList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					activeAwardList = session.createSQLQuery(
							"SELECT t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND( t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId and unit_number = :unitNumber) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :personId AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
					activeAwardList.setString("unitNumber", unitNumber);
				} else {
					activeAwardList = session.createSQLQuery(
							"SELECT t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND( t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				activeAwardList = session.createSQLQuery(
						"SELECT t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId)");
			}
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
	public List<ResearchSummaryPieChart> getSummaryAwardedProposalDonutChart(String person_id, String unitNumber, boolean isAdmin,
			List<ResearchSummaryPieChart> summaryAwardDonutChart) {	
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = null;
		if(isAdmin) {
			if(unitNumber != null) {
				query = session.createSQLQuery(
						"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from proposal t1 inner join SPONSOR t2 on t1.sponsor_code = t2.sponsor_code where t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id and unit_number = :unitNumber) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 INNER JOIN PROPOSAL_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP') AND T2.UNIT_NUMBER = :unitNumber)) group by t1.sponsor_code,t2.SPONSOR_NAME");
				query.setString("unitNumber", unitNumber);
			}else {
				query = session.createSQLQuery(
						"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from proposal t1 inner join SPONSOR t2 on t1.sponsor_code = t2.sponsor_code where t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP'))) group by t1.sponsor_code,t2.SPONSOR_NAME");
			}
		} else {
			query = session.createSQLQuery(
					"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from proposal t1 inner join SPONSOR t2 on t1.sponsor_code = t2.sponsor_code where t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id ) group by t1.sponsor_code,t2.SPONSOR_NAME");
		}
		query.setString("person_id", person_id);
		return summaryAwardDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryInProgressProposalDonutChart(String person_id, String unitNumber, boolean isAdmin,
			List<ResearchSummaryPieChart> summaryProposalDonutChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = null;
		if(isAdmin) {
			if(unitNumber != null) {
				query = session.createSQLQuery(
						"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code where t1.status_code=1 and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber)) group by t1.sponsor_code,t2.SPONSOR_NAME");
				query.setString("unitNumber", unitNumber);
			}else {
				query = session.createSQLQuery(
						"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code where t1.status_code=1 and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3))) group by t1.sponsor_code,t2.SPONSOR_NAME");
			}
		} else {
			query = session.createSQLQuery(
					"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from fibi_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code where t1.status_code=1 and t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t1.sponsor_code,t2.SPONSOR_NAME");
		}
		query.setString("person_id", person_id);
		return summaryProposalDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getInProgressProposalsBySponsorExpanded(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t4.full_name AS PI, t1.TYPE_CODE, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber))");
					proposalQuery.setString("unitNumber", unitNumber);
				} else {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t4.full_name AS PI, t1.TYPE_CODE, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)))");
				}
			} else {
				proposalQuery = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t4.full_name AS PI, t1.TYPE_CODE, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId)");
			}
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			inProgressProposal = proposalQuery.list();
			logger.info("inProgressProposal : " + inProgressProposal);
			dashBoardProfile.setProposalViews(inProgressProposal);
		} catch (Exception e) {
			logger.error("Error in method getInProgressProposalsBySponsorExpanded");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAwardedProposalsBySponsorExpanded(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> awardedProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalQuery = session.createSQLQuery(
							"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.ACTIVITY_TYPE_CODE, t5.DESCRIPTION as ACTIVITY_TYPE, T1.PROPOSAL_TYPE_CODE, T6.description as PROPOSAL_TYPE, t1.sponsor_code from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId and unit_number = :unitNumber) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 INNER JOIN PROPOSAL_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP') AND T2.UNIT_NUMBER = :unitNumber))");
					proposalQuery.setString("unitNumber", unitNumber);
				} else {
					proposalQuery = session.createSQLQuery(
							"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.ACTIVITY_TYPE_CODE, t5.DESCRIPTION as ACTIVITY_TYPE, T1.PROPOSAL_TYPE_CODE, T6.description as PROPOSAL_TYPE, t1.sponsor_code from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				proposalQuery = session.createSQLQuery(
						"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.ACTIVITY_TYPE_CODE, t5.DESCRIPTION as ACTIVITY_TYPE, T1.PROPOSAL_TYPE_CODE, T6.description as PROPOSAL_TYPE, t1.sponsor_code from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId ) ");
			}
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awardedProposal = proposalQuery.list();
			logger.info("awardedProposal : " + awardedProposal);
			dashBoardProfile.setProposalViews(awardedProposal);
		} catch (Exception e) {
			logger.error("Error in method getAwardedProposalsBySponsorExpanded");
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
	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		String property5 = vo.getProperty5();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();
		Boolean isUnitAdmin = vo.getIsUnitAdmin();
		Boolean isSuperUser = vo.getIsSuperUser();
		boolean isProvost = vo.isProvost();
		// boolean isReviewer = vo.isReviewer();
		List<String> unitNumbers = new ArrayList<>();
		if (isUnitAdmin) {
			List<RoleMemberBo> unitAdminMemberBos = roleDao.fetchUserRole(personId, Constants.UNIT_ADMINISTRATOR_ROLE);
			if (unitAdminMemberBos != null && !unitAdminMemberBos.isEmpty()) {
				for (RoleMemberBo unitAdminMemberBo : unitAdminMemberBos) {
					List<RoleMemberAttributeDataBo> attributeDataBos = unitAdminMemberBo.getAttributeDetails();
					if (attributeDataBos != null && !attributeDataBos.isEmpty()) {
						for (RoleMemberAttributeDataBo bo : attributeDataBos) {
							unitNumbers.add(bo.getAttributeValue());
						}
					}
				}
			}
		}	

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(Proposal.class);
			searchCriteria.createAlias("proposalStatus", "proposalStatus");
			//searchCriteria.createAlias("proposalCategory", "proposalCategory");
			searchCriteria.createAlias("activityType", "activityType");
			searchCriteria.createAlias("proposalType", "proposalType");
			searchCriteria.createAlias("proposalPersons", "proposalPersons");

			Criteria countCriteria = session.createCriteria(Proposal.class);
			countCriteria.createAlias("proposalStatus", "proposalStatus");
			//countCriteria.createAlias("proposalCategory", "proposalCategory");
			countCriteria.createAlias("activityType", "activityType");
			countCriteria.createAlias("proposalType", "proposalType");
			countCriteria.createAlias("proposalPersons", "proposalPersons");
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				if (!isProvost) {
					searchCriteria.addOrder(Order.desc("updateTimeStamp"))
					.addOrder(Order.desc("proposalId"));
				}
			} else {
				if (sortBy.equals("proposalPersons.fullName") && reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (sortBy.equals("proposalPersons.fullName") && reverse.equals("ASC")) {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId"));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId"));
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
				//and.add(Restrictions.like("proposalCategory.description", "%" + property4 + "%").ignoreCase());
				and.add(Restrictions.like("activityType.description", "%" + property4 + "%").ignoreCase());
			}
			if (property5 != null && !property5.isEmpty()) {
				// and.add(Restrictions.like("proposalPerson", "%" + property5 + "%").ignoreCase());
				and.add(Restrictions.conjunction().add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE)).add(Restrictions.like("proposalPersons.fullName", "%" + property5 + "%").ignoreCase()));		
			}
			
			if (personId != null && !personId.isEmpty()) {
				/*if(!isUnitAdmin && !isProvost && !isReviewer) {
					searchCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
					searchCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())).add(Restrictions.eq("homeUnitNumber", vo.getUnitNumber())));
					countCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
					countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName())).add(Restrictions.eq("homeUnitNumber", vo.getUnitNumber())));
				}*/
				if(isUnitAdmin && !isSuperUser) {
					searchCriteria.add(Restrictions.in("homeUnitNumber", unitNumbers));
					countCriteria.add(Restrictions.in("homeUnitNumber", unitNumbers));
				}
			}
			searchCriteria.add(Restrictions.eq("isInactive", false));
			countCriteria.add(Restrictions.eq("isInactive", false));
			searchCriteria.add(and);
			/*ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("proposalId"), "proposalId");
			projList.add(Projections.property("title"), "title");
			//projList.add(Projections.property("proposalCategory.description"), "applicationCategory");
			projList.add(Projections.property("activityType.description"), "applicationActivityType");
			projList.add(Projections.property("proposalType.description"), "applicationType");
			projList.add(Projections.property("proposalStatus.description"), "applicationStatus");
			projList.add(Projections.property("sponsorDeadlineDate"), "sponsorDeadlineDate");
			projList.add(Projections.property("sponsorName"), "sponsorName");
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
					propObj.setCreateUser(proposalObject.getCreateUser());
					if (proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS || proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
						Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(propObj.getProposalId());
						workflowService.prepareWorkflowDetails(workflow);
						propObj.setWorkflow(workflow);
						List<Workflow> WorkflowList = workflowDao.fetchWorkflowsByModuleItemId(propObj.getProposalId());
						if(WorkflowList != null) {
							workflowService.prepareWorkflowDetailsList(WorkflowList);
							Collections.sort(WorkflowList, new WorkflowComparator());
							propObj.setWorkflowList(WorkflowList);
						}
					}
					proposalList.add(propObj);
				}
			}
			dashBoardProfile.setProposal(proposalList);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForProposal");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForMyProposal(CommonVO vo) {
		logger.info("----------- getDashBoardDataForMyProposal ------------");
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		String property5 = vo.getProperty5();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();
		logger.info("pageNumber : " + pageNumber);
		logger.info("sortBy : " + sortBy);
		logger.info("reverse : " + reverse);
		logger.info("property1 : " + property1);
		logger.info("property2 : " + property2);
		logger.info("property3 : " + property3);
		logger.info("property4 : " + property4);
		logger.info("property5 : " + property5);
		logger.info("currentPage : " + currentPage);
		logger.info("personId : " + personId);

		Conjunction and = Restrictions.conjunction();
		try {
			// logger.info("----------- getDashBoardDataForMyProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(Proposal.class);
			searchCriteria.createAlias("proposalStatus", "proposalStatus");
			searchCriteria.createAlias("activityType", "activityType");
			searchCriteria.createAlias("proposalType", "proposalType");
			// searchCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
			searchCriteria.createAlias("proposalPersons", "proposalPersons");

			Criteria countCriteria = session.createCriteria(Proposal.class);
			countCriteria.createAlias("proposalStatus", "proposalStatus");
			countCriteria.createAlias("activityType", "activityType");
			countCriteria.createAlias("proposalType", "proposalType");
			// countCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
			countCriteria.createAlias("proposalPersons", "proposalPersons");
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"))
				.addOrder(Order.desc("proposalId"));
			} else {
				if (sortBy.equals("proposalPersons.fullName") && reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (sortBy.equals("proposalPersons.fullName") && reverse.equals("ASC")) {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId"));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId"));
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
				and.add(Restrictions.like("activityType.description", "%" + property4 + "%").ignoreCase());
			}
			if (property5 != null && !property5.isEmpty()) {
				// and.add(Restrictions.like("proposalPerson", "%" + property5 + "%").ignoreCase());
				and.add(Restrictions.conjunction().add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE)).add(Restrictions.like("proposalPersons.fullName", "%" + property5 + "%").ignoreCase()));		
			}
			if (personId != null && !personId.isEmpty()) {
				// searchCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
				searchCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName()))); // .add(Restrictions.eq("homeUnitNumber", vo.getUnitNumber()))
				// countCriteria.createAlias("proposalPersons", "proposalPersons", JoinType.LEFT_OUTER_JOIN);
				countCriteria.add(Restrictions.disjunction().add(Restrictions.eq("proposalPersons.personId", personId)).add(Restrictions.eq("createUser", vo.getUserName()))); // .add(Restrictions.eq("homeUnitNumber", vo.getUnitNumber()))
			}
			searchCriteria.add(Restrictions.eq("isInactive", false));
			countCriteria.add(Restrictions.eq("isInactive", false));
			searchCriteria.add(and);
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
					if (proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS || proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
						Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(propObj.getProposalId());	
						workflowService.prepareWorkflowDetails(workflow);
						propObj.setWorkflow(workflow);
						List<Workflow> WorkflowList = workflowDao.fetchWorkflowsByModuleItemId(propObj.getProposalId());
						if(WorkflowList != null) {
							workflowService.prepareWorkflowDetailsList(WorkflowList);
							Collections.sort(WorkflowList, new WorkflowComparator());
							propObj.setWorkflowList(WorkflowList);
						}
					}
					proposalList.add(propObj);
				}
			}
			dashBoardProfile.setProposal(proposalList);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForMyProposal");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForReviewPendingProposal(CommonVO vo, List<Integer> proposalIds) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		String property5 = vo.getProperty5();
		Integer currentPage = vo.getCurrentPage();

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("----------- getDashBoardDataForReviewPendingProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(Proposal.class);
			searchCriteria.createAlias("proposalStatus", "proposalStatus");
			searchCriteria.createAlias("activityType", "activityType");
			searchCriteria.createAlias("proposalType", "proposalType");
			searchCriteria.createAlias("proposalPersons", "proposalPersons");

			Criteria countCriteria = session.createCriteria(Proposal.class);
			countCriteria.createAlias("proposalStatus", "proposalStatus");
			countCriteria.createAlias("activityType", "activityType");
			countCriteria.createAlias("proposalType", "proposalType");
			countCriteria.createAlias("proposalPersons", "proposalPersons");
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"))
				.addOrder(Order.desc("proposalId"));
			} else {
				if (sortBy.equals("proposalPersons.fullName") && reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (sortBy.equals("proposalPersons.fullName") && reverse.equals("ASC")) {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId")).add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE));
				} else if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy))
					.addOrder(Order.desc("proposalId"));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy))
					.addOrder(Order.desc("proposalId"));
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
				and.add(Restrictions.like("activityType.description", "%" + property4 + "%").ignoreCase());
			}
			if (property5 != null && !property5.isEmpty()) {
				and.add(Restrictions.conjunction().add(Restrictions.eq("proposalPersons.personRoleId", Constants.PI_ROLE_CODE)).add(Restrictions.like("proposalPersons.fullName", "%" + property5 + "%").ignoreCase()));
			}
			List<Integer> proposalStatusCodes = new ArrayList<>();
			proposalStatusCodes.add(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
			proposalStatusCodes.add(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
			proposalStatusCodes.add(Constants.PROPOSAL_STATUS_CODE_RETURNED);
			searchCriteria.add(Restrictions.disjunction().add(Restrictions.in("statusCode", proposalStatusCodes)));
			searchCriteria.add(Restrictions.in("proposalId", proposalIds));
			countCriteria.add(Restrictions.disjunction().add(Restrictions.in("statusCode", proposalStatusCodes)));
			countCriteria.add(Restrictions.in("proposalId", proposalIds));
			searchCriteria.add(Restrictions.eq("isInactive", false));
			countCriteria.add(Restrictions.eq("isInactive", false));
			searchCriteria.add(and);
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
					if (proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS || proposalObject.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
						Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(propObj.getProposalId());
						workflowService.prepareWorkflowDetails(workflow);
						propObj.setWorkflow(workflow);
						List<Workflow> WorkflowList = workflowDao.fetchWorkflowsByModuleItemId(propObj.getProposalId());
						if(WorkflowList != null) {
							workflowService.prepareWorkflowDetailsList(WorkflowList);
							Collections.sort(WorkflowList, new WorkflowComparator());
							propObj.setWorkflowList(WorkflowList);
						}
					}
					proposalList.add(propObj);
				}
			}
			dashBoardProfile.setProposal(proposalList);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForReviewPendingProposal");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getApprovalInprogressProposalIds(String personId, String approvalStatusCode, Integer moduleCode) {
		List<Integer> proposalIds = new ArrayList<Integer>();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"SELECT PROPOSAL_ID FROM eps_prop_pre_review where PRE_REVIEW_STATUS_CODE=:pre_review_status_code and REVIEWER_PERSON_ID=:reviewer_person_id union select t1.MODULE_ITEM_ID as PROPOSAL_ID from fibi_workflow t1 inner join fibi_workflow_detail t2 on t1.WORKFLOW_ID = t2.workflow_id where t1.MODULE_CODE = :module_code and t1.IS_WORKFLOW_ACTIVE='Y' and t2.approver_person_id = :person_id and t2.APPROVAL_STATUS_CODE = :approval_status_code");
		query.setString("pre_review_status_code", Constants.PRE_REVIEW_STATUS_INPROGRESS);
		query.setString("reviewer_person_id", personId);
		query.setString("person_id", personId);
		query.setString("approval_status_code", approvalStatusCode);
		query.setInteger("module_code", moduleCode);
		proposalIds = query.list();
		return proposalIds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getInprogressProposalsForDownload(String personId,List<Object[]> inprogressProposals, String unitNumber, boolean isAdmin, String userName) throws Exception {
		try {
			logger.info("----------- getInprogressProposalsForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query progressProposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					progressProposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=1 and ( t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER = :userName)");
					progressProposalList.setString("unitNumber", unitNumber);
				} else {
					progressProposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=1 and ( t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER = :userName)");
				}
			} else {
				progressProposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBERwhere t1.status_code=1 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 	WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3))	or t1.CREATE_USER =:userName)");
			}
			progressProposalList.setString("personId", personId);
			progressProposalList.setString("userName", userName);
			inprogressProposals = progressProposalList.list();
			logger.info("Inprogress Proposals : " + inprogressProposals);
		} catch (Exception e) {
			logger.error("Error in method getInprogressProposalsForDownload");
			e.printStackTrace();
		}
		return inprogressProposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSubmittedProposalsForDownload(String personId,List<Object[]> submittedProposals, String unitNumber, boolean isAdmin, String userName) throws Exception {
		try {
			logger.info("----------- getSubmittedProposalsForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query subproposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					subproposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI,t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and ( t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)  AND T2.UNIT_NUMBER = :unitNumber) or t1.CREATE_USER = :userName)");
					subproposalList.setString("unitNumber", unitNumber);
				} else {
					subproposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and (t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) ) or t1.CREATE_USER = :userName)");
				}
			} else {
				subproposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t5.sponsor_name, t2.TOTAL_COST, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 left outer join fibi_budget_header t2 on t1.budget_header_id=t2.budget_header_id LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.HOME_UNIT_NUMBER= t6.UNIT_NUMBER where t1.status_code=2 and (t1.proposal_id IN (SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :person_id AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)) or t1.CREATE_USER =:userName)");
			}
			subproposalList.setString("personId", personId);
			subproposalList.setString("userName", userName);
			submittedProposals = subproposalList.list();
			logger.info("Submitted Proposals : " + submittedProposals);
		} catch (Exception e) {
			logger.error("Error in method getSubmittedProposalsForDownload");
			e.printStackTrace();
		}
		return submittedProposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getActiveAwardsForDownload(String personId,List<Object[]> activeAwards, String unitNumber, boolean isAdmin) throws Exception {
		try {
			logger.info("----------- getActiveAwardsForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query activeAwardList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					activeAwardList = session.createSQLQuery(
							"SELECT t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND( t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId and unit_number = :unitNumber) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :personId AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
					activeAwardList.setString("unitNumber", unitNumber);
				} else {
					activeAwardList = session.createSQLQuery(
							"SELECT t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND( t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				activeAwardList = session.createSQLQuery(
						"SELECT t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 LEFT OUTER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id LEFT OUTER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId)");
			}
			activeAwardList.setString("personId", personId);
			activeAwards = activeAwardList.list();
			logger.info("Active Awards : " + activeAwards);
		} catch (Exception e) {
			logger.error("Error in method getActiveAwardsForDownload");
			e.printStackTrace();
		}
		return activeAwards;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getInProgressProposalsBySponsorForDownload(String personId, String sponsorCode, List<Object[]> inProgressProposalsBySponsor, String unitNumber, boolean isAdmin) throws Exception {
		try {
			logger.info("----------- getInProgressProposalsBySponsorForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber))");
					proposalQuery.setString("unitNumber", unitNumber);
				} else {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and (t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)))");
				}
			} else {
				proposalQuery = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t5.DESCRIPTION as Proposal_Type, t6.TOTAL_COST as BUDGET, t4.full_name AS PI, t1.SPONSOR_DEADLINE_DATE from fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t4 ON t1.proposal_id = t4.proposal_id AND t4.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t5 ON t1.TYPE_CODE=t5.TYPE_CODE LEFT OUTER JOIN fibi_budget_header t6 ON t1.budget_header_id = t6.budget_header_id where t1.status_code=1 and t1.sponsor_code = :sponsorCode and t1.HOME_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId)");
			}
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			inProgressProposalsBySponsor = proposalQuery.list();
			logger.info("inProgressProposalsBySponsor : " + inProgressProposalsBySponsor);	
		} catch (Exception e) {
			logger.error("Error in method getInProgressProposalsBySponsorForDownload");
			e.printStackTrace();
		}
		return inProgressProposalsBySponsor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAwardedProposalsBySponsorForDownload(String personId, String sponsorCode, List<Object[]> awardedProposalsBySponsor, String unitNumber, boolean isAdmin) throws Exception {
		try {
			logger.info("----------- getAwardedProposalsBySponsorForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_number, t1.title, T6.description as PROPOSAL_TYPE, t5.DESCRIPTION as ACTIVITY_TYPE, t4.full_name AS PI from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId and unit_number = :unitNumber) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 INNER JOIN PROPOSAL_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP') AND T2.UNIT_NUMBER = :unitNumber))");
					proposalQuery.setString("unitNumber", unitNumber);
				} else {
					proposalQuery = session.createSQLQuery(
							"select t1.proposal_number, t1.title, T6.description as PROPOSAL_TYPE, t5.DESCRIPTION as ACTIVITY_TYPE, t4.full_name AS PI from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and (t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId) or t1.proposal_number IN( SELECT T1.PROPOSAL_NUMBER FROM PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				proposalQuery = session.createSQLQuery(
						"select t1.proposal_number, t1.title, T6.description as PROPOSAL_TYPE, t5.DESCRIPTION as ACTIVITY_TYPE, t4.full_name AS PI from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId ) ");
			}
			proposalQuery.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awardedProposalsBySponsor = proposalQuery.list();
			logger.info("awardedProposalsBySponsor : " + awardedProposalsBySponsor);
		} catch (Exception e) {
			logger.error("Error in method getAwardedProposalsBySponsorForDownload");
			e.printStackTrace();
		}
		return awardedProposalsBySponsor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAwardBySponsorTypesForDownload(String personId, String sponsorCode, List<Object[]> awardBySponsorTypes, String unitNumber, boolean isAdmin) throws Exception {
		try {
			logger.info("----------- getAwardBySponsorTypesForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query awardList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					awardList = session.createSQLQuery(
							"SELECT t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND (t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId and unit_number = :unitNumber) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 INNER JOIN AWARD_PERSON_UNITS T2 ON T1.AWARD_PERSON_ID = T2.AWARD_PERSON_ID WHERE T1.PERSON_ID = :personId AND T2.UNIT_NUMBER = :unitNumber AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
					awardList.setString("unitNumber", unitNumber);
				} else {
					awardList = session.createSQLQuery(
							"SELECT t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND (t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId) OR T1.AWARD_ID IN ( SELECT T1.AWARD_ID FROM AWARD_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.CONTACT_ROLE_CODE IN ('COI','PI','KP')))");
				}
			} else {
				awardList = session.createSQLQuery(
						"SELECT t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId)");
			}
			awardList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awardBySponsorTypes = awardList.list();
			logger.info("awardsBySponsorTypes : " + awardBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getAwardBySponsorTypesForDownload");
			e.printStackTrace();
		}
		return awardBySponsorTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getProposalBySponsorTypesForDownload(String personId, String sponsorCode, List<Object[]> proposalBySponsorTypes, String unitNumber, boolean isAdmin) throws Exception {	
		try {
			logger.info("----------- getProposalBySponsorTypesForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = null;
			if(isAdmin) {
				if(unitNumber != null) {
					proposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId and unit_number = :unitNumber) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 INNER JOIN FIBI_PROP_PERSON_UNITS T2 ON T1.PROPOSAL_PERSON_ID = T2.PROPOSAL_PERSON_ID WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3) AND T2.UNIT_NUMBER = :unitNumber))");
					proposalList.setString("unitNumber", unitNumber);
				} else {
					proposalList = session.createSQLQuery(
							"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND (t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId) or t1.proposal_id IN ( SELECT T1.proposal_id FROM FIBI_PROPOSAL_PERSONS T1 WHERE T1.PERSON_ID = :personId AND T1.PROP_PERSON_ROLE_ID IN (1,2,3)))");
				}
			} else {
				proposalList = session.createSQLQuery(
						"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId)");
			}
			proposalList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			proposalBySponsorTypes = proposalList.list();
			logger.info("proposalsBySponsorTypes : " + proposalBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getProposalBySponsorTypesForDownload");
			e.printStackTrace();
		}
		return proposalBySponsorTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDashBoardDataOfProposalForDownload(List<Object[]> proposals, CommonVO vo) throws Exception {
		try {
			logger.info("----------- getDashBoardDataOfProposalForDownload ------------");
			Query proposalList = null;
			logger.info("PI_Name-property5 : " + vo.getProperty5());
			logger.info("IsSuperUser : " + vo.getIsSuperUser());
			String superUserQuery = "SELECT t1.PROPOSAL_ID AS PROPOSAL_ID,t1.TITLE AS TITLE,t2.FULL_NAME AS FULL_NAME,t5.DESCRIPTION AS CATEGORY,"
					+ "t3.DESCRIPTION AS type,t4.DESCRIPTION AS status,t1.SPONSOR_NAME AS sponsor, t1.SPONSOR_DEADLINE_DATE "
					+ "FROM fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t2 ON t1.PROPOSAL_ID = t2.PROPOSAL_ID AND t2.PROP_PERSON_ROLE_ID = 3 "
					+ "INNER JOIN fibi_proposal_type t3 ON t1.TYPE_CODE = t3.TYPE_CODE INNER JOIN fibi_proposal_status t4 ON t1.STATUS_CODE = t4.STATUS_CODE "
					+ "INNER JOIN activity_type t5 ON t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE where T1.IS_INACTIVE = 'N'";
			String unitHeadQuery = "SELECT t1.PROPOSAL_ID AS PROPOSAL_ID, t1.TITLE AS TITLE, t2.FULL_NAME AS FULL_NAME, t5.DESCRIPTION AS CATEGORY, "
					+ "t3.DESCRIPTION AS type, t4.DESCRIPTION AS status, t1.SPONSOR_NAME AS sponsor, t1.SPONSOR_DEADLINE_DATE "
					+ "FROM fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t2 "
					+ "ON t1.PROPOSAL_ID = t2.PROPOSAL_ID AND t2.PROP_PERSON_ROLE_ID = 3 INNER JOIN fibi_proposal_type t3 ON t1.TYPE_CODE = t3.TYPE_CODE "
					+ "INNER JOIN fibi_proposal_status t4 ON t1.STATUS_CODE = t4.STATUS_CODE INNER JOIN activity_type t5 ON t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE WHERE T1.IS_INACTIVE = 'N' AND T1.HOME_UNIT_NUMBER "
					+ "IN ( SELECT DISTINCT T7.ATTR_VAL FROM KRIM_ROLE_MBR_T T6 "
					+ "INNER JOIN KRIM_ROLE_MBR_ATTR_DATA_T T7 ON T6.ROLE_MBR_ID = T7.ROLE_MBR_ID WHERE T6.ROLE_ID = '1954' AND T6.MBR_ID = :personId)";		
			String likeQuery = "";
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.PROPOSAL_ID) like lower(:proposalId) ";
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.TITLE) like lower(:title) ";
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				likeQuery = likeQuery + " and lower(t4.DESCRIPTION) like lower(:status) ";
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				likeQuery = likeQuery + " and lower(t5.DESCRIPTION) like lower(:category) ";
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				likeQuery = likeQuery + " and lower(t2.FULL_NAME) like lower(:fullName) ";
			}
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			if (vo.getIsSuperUser()) {
					proposalList = session.createSQLQuery(superUserQuery + likeQuery);
			} else {
				proposalList = session.createSQLQuery(unitHeadQuery + likeQuery);	
				proposalList.setString("personId", vo.getPersonId());
			}
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				proposalList.setString("proposalId", "%" + vo.getProperty1() + "%");
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				proposalList.setString("title", "%" + vo.getProperty2() + "%");
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				proposalList.setString("status", "%" + vo.getProperty3() + "%");
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				proposalList.setString("category", "%" + vo.getProperty4() + "%");
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				proposalList.setString("fullName", "%" + vo.getProperty5() + "%");
			}
			proposals = proposalList.list();
			logger.info("allProposals : " + proposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataOfProposalForDownload");
			e.printStackTrace();
		}
		return proposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDashBoardDataOfMyProposalForDownload(CommonVO vo,List<Object[]> myProposals) throws Exception {
		try {
			logger.info("----------- getDashBoardDataOfMyProposalForDownload ------------");
			Query proposalList = null;
			String mainQuery = "SELECT t1.PROPOSAL_ID AS PROPOSAL_ID,t1.TITLE AS TITLE,t2.FULL_NAME AS FULL_NAME,"
					+ "t5.DESCRIPTION AS CATEGORY,t3.DESCRIPTION AS type,t4.DESCRIPTION AS status,t1.SPONSOR_NAME AS sponsor,"
					+ "t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t2 "
					+ "ON t1.PROPOSAL_ID = t2.PROPOSAL_ID AND t2.PROP_PERSON_ROLE_ID = 3 INNER JOIN fibi_proposal_type t3 "
					+ "ON t1.TYPE_CODE = t3.TYPE_CODE INNER JOIN fibi_proposal_status t4 ON t1.STATUS_CODE = t4.STATUS_CODE "
					+ "INNER JOIN activity_type t5 ON t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE "
					+ "where T1.IS_INACTIVE = 'N' AND (T2.Person_Id = :personId or T1.Create_User = :createUser)";
			String likeQuery = "";
			String homeUnitNumber = vo.getUnitNumber();
			String createUser = vo.getUserName();
			String personId = vo.getPersonId();
			logger.info("homeUnitNumber : " + homeUnitNumber);
			logger.info("createUser : " + createUser);
			logger.info("personId : " + personId);
			logger.info("PI_Name-property5 : " + vo.getProperty5());
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.PROPOSAL_ID) like lower(:proposalId) ";
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.TITLE) like lower(:title) ";
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				likeQuery = likeQuery + " and lower(t4.DESCRIPTION) like lower(:status) ";
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				likeQuery = likeQuery + " and lower(t5.DESCRIPTION) like lower(:category) ";
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				likeQuery = likeQuery + " and lower(t2.FULL_NAME) like lower(:fullName) ";
			}
			/*Query proposalList = session.createSQLQuery(
					"SELECT t1.PROPOSAL_ID AS PROPOSAL_ID,t1.TITLE AS TITLE,t2.FULL_NAME AS FULL_NAME,t5.DESCRIPTION AS CATEGORY,t3.DESCRIPTION AS type,t4.DESCRIPTION AS status,t1.SPONSOR_NAME AS sponsor,t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t2 ON t1.PROPOSAL_ID = t2.PROPOSAL_ID AND t2.PROP_PERSON_ROLE_ID = 3 INNER JOIN fibi_proposal_type t3 ON t1.TYPE_CODE = t3.TYPE_CODE INNER JOIN fibi_proposal_status t4 ON t1.STATUS_CODE = t4.STATUS_CODE INNER JOIN activity_type t5 ON t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE where (T2.Person_Id = :personId or T1.Create_User = :createUser or t1.home_unit_number = :homeUnitNumber)");
			proposalList.setString("personId", personId).setString("createUser", createUser).setString("homeUnitNumber", homeUnitNumber);
			*/
			proposalList = session.createSQLQuery(mainQuery + likeQuery);
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				proposalList.setString("proposalId", "%" + vo.getProperty1() + "%");
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				proposalList.setString("title", "%" + vo.getProperty2() + "%");
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				proposalList.setString("status", "%" + vo.getProperty3() + "%");
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				proposalList.setString("category", "%" + vo.getProperty4() + "%");
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				proposalList.setString("fullName", "%" + vo.getProperty5() + "%");
			}
			proposalList.setString("personId", personId).setString("createUser", createUser);
			myProposals = proposalList.list();
			logger.info("myProposals : " + myProposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataOfMyProposalForDownload");
			e.printStackTrace();
		}
		return myProposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDashBoardDataOfReviewPendingProposalForDownload(CommonVO vo,List<Object[]> pendingReviewProposals) throws Exception {
		try {
			logger.info("----------- getDashBoardDataOfReviewPendingProposalForDownload ------------");
			Query proposalList = null;
			String mainQuery = "SELECT T1.PROPOSAL_ID AS PROPOSAL_ID,T1.TITLE AS TITLE,T2.FULL_NAME AS FULL_NAME,T5.DESCRIPTION AS CATEGORY,T3.DESCRIPTION AS TYPE,"
					+ "T4.DESCRIPTION AS STATUS,T1.SPONSOR_NAME AS SPONSOR,T1.SPONSOR_DEADLINE_DATE "
					+ "FROM FIBI_PROPOSAL T1 LEFT OUTER JOIN FIBI_PROPOSAL_PERSONS T2 "
					+ "ON T1.PROPOSAL_ID = T2.PROPOSAL_ID AND T2.PROP_PERSON_ROLE_ID = 3 INNER JOIN FIBI_PROPOSAL_TYPE T3 "
					+ "ON T1.TYPE_CODE = T3.TYPE_CODE INNER JOIN FIBI_PROPOSAL_STATUS T4 "
					+ "ON T1.STATUS_CODE = T4.STATUS_CODE INNER JOIN ACTIVITY_TYPE T5 "
					+ "ON T1.ACTIVITY_TYPE_CODE = T5.ACTIVITY_TYPE_CODE "
					+ "WHERE T1.STATUS_CODE IN ('1','2','3') AND T1.IS_INACTIVE = 'N' AND T1.PROPOSAL_ID "
					+ "IN (	SELECT PROPOSAL_ID FROM EPS_PROP_PRE_REVIEW WHERE PRE_REVIEW_STATUS_CODE=1 AND REVIEWER_PERSON_ID = :personId "
					+ "UNION SELECT T6.MODULE_ITEM_ID AS PROPOSAL_ID FROM FIBI_WORKFLOW T6 "
					+ "INNER JOIN FIBI_WORKFLOW_DETAIL T7 ON T6.WORKFLOW_ID = T7.WORKFLOW_ID "
					+ "WHERE T6.MODULE_CODE = 1 AND T6.IS_WORKFLOW_ACTIVE='Y' AND T7.APPROVER_PERSON_ID = :personId AND T7.APPROVAL_STATUS_CODE = 'W')";
			String personId = vo.getPersonId();
			logger.info("personId : " + personId);
			logger.info("PI_Name-property5 : " + vo.getProperty5());
			String likeQuery = "";
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			/*Query proposalList = session.createSQLQuery(
					"SELECT t1.PROPOSAL_ID AS PROPOSAL_ID,t1.TITLE AS TITLE,t2.FULL_NAME AS FULL_NAME,t5.DESCRIPTION AS CATEGORY,t3.DESCRIPTION AS type,t4.DESCRIPTION AS status,t1.SPONSOR_NAME AS sponsor,t1.SPONSOR_DEADLINE_DATE FROM fibi_proposal t1 LEFT OUTER JOIN fibi_proposal_persons t2 ON t1.PROPOSAL_ID = t2.PROPOSAL_ID  AND t2.PROP_PERSON_ROLE_ID = 3 INNER JOIN fibi_proposal_type t3 ON t1.TYPE_CODE = t3.TYPE_CODE INNER JOIN fibi_proposal_status t4 ON t1.STATUS_CODE = t4.STATUS_CODE INNER JOIN activity_type t5 ON t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN fibi_workflow T6 ON t1.proposal_id = t6.module_item_id and t6.module_code = 1 and t6.is_workflow_active = 'Y' INNER JOIN fibi_workflow_detail t7 ON t6.workflow_id = t7.workflow_id and t7.approval_status_code = 'W' where t7.approver_person_id = :personId");
			 */
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.PROPOSAL_ID) like lower(:proposalId) ";
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				likeQuery = likeQuery + " and lower(t1.TITLE) like lower(:title) ";
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				likeQuery = likeQuery + " and lower(t4.DESCRIPTION) like lower(:status) ";
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				likeQuery = likeQuery + " and lower(t5.DESCRIPTION) like lower(:category) ";
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				likeQuery = likeQuery + " and lower(t2.FULL_NAME) like lower(:fullName) ";
			}
			proposalList = session.createSQLQuery(mainQuery + likeQuery);
			if (vo.getProperty1() != null && !vo.getProperty1().isEmpty()) {
				proposalList.setString("proposalId", "%" + vo.getProperty1() + "%");
			}
			if (vo.getProperty2() != null && !vo.getProperty2().isEmpty()) {
				proposalList.setString("title", "%" + vo.getProperty2() + "%");
			}
			if (vo.getProperty3() != null && !vo.getProperty3().isEmpty()) {
				proposalList.setString("status", "%" + vo.getProperty3() + "%");
			}
			if (vo.getProperty4() != null && !vo.getProperty4().isEmpty()) {
				proposalList.setString("category", "%" + vo.getProperty4() + "%");
			}
			if (vo.getProperty5() != null && !vo.getProperty5().isEmpty()) {
				proposalList.setString("fullName", "%" + vo.getProperty5() + "%");
			}
			proposalList.setString("personId", personId);
			pendingReviewProposals = proposalList.list();
			logger.info("pendingReviewProposals : " + pendingReviewProposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataOfReviewPendingProposalForDownload");
			e.printStackTrace();
		}
		return pendingReviewProposals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDashBoardDataForAwardForDownload(String personId, String sponsorCode,List<Object[]> awards) throws Exception {
		try {
			logger.info("----------- getDashBoardDataForAwardForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
					"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SUBMISSION_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId)");
			proposalList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			awards = proposalList.list();
			logger.info("dashBoardDataForAward : " + awards);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForAwardForDownload");
			e.printStackTrace();
		}
		return awards;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getProtocolDashboardDataForDownload(String personId, String sponsorCode,List<Object[]> protocols) throws Exception {
		try {
			logger.info("----------- getProtocolDashboardDataForDownload ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
					"select t1.proposal_id, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI, t1.SUBMISSION_DATE FROM fibi_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN fibi_proposal_persons t3 ON t1.proposal_id = t3.proposal_id AND t3.prop_person_role_id = 3 INNER JOIN fibi_proposal_type t4 ON t1.TYPE_CODE=t4.TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.HOME_UNIT_NUMBER IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :personId)");
			proposalList.setString("personId", personId).setString("sponsorCode", sponsorCode);
			protocols = proposalList.list();
			logger.info("dashBoardDataForProtocols : " + protocols);
		} catch (Exception e) {
			logger.error("Error in method getProtocolDashboardDataForDownload");
			e.printStackTrace();
		}
		return protocols;
	}

	@Override
	public PrincipalBo getCurrentPassword(String personId) throws Exception {
		PrincipalBo principalBo = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(PrincipalBo.class);
		criteria.add(Restrictions.eq("principalId", personId));
		principalBo = (PrincipalBo) criteria.uniqueResult();
		return principalBo;
	}

	@Override
	public Integer changePassword(String encryptedPWD, String personId) {
		logger.info("----------- changePassword ------------");
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query updateQuery = session.createSQLQuery(
				"update krim_prncpl_t set prncpl_pswd = :encryptedPWD where prncpl_id = :personId");
		updateQuery.setParameter("encryptedPWD", encryptedPWD).setString("personId", personId);		
		return updateQuery.executeUpdate();
	}

}
