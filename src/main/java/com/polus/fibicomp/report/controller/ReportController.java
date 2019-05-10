package com.polus.fibicomp.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.report.service.ReportService;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class ReportController {

	protected static Logger logger = Logger.getLogger(ReportController.class.getName());

	@Autowired
	@Qualifier(value = "reportService")
	private ReportService reportService;

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(value = "/applicationReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String applicationReport(@RequestBody ReportVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for applicationReport");
		return reportService.applicationReport(vo);
	}

	@RequestMapping(value = "/fetchReportData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchReportData(@RequestBody ReportVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchReportData");
		return reportService.fetchReportData(vo);
	}

	@RequestMapping(value = "/exportReportDatas", method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportReportDatas(HttpServletRequest request, @RequestBody CommonVO vo) throws Exception {
		logger.info("Requesting for exportReportDatas");
		XSSFWorkbook workbook = reportService.getXSSFWorkbookForReport(vo);
		return dashboardService.getResponseEntityForDownload(vo, workbook);
		// return reportService.getResponseEntityForDownload(vo, workbook);
	}

	@RequestMapping(value = "/getPieChartDataBySponsorTypes", method = RequestMethod.POST)
	public String getPieChartDataBySponsorTypes(@RequestBody ReportVO vo, HttpServletRequest request) throws Exception {
		logger.info("Requesting for getPieChartDataBySponsorTypes");
		return reportService.getProposalDataBySponsorTypes(vo);
	}

}
