package com.polus.fibicomp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.constants.Constants;
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
				//dashBoardProfile = dashboardDao.getDashBoardDataForProposal(vo);
				if (vo.getIsUnitAdmin()) {
					String proposalTabName = vo.getProposalTabName();
					logger.info("proposalTabName : " + proposalTabName);
					if (proposalTabName.equals("MY_PROPOSAL")) {
						dashBoardProfile = dashboardDao.getDashBoardDataForMyProposal(vo);
					} else if (proposalTabName.equals("REVIEW_PENDING_PROPOSAL")) {
						List<Integer> proposalIds = dashboardDao.getApprovalInprogressProposalIds(vo.getPersonId(), Constants.WORKFLOW_STATUS_CODE_WAITING, Constants.MODULE_CODE_PROPOSAL);
						if (proposalIds != null && !proposalIds.isEmpty()) {
							dashBoardProfile = dashboardDao.getDashBoardDataForReviewPendingProposal(vo, proposalIds);							
						}
					} else {
						dashBoardProfile = dashboardDao.getDashBoardDataForProposal(vo);
					}
				} else {
					dashBoardProfile = dashboardDao.getDashBoardDataForProposal(vo);
				}
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

	public ResponseEntity<byte[]> getResponseEntityForExcelDownload(XSSFWorkbook workbook) throws Exception {
		File file = new File("DashboardData.xlsx");
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);
		outputStream.close();
		FileInputStream inputStream = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",IOUtils.toByteArray(inputStream));
		inputStream.close();
		ResponseEntity<byte[]> dashboardData = null;
		byte[] bytes = multipartFile.getBytes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
		headers.setContentDispositionFormData("DashboardData.xlsx", "DashboardData.xlsx");
		headers.setContentLength(bytes.length);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		headers.setPragma("public");
		dashboardData = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		return dashboardData;
	}

	public void prepareExcelSheet(List<Object[]> dashboardData, XSSFSheet sheet, Object[] tableHeadingRow) {
		Row headingRow = sheet.createRow(1);
		int headingCellNumber = 0;
		for (Object heading : tableHeadingRow) {
			Cell cell = headingRow.createCell(headingCellNumber++);
			cell.setCellValue((String) heading);
		}
		int rowNumber = 2;
		for (Object[] objectArray : dashboardData) {
			Row row = sheet.createRow(rowNumber++);
			int cellNumber = 0;
			for (Object objectData : objectArray) {
				Cell cell = row.createCell(cellNumber++);
				if (objectData instanceof String)
					cell.setCellValue((String) objectData);
				else if (objectData instanceof Integer)
					cell.setCellValue((Integer) objectData);
				else if (objectData instanceof BigDecimal) {
					String stringValue = ((BigDecimal) objectData).toString();
					cell.setCellValue((String) stringValue);
				} else if (objectData instanceof Date) {
					if (objectData != null) {
						Date date = (Date) objectData;
						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
						String dateValue = dateFormat.format(date);
						cell.setCellValue((String) dateValue);
					}
				}
			}
		}
	}

	@Override
	public XSSFWorkbook getXSSFWorkbookForDashboard(CommonVO vo,XSSFWorkbook workbook) throws Exception {
		logger.info("---------getXSSFWorkbookForDashboard---------");
		List<Object[]> dashboardData = new ArrayList<Object[]>();
		String requestType = vo.getTabIndex();
		logger.info("requestType : " + requestType);
		try {
			if (requestType.equals("PROPOSAL")) {
				if (vo.getIsUnitAdmin()) {
					String proposalTabName = vo.getProposalTabName();
					logger.info("proposalTabName : " + proposalTabName);
					if (proposalTabName.equals("MY_PROPOSAL")) {
						dashboardData = dashboardDao.getDashBoardDataOfMyProposalForDownload(vo,dashboardData);
						XSSFSheet sheet = workbook.createSheet("My Proposals");
						Object[] tableHeadingRow = {"Id#", "Title", "PI","Category","Type","Status","Sponsor","Sponsor Deadline"};
						prepareExcelSheet(dashboardData,sheet,tableHeadingRow);
					} else if (proposalTabName.equals("REVIEW_PENDING_PROPOSAL")) {
						List<Integer> proposalIds = dashboardDao.getApprovalInprogressProposalIds(vo.getPersonId(), Constants.WORKFLOW_STATUS_CODE_WAITING, Constants.MODULE_CODE_PROPOSAL);
						if (proposalIds != null && !proposalIds.isEmpty()) {
							dashboardData = dashboardDao.getDashBoardDataOfReviewPendingProposalForDownload(vo,dashboardData);
							XSSFSheet sheet = workbook.createSheet("Pending Review");
							Object[] tableHeadingRow = {"Id#", "Title", "PI","Category","Type","Status","Sponsor","Sponsor Deadline"};
							prepareExcelSheet(dashboardData,sheet,tableHeadingRow);
						}
					} else {
						dashboardData = dashboardDao.getDashBoardDataOfProposalForDownload(dashboardData);
						XSSFSheet sheet = workbook.createSheet("All Proposals");
						Object[] tableHeadingRow = {"Id#", "Title", "PI","Category","Type","Status","Sponsor","Sponsor Deadline"};
						prepareExcelSheet(dashboardData,sheet,tableHeadingRow);
					}
				} else {
					dashboardData = dashboardDao.getDashBoardDataOfProposalForDownload(dashboardData);
					XSSFSheet sheet = workbook.createSheet("Proposals");
					Object[] tableHeadingRow = {"Id#", "Title", "PI","Category","Type","Status","Sponsor","Sponsor Deadline"};
					prepareExcelSheet(dashboardData,sheet,tableHeadingRow);
				}
			}
		} catch (Exception e) {
			logger.error("Error in method getXSSFWorkbookForDashboard", e);
		}
		return workbook;
	}

	@Override
	public XSSFWorkbook getXSSFWorkbookForResearchSummary(CommonVO vo) throws Exception {
		logger.info("---------getXSSFWorkbookForResearchSummary---------");
		XSSFWorkbook workbook = new XSSFWorkbook();
		String personId = vo.getPersonId();
		String dashboardIndex = vo.getResearchSummaryIndex();
		String sponsorCode = vo.getSponsorCode();
		logger.info("personId : " + personId);
		logger.info("dashboardIndex : " + dashboardIndex);
		logger.info("sponsorCode : " + sponsorCode);
		List<Object[]> dashboardData = new ArrayList<Object[]>();
		try {
			if (dashboardIndex.equals("PROPOSALSINPROGRESS")) {
				dashboardData = dashboardDao.getInprogressProposalsForDownload(personId, dashboardData);
				XSSFSheet sheet = workbook.createSheet("In Progress Proposals");
				Object[] tableHeadingRow = { "Proposal#", "Title", "Sponsor", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("PROPOSALSSUBMITTED")) {
				dashboardData = dashboardDao.getSubmittedProposalsForDownload(personId, dashboardData);
				XSSFSheet sheet = workbook.createSheet("Submitted Proposals");
				Object[] tableHeadingRow = { "Proposal#", "Title", "Sponsor", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("AWARDSACTIVE")) {
				dashboardData = dashboardDao.getActiveAwardsForDownload(personId, dashboardData);
				XSSFSheet sheet = workbook.createSheet("Active Awards");
				Object[] tableHeadingRow = { "Award#", "Account", "Title", "Sponsor", "PI", "Budget"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("INPROGRESS")) {
				dashboardData = dashboardDao.getInProgressProposalsBySponsorForDownload(personId, sponsorCode, dashboardData);
				XSSFSheet sheet = workbook.createSheet("In Progress Proposals By Sponsor");
				Object[] tableHeadingRow = { "Proposal#", "Title", "Type", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("AWARDED")) {
				dashboardData = dashboardDao.getAwardedProposalsBySponsorForDownload(personId, sponsorCode, dashboardData);
				XSSFSheet sheet = workbook.createSheet("Awarded Proposals By Sponsor");
				Object[] tableHeadingRow = { "Award#", "Title", "Type", "Activity Type", "PI"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("AWARD")) {
				dashboardData = dashboardDao.getAwardBySponsorTypesForDownload(personId, sponsorCode, dashboardData);
				XSSFSheet sheet = workbook.createSheet("Awards by sponsor types");
				Object[] tableHeadingRow = { "Award#", "Account", "Title", "Sponsor", "PI"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			} else if (dashboardIndex.equals("PROPOSAL")) {
				dashboardData = dashboardDao.getProposalBySponsorTypesForDownload(personId, sponsorCode, dashboardData);
				XSSFSheet sheet = workbook.createSheet("Proposal by sponsor types");
				Object[] tableHeadingRow = { "Proposal#", "Title", "Sponsor", "Proposal Type", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow);
			}
		} catch (Exception e) {
			logger.error("Error in method getXSSFWorkbookForResearchSummary", e);
		}
		return workbook;
	}

}
