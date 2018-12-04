package com.polus.fibicomp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.dao.DashboardDao;
import com.polus.fibicomp.dao.LoginDao;
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

	@Autowired
	private LoginDao loginDao;

	@Override
	public String getDashBoardResearchSummary(CommonVO vo) throws Exception {
		return dashboardDao.getDashBoardResearchSummary(vo.getPersonId(), vo.getUnitNumber(), vo.getIsAdmin());
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
				/*if (vo.getIsUnitAdmin()) {
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
				}*/
				String proposalTabName = vo.getProposalTabName();
				logger.info("proposalTabName : " + proposalTabName);
				if (proposalTabName.equals("MY_PROPOSAL")) {
					dashBoardProfile = dashboardDao.getDashBoardDataForMyProposal(vo);
				} else if (proposalTabName.equals("REVIEW_PENDING_PROPOSAL")) {
					List<Integer> proposalIds = dashboardDao.getApprovalInprogressProposalIds(vo.getPersonId(), Constants.WORKFLOW_STATUS_CODE_WAITING, Constants.MODULE_CODE_PROPOSAL);
					if (proposalIds != null && !proposalIds.isEmpty()) {
						dashBoardProfile = dashboardDao.getDashBoardDataForReviewPendingProposal(vo, proposalIds);							
					} else {
						dashBoardProfile.setProposal(new ArrayList<>());
					}
				} else if (vo.getIsUnitAdmin() && proposalTabName.equals("PROPOSAL")) {
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
			dashBoardProfile.setUnitAdministrators(loginDao.isUnitAdmin(vo.getPersonId()));
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
		boolean isAdmin = vo.getIsAdmin();
		String unitNumber = vo.getUnitNumber();		
		logger.info("personId :"+ personId);
		logger.info("sponsorCode :"+ sponsorCode);
		logger.info("pieChartIndex :"+ pieChartIndex);
		logger.info("isAdmin :"+ isAdmin);
		logger.info("unitNumber :"+ unitNumber);
		try {
			if (pieChartIndex.equals("AWARD")) {
				pieChartDataByType = dashboardDao.getAwardBySponsorTypes(personId, sponsorCode, isAdmin, unitNumber);
			}
			if (pieChartIndex.equals("PROPOSAL")) {
				pieChartDataByType = dashboardDao.getProposalBySponsorTypes(personId, sponsorCode, isAdmin, unitNumber);
			}
		} catch (Exception e) {
			logger.error("Error in method getPieChartDataByType", e);
		}
		return pieChartDataByType;
	}

	@Override
	public String getDetailedSummaryData(CommonVO vo) throws Exception {
		logger.info("---------getDetailedSummaryData---------");
		String personId = vo.getPersonId();
		String researchSummaryIndex = vo.getResearchSummaryIndex();
		boolean isAdmin = vo.getIsAdmin();
		String unitNumber = vo.getUnitNumber();
		logger.info("personId :"+ personId);
		logger.info("researchSummaryIndex :"+ researchSummaryIndex);
		logger.info("isAdmin :"+ isAdmin);
		logger.info("unitNumber :"+ unitNumber);
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId, isAdmin, unitNumber);
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId, isAdmin, unitNumber);
			}
			if (researchSummaryIndex.equals("AWARDSACTIVE")) {
				dashBoardProfile = dashboardDao.getActiveAwards(personId, isAdmin, unitNumber);
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
		boolean isAdmin = vo.getIsAdmin();
		String unitNumber = vo.getUnitNumber();
		logger.info("personId :"+ personId);
		logger.info("sponsorCode :"+ sponsorCode);
		logger.info("donutChartIndex :"+ donutChartIndex);
		logger.info("isAdmin :"+ isAdmin);
		logger.info("unitNumber :"+ unitNumber);
		try {
			if (donutChartIndex.equals("INPROGRESS")) {
				donutChartData = dashboardDao.getInProgressProposalsBySponsorExpanded(personId, sponsorCode, isAdmin, unitNumber);
			}
			if (donutChartIndex.equals("AWARDED")) {
				donutChartData = dashboardDao.getAwardedProposalsBySponsorExpanded(personId, sponsorCode, isAdmin, unitNumber);
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
		boolean isAdmin = false;
		String unitNumber = "";
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error fetching research summary");
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId, isAdmin, unitNumber);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Research summary details fetched successfully");
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId, isAdmin, unitNumber);
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

	private int getColumnsCount(XSSFSheet xssfSheet) {
		int columnCount = 0;
		Iterator<Row> rowIterator = xssfSheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			List<Cell> cells = new ArrayList<>();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				cells.add(cellIterator.next());
			}
			for (int cellIndex = cells.size(); cellIndex >= 1; cellIndex--) {
				Cell cell = cells.get(cellIndex - 1);
				if (cell.toString().trim().isEmpty()) {
					cells.remove(cellIndex - 1);
				} else {
					columnCount = cells.size() > columnCount ? cells.size() : columnCount;
					break;
				}
			}
		}
		return columnCount;
	}

	private File generatePDFFile(File pdfFile, File excelFile, String documentHeading) {
		try {
			FileInputStream inputStream = new FileInputStream(excelFile);
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			if (workbook.getNumberOfSheets() != 0) {
				XSSFSheet worksheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = worksheet.iterator();
				Document document = new Document();
				pdfFile = new File("PDFFile.pdf");
				FileOutputStream outputStream = new FileOutputStream(pdfFile);
				PdfWriter.getInstance(document, outputStream);
				document.open();
				Paragraph paragraph = new Paragraph(documentHeading);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph);
				document.add(Chunk.NEWLINE);
				int columnCount = getColumnsCount(worksheet);
				PdfPTable table = new PdfPTable(columnCount);
				PdfPCell table_cell;
				Font tableHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
				Font tableBodyFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					int rowIndex = row.getRowNum();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							if (rowIndex == 0) {
							} else if (rowIndex == 1) {
								table_cell = new PdfPCell(new Phrase(cell.getStringCellValue(), tableHeadingFont));
								table.addCell(table_cell);
							} else {
								table_cell = new PdfPCell(new Phrase(cell.getStringCellValue(), tableBodyFont));
								table.addCell(table_cell);
							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							Double cellValueInDouble = cell.getNumericCellValue();
							Integer cellValueInInteger = cellValueInDouble.intValue();
							String cellValueInString = Integer.toString(cellValueInInteger);
							table_cell = new PdfPCell(new Phrase(cellValueInString, tableBodyFont));
							table.addCell(table_cell);
							break;
						}
					}
				}
				document.add(table);
				document.close();
				inputStream.close();
			}
		} catch (Exception e) {
			logger.error("Error in method generatePDFFile", e);
		}
		return pdfFile;
	}

	private MultipartFile generateMultiPartFile(MultipartFile multipartFile, File file) {
		try {
			FileInputStream inputStream = new FileInputStream(file);
			multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(inputStream));
			inputStream.close();
		} catch (Exception e) {
			logger.error("Error in method generateMultiPartFile", e);
		}
		return multipartFile;
	}

	@Override
	public ResponseEntity<byte[]> getResponseEntityForDownload(CommonVO vo, XSSFWorkbook workbook) throws Exception {
		logger.info("--------- getResponseEntityForExcelDownload ---------");
		File excelFile = new File("ExcelFile.xlsx");
		FileOutputStream outputStream = new FileOutputStream(excelFile);
		workbook.write(outputStream);
		outputStream.close();
		String exportType = vo.getExportType();
		String documentHeading = vo.getDocumentHeading();
		logger.info("exportType : " + exportType);
		logger.info("documentHeading : " + documentHeading);
		File pdfFile = null;
		MultipartFile multipartFile = null;
		if (exportType.equals("pdf")) {
			pdfFile = generatePDFFile(pdfFile, excelFile, documentHeading);
			multipartFile = generateMultiPartFile(multipartFile, pdfFile);
		} else {
			multipartFile = generateMultiPartFile(multipartFile, excelFile);
		}
		return getResponseEntity(multipartFile);
	}

	private ResponseEntity<byte[]> getResponseEntity(MultipartFile multipartFile) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] bytes = multipartFile.getBytes();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentLength(bytes.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in method getResponseEntity", e);
		}
		return attachmentData;
	}

	private void autoSizeColumns(XSSFWorkbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(1);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	private void prepareExcelSheet(List<Object[]> dashboardData, XSSFSheet sheet, Object[] tableHeadingRow, XSSFWorkbook workbook, CommonVO vo) {
		int headingCellNumber = 0;
		String documentHeading = vo.getDocumentHeading();
		logger.info("documentHeading : " + documentHeading);
		// Excel sheet heading style and font creation code.
		Row headerRow = sheet.createRow(0);
		Cell headingCell = headerRow.createCell(0);
		headingCell.setCellValue((String) documentHeading);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, tableHeadingRow.length - 1));
		XSSFFont headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 15);
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		headingCell.setCellStyle(headerStyle);
		// Table head style and font creation code.
		Row tableHeadRow = sheet.createRow(1);
		XSSFCellStyle tableHeadStyle = workbook.createCellStyle();
		tableHeadStyle.setBorderTop(BorderStyle.HAIR);
		tableHeadStyle.setBorderBottom(BorderStyle.HAIR);
		tableHeadStyle.setBorderLeft(BorderStyle.HAIR);
		tableHeadStyle.setBorderRight(BorderStyle.HAIR);
		XSSFFont tableHeadFont = workbook.createFont();
		tableHeadFont.setBold(true);
		tableHeadFont.setFontHeightInPoints((short) 12);
		tableHeadStyle.setFont(tableHeadFont);
		// Table body style and font creation code.
		XSSFCellStyle tableBodyStyle = workbook.createCellStyle();
		tableBodyStyle.setBorderTop(BorderStyle.HAIR);
		tableBodyStyle.setBorderBottom(BorderStyle.HAIR);
		tableBodyStyle.setBorderLeft(BorderStyle.HAIR);
		tableBodyStyle.setBorderRight(BorderStyle.HAIR);
		XSSFFont tableBodyFont = workbook.createFont();
		tableBodyFont.setFontHeightInPoints((short) 12);
		tableBodyStyle.setFont(tableBodyFont);
		// Set table head data to each column.
		for (Object heading : tableHeadingRow) {
			Cell cell = tableHeadRow.createCell(headingCellNumber++);
			cell.setCellValue((String) heading);
			cell.setCellStyle(tableHeadStyle);
		}
		// Set table body data to each column.
		int rowNumber = 2;
		for (Object[] objectArray : dashboardData) {
			Row row = sheet.createRow(rowNumber++);
			int cellNumber = 0;
			for (Object objectData : objectArray) {
				Cell cell = row.createCell(cellNumber++);
				cell.setCellStyle(tableBodyStyle);
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
				} else if (objectData == null) {
					cell.setCellValue((String) " ");
				}
			}
		}
		// Adjust size of table columns according to length of table data.
		autoSizeColumns(workbook);
	}

	@Override
	public XSSFWorkbook getXSSFWorkbookForDashboard(CommonVO vo) throws Exception {
		logger.info("--------- getXSSFWorkbookForDashboard ---------");
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Object[]> dashboardData = new ArrayList<Object[]>();
		String requestType = vo.getTabIndex();
		logger.info("requestType : " + requestType);
		try {
			if (requestType.equals("PROPOSAL")) {
				if (vo.getIsUnitAdmin()) {
					String proposalTabName = vo.getProposalTabName();
					logger.info("proposalTabName : " + proposalTabName);
					if (proposalTabName.equals("MY_PROPOSAL")) {
						dashboardData = dashboardDao.getDashBoardDataOfMyProposalForDownload(vo, dashboardData);
						XSSFSheet sheet = workbook.createSheet("My Proposals");
						Object[] tableHeadingRow = {"Id#", "Title", "PI", "Category", "Type", "Status", "Sponsor", "Sponsor Deadline"};
						prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
					} else if (proposalTabName.equals("REVIEW_PENDING_PROPOSAL")) {
						List<Integer> proposalIds = dashboardDao.getApprovalInprogressProposalIds(vo.getPersonId(), Constants.WORKFLOW_STATUS_CODE_WAITING, Constants.MODULE_CODE_PROPOSAL);
						if (proposalIds != null && !proposalIds.isEmpty()) {
							dashboardData = dashboardDao.getDashBoardDataOfReviewPendingProposalForDownload(vo, dashboardData);
							XSSFSheet sheet = workbook.createSheet("Pending Review");
							Object[] tableHeadingRow = {"Id#", "Title", "PI", "Category", "Type", "Status", "Sponsor", "Sponsor Deadline"};
							prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
						}
					} else {
						dashboardData = dashboardDao.getDashBoardDataOfProposalForDownload(dashboardData);
						XSSFSheet sheet = workbook.createSheet("All Proposals");
						Object[] tableHeadingRow = {"Id#", "Title", "PI", "Category", "Type", "Status", "Sponsor", "Sponsor Deadline"};
						prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
					}
				} else {
					dashboardData = dashboardDao.getDashBoardDataOfProposalForDownload(dashboardData);
					XSSFSheet sheet = workbook.createSheet("Proposals");
					Object[] tableHeadingRow = {"Id#", "Title", "PI", "Category", "Type", "Status", "Sponsor", "Sponsor Deadline"};
					prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
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
		boolean isAdmin = vo.getIsAdmin();
		String unitNumber = vo.getUnitNumber();
		logger.info("personId : " + personId);
		logger.info("dashboardIndex : " + dashboardIndex);
		logger.info("sponsorCode : " + sponsorCode);
		logger.info("isAdmin : " + isAdmin);
		logger.info("unitNumber : " + unitNumber);
		List<Object[]> dashboardData = new ArrayList<Object[]>();
		try {
			if (dashboardIndex.equals("PROPOSALSINPROGRESS")) {
				dashboardData = dashboardDao.getInprogressProposalsForDownload(personId, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("In Progress Proposals");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Sponsor", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("PROPOSALSSUBMITTED")) {
				dashboardData = dashboardDao.getSubmittedProposalsForDownload(personId, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("Submitted Proposals");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Sponsor", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("AWARDSACTIVE")) {
				dashboardData = dashboardDao.getActiveAwardsForDownload(personId, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("Active Awards");
				Object[] tableHeadingRow = {"Award#", "Account", "Title", "Sponsor", "PI", "Budget"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("INPROGRESS")) {
				dashboardData = dashboardDao.getInProgressProposalsBySponsorForDownload(personId, sponsorCode, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("In Progress Proposals By Sponsor");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Type", "Budget", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("AWARDED")) {
				dashboardData = dashboardDao.getAwardedProposalsBySponsorForDownload(personId, sponsorCode, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("Awarded Proposals By Sponsor");
				Object[] tableHeadingRow = {"Award#", "Title", "Type", "Activity Type", "PI"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("AWARD")) {
				dashboardData = dashboardDao.getAwardBySponsorTypesForDownload(personId, sponsorCode, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("Awards by sponsor types");
				Object[] tableHeadingRow = {"Award#", "Account", "Title", "Sponsor", "PI"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			} else if (dashboardIndex.equals("PROPOSAL")) {
				dashboardData = dashboardDao.getProposalBySponsorTypesForDownload(personId, sponsorCode, dashboardData, unitNumber, isAdmin);
				XSSFSheet sheet = workbook.createSheet("Proposal by sponsor types");
				Object[] tableHeadingRow = {"Proposal#", "Title", "Sponsor", "Proposal Type", "PI", "Sponsor Deadline"};
				prepareExcelSheet(dashboardData, sheet, tableHeadingRow, workbook, vo);
			}
		} catch (Exception e) {
			logger.error("Error in method getXSSFWorkbookForResearchSummary", e);
		}
		return workbook;
	}

}
