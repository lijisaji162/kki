package com.polus.fibicomp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.DashboardDao;
import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.view.MobileProfile;
import com.polus.fibicomp.view.MobileProposalView;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {

	protected static Logger logger = Logger.getLogger(DashboardServiceImpl.class.getName());

	@Autowired
	private DashboardDao dashboardDao;

	@Override
	public String getDashBoardResearchSummary(String personId) throws Exception {
		return dashboardDao.getDashBoardResearchSummary(personId);
	}

	@Override
	public String getDashBoardData(CommonVO vo) throws Exception {
		logger.info("-------- getDashBoardData ---------");
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		String requestType = vo.getTabIndex();
		logger.info("requestType : " + requestType);
		try {
			if (requestType.equals("AWARD")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForAward(vo);
			}
			if (requestType.equals("PROPOSAL")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForProposal(vo);
			}
			if (requestType.equals("IRB")) {
				dashBoardProfile = dashboardDao.getProtocolDashboardData(vo);
			}
			if (requestType.equals("IACUC")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForIacuc(vo);
			}
			if (requestType.equals("DISCLOSURE")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForDisclosures(vo);
			}
			if (requestType.equals("COMMITTEE")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForCommittee(vo);
			}
			if (requestType.equals("SCHEDULE")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForCommitteeSchedule(vo);
			}
			if (requestType.equals("GRANT")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForGrantCall(vo);
			}
			if (requestType.equals("SMU_PROPOSAL")) {
				if (vo.getIsUnitAdmin()) {
					String proposalTabName = vo.getProposalTabName();
					logger.info("proposalTabName : " + proposalTabName);
					if (proposalTabName.equals("MY_PROPOSAL")) {
						dashBoardProfile = dashboardDao.getDashBoardDataForSmuMyProposal(vo);
					} else if (proposalTabName.equals("REVIEW_PENDING_PROPOSAL")) {
						dashBoardProfile = dashboardDao.getDashBoardDataForSmuReviewPendingProposal(vo);
					} else {
						dashBoardProfile = dashboardDao.getDashBoardDataForSmuProposal(vo);
					}
				} else {
					dashBoardProfile = dashboardDao.getDashBoardDataForSmuProposal(vo);
				}
			}
			// dashBoardProfile.setPersonDTO(personDTO);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardData", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@Override
	public List<ActionItem> getUserNotification(String personId) {
		return dashboardDao.getUserNotification(personId);
	}

	@Override
	public String getPieChartDataByType(CommonVO vo) throws Exception {
		logger.info("---------getPieChartDataByType---------");
		String pieChartDataByType = null;
		String personId = vo.getPersonId();
		String sponsorCode = vo.getSponsorCode();
		String pieChartIndex = vo.getPieChartIndex();
		logger.info("personId :"+ personId);
		logger.info("sponsorCode :"+ sponsorCode);
		logger.info("pieChartIndex :"+ pieChartIndex);
		try {
			if (pieChartIndex.equals("AWARD")) {
				pieChartDataByType = dashboardDao.getAwardBySponsorTypes(personId, sponsorCode);
			}
			if (pieChartIndex.equals("PROPOSAL")) {
				pieChartDataByType = dashboardDao.getProposalBySponsorTypes(personId, sponsorCode);
			}
		} catch (Exception e) {
			logger.error("Error in method getPieChartDataByType", e);
		}
		return pieChartDataByType;
	}

	@Override
	public String getDetailedSummaryData(String personId, String researchSummaryIndex) throws Exception {
		logger.info("---------getDetailedSummaryData---------");
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId);
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId);
			}
			if (researchSummaryIndex.equals("AWARDSACTIVE")) {
				dashBoardProfile = dashboardDao.getActiveAwards(personId);
			}
		} catch (Exception e) {
			logger.error("Error in method getDetailedSummaryData", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}


	@Override
	public String getDonutChartDataBySponsor(CommonVO vo) {
		logger.info("---------getDonutChartDataBySponsor---------");
		String donutChartData = null;
		String personId = vo.getPersonId();
		String sponsorCode = vo.getSponsorCode();
		String donutChartIndex = vo.getDonutChartIndex();
		logger.info("personId :"+ personId);
		logger.info("sponsorCode :"+ sponsorCode);
		logger.info("donutChartIndex :"+ donutChartIndex);
		try {
			if (donutChartIndex.equals("INPROGRESS")) {
				donutChartData = dashboardDao.getInProgressProposalsBySponsorExpanded(personId, sponsorCode);
			}
			if (donutChartIndex.equals("AWARDED")) {
				donutChartData = dashboardDao.getAwardedProposalsBySponsorExpanded(personId, sponsorCode);
			}
		} catch (Exception e) {
			logger.error("Error in method getPieChartDataByType", e);
		}
		return donutChartData;
	}

	@Override
	public String getFibiResearchSummary(String personId) throws Exception {
		logger.info("---------getFibiResearchSummary---------");
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Failed");
		List<Object[]> summaryTable = new ArrayList<Object[]>();
		List<Object[]> summaryResponse = new ArrayList<Object[]>();

		try {
			summaryTable = dashboardDao.getFibiSummaryTable(personId, summaryTable);
			Integer documentCount = 0;
			Integer iterator = 0;
			for (Object[] view : summaryTable) {
				documentCount = ((BigDecimal) view[1]).intValueExact();
				if (view[2] == null) {
					view[2] = 0;
				}
				summaryResponse.add(view);
				if (documentCount == 0 && view[2] == null) {
					iterator++;
				}
			}
			if (iterator < 3) {
				mobileProfile.setData(summaryResponse);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Data found");
				logger.info("summaryResponse : " + summaryResponse);
			}
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.getFibiResearchSummary", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getProposalsBySearchCriteria(CommonVO vo) throws Exception {
		logger.info("---------getProposalsBySearchCriteria---------");
		MobileProfile mobileProfile = new MobileProfile();
		String requestType = vo.getTabIndex();
		logger.info("requestType : " + requestType);
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("No Search Results Found");
		try {
			if (requestType.equals("AWARD")) {
			}
			if (requestType.equals("PROPOSAL")) {
				List<MobileProposalView> proposalViews = dashboardDao.getProposalsByParams(vo);
				if (proposalViews != null && !proposalViews.isEmpty()) {
					mobileProfile.setData(proposalViews);
					mobileProfile.setStatus(true);
					mobileProfile.setMessage("Data found for the given search key");
					logger.info("searched proposal datas : " + proposalViews);
				}
			}
			if (requestType.equals("IRB")) {
			}
			if (requestType.equals("IACUC")) {
			}
		} catch (Exception e) {
			logger.error("Error in methord DashboardServiceImpl.getFibiSearch", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getFibiResearchSummary(String personId, String researchSummaryIndex) throws Exception {
		logger.info("---------getFibiResearchSummary---------");
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error fetching research summary");
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Research summary details fetched successfully");
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Research summary details fetched successfully");
			}
			mobileProfile.setData(dashBoardProfile);
		} catch (Exception e) {
			logger.error("Error in method getFibiDetailedResearchSummary", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getProposalsForCertification(String personId) {
		logger.info("---------getProposalsForCertification---------");
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error fetching certification data");
		List<MobileProposalView> mobileProposalViews = dashboardDao.getProposalsForCertification(personId);
		if (mobileProposalViews != null && !mobileProposalViews.isEmpty()) {
			mobileProfile.setData(mobileProposalViews);
			mobileProfile.setStatus(true);
			mobileProfile.setMessage("Datas retrived sucessfully");
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(mobileProfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
