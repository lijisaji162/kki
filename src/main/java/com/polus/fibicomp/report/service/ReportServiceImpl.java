package com.polus.fibicomp.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.dao.DashboardDao;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.report.dao.ReportDao;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryPieChart;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "reportService")
public class ReportServiceImpl implements ReportService {

	protected static Logger logger = Logger.getLogger(ReportServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "reportDao")
	private ReportDao reportDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private GrantCallDao grantCallDao;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private DashboardDao dashboardDao;


	@Override
	public String applicationReport(ReportVO reportVO) {
		String reportName = reportVO.getReportName();
		logger.info("reportName : " + reportName);
		/*if (reportName.equals("Submitted Proposals by Grant Call")) {
			reportVO = reportDao.fetchApplicationByGrantCallId(reportVO);
		} else if (reportName.equals("Awards by Grant Call")) {
			reportVO = reportDao.fetchAwardByGrantCallId(reportVO);
		} else if (reportName.equals("Expenditure by Award")) {
			reportVO = reportDao.fetchExpenditureByAward(reportVO);
		}*/
		if (reportName.equals("Proposals by PI")) {
			reportVO = reportDao.fetchProposalsByPI(reportVO);
		}
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	public void fetchOpenGrantIds(ReportVO reportVO) {
		List<GrantCall> grantIds = reportDao.fetchOpenGrantIds();
		reportVO.setGrantIds(grantIds);
	}

	public void fetchApplicationsCountByGrantCallType(ReportVO reportVO) {
		List<GrantCallType> grantCallTypes = grantCallDao.fetchAllGrantCallTypes();
		if (grantCallTypes != null && !grantCallTypes.isEmpty()) {
			Map<String, List<Proposal>> applicationsByGrantCallType = new HashMap<String, List<Proposal>>();
			for (GrantCallType grantCallType : grantCallTypes) {
				String grantCallTypeDesc = grantCallType.getDescription();
				List<Proposal> proposals = reportDao.fetchApplicationsByGrantCallType(grantCallType.getGrantTypeCode());
				logger.info("Grant Call Type : " + grantCallTypeDesc + " ---------- Applications : " + proposals);
				applicationsByGrantCallType.put(grantCallTypeDesc, proposals);
			}
			reportVO.setApplicationsByGrantCallType(applicationsByGrantCallType);
		}
	}

	public void fetchProtocolsCountByProtocolType(ReportVO reportVO) {
		List<ProtocolType> protocolTypes = reportDao.fetchAllProtocolTypes();
		if (protocolTypes != null && !protocolTypes.isEmpty()) {
			Map<String, List<ProtocolView>> protocolsByType = new HashMap<String, List<ProtocolView>>();
			for (ProtocolType protocolType : protocolTypes) {
				String protocolTypeDesc = protocolType.getDescription();
				List<ProtocolView> protocols = reportDao.fetchProtocolsByProtocolType(protocolType.getProtocolTypeCode());
				logger.info("Protocol Type : " + protocolTypeDesc + " ---------- Protocols : " + protocols);
				protocolsByType.put(protocolTypeDesc, protocols);
			}
			reportVO.setProtocolsByType(protocolsByType);
		}
	}

	@Override
	public String fetchReportData(ReportVO reportVO) {
		/*fetchOpenGrantIds(reportVO);
		fetchApplicationsCountByGrantCallType(reportVO);
		fetchProtocolsCountByProtocolType(reportVO);
		fetchAwardByGrantCallType(reportVO);
		fetchAwardNumbers(reportVO);*/
		getPieChartDataBySponsorType(reportVO);
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	public void fetchAwardByGrantCallType(ReportVO reportVO) {
		List<GrantCallType> grantCallTypes = reportDao.fetchAllGrantCallTypes();
		if (grantCallTypes != null && !grantCallTypes.isEmpty()) {
			Map<String, List<AwardView>> awardByGrantType = new HashMap<String, List<AwardView>>();
			for (GrantCallType grantCallType : grantCallTypes) {
				String grantCallTypeDesc = grantCallType.getDescription();
				List<Integer> proposalIds = reportDao.fetchProposalIdByGrantTypeCode(grantCallType.getGrantTypeCode());
				if (proposalIds != null && !proposalIds.isEmpty()) {
					List<Integer> awardIds = reportDao.fetchAwardCountByGrantType(proposalIds);
					logger.info("awardIds : " + awardIds);
					if (awardIds != null && !awardIds.isEmpty()) {
						List<AwardView> awardList = reportDao.fetchAwardByAwardNumbers(awardIds);
						logger.info("GrantCallType : " + grantCallTypeDesc + " ---------- awardList : " + awardList);
						awardByGrantType.put(grantCallTypeDesc, awardList);
					}
				}
			}
			reportVO.setAwardByGrantType(awardByGrantType);
		}
	}

	public void fetchAwardNumbers(ReportVO reportVO) {
		List<AwardView> awardNumbers = reportDao.fetchAwardNumbers();
		reportVO.setAwardNumbers(awardNumbers);
	}

	@Override
	public XSSFWorkbook getXSSFWorkbookForReport(CommonVO vo) throws Exception {
		logger.info("--------- getXSSFWorkbookForReport ---------");
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Object[]> reportData = new ArrayList<Object[]>();
		String reportType = vo.getTabIndex();
		String personId = vo.getPersonId();
		List<String> sponsorCodes = vo.getSponsorCodes();
		logger.info("personId : " + personId);
		logger.info("reportType : " + reportType);
		try {
			if (reportType.equals("Proposals by PI")) {
				reportData = reportDao.getReportDataOfProposalsByPIForDownload(personId, reportData);
				XSSFSheet sheet = workbook.createSheet("Proposals by PI");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Category", "Proposal Type", "Status", "Sponsor", "Sponsor Deadline", "Direct Cost", "Indirect Cost", "Total Cost"};
				dashboardService.prepareExcelSheet(reportData, sheet, tableHeadingRow, workbook, vo);
			} else if (reportType.equals("Proposals by Sponsor Type")) {
				reportData = reportDao.getReportDataOfProposalsBySponsorTypeForDownload(personId, sponsorCodes, reportData);
				XSSFSheet sheet = workbook.createSheet("Proposals by Sponsor Type");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Sponsor", "Sponsor Type", "Proposal Type", "PI", "Sponsor Deadline"};
				dashboardService.prepareExcelSheet(reportData, sheet, tableHeadingRow, workbook, vo);
			}
		} catch (Exception e) {
			logger.error("Error in method getXSSFWorkbookForDashboard", e);
		}
		return workbook;
	}

	@Override
	public String getProposalDataBySponsorTypes(ReportVO reportVO) {
		List<Proposal> proposals = new ArrayList<Proposal>();
		String personId = reportVO.getPersonId();
		List<String> sponsorCodes = reportVO.getSponsorCodes();	
		logger.info("personId :"+ personId);
		logger.info("sponsorCodes :"+ sponsorCodes);
		proposals = reportDao.getProposalBySponsorTypes(personId, sponsorCodes);
		reportVO.setProposals(proposals);
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	public void getPieChartDataBySponsorType(ReportVO reportVO) {
		List<ResearchSummaryPieChart> proposalBySponsorType = new ArrayList<ResearchSummaryPieChart>();
		proposalBySponsorType = dashboardDao.getSummaryProposalPieChart(reportVO.getPersonId(), null, reportVO.getIsAdmin(), proposalBySponsorType);
		reportVO.setProposalBySponsorType(proposalBySponsorType);	
	}

}
