package com.polus.fibicomp.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.report.service.ReportService;
import com.polus.fibicomp.report.vo.ReportVO;

@RestController
public class ReportController {

	protected static Logger logger = Logger.getLogger(ReportController.class.getName());

	@Autowired
	@Qualifier(value = "reportService")
	private ReportService reportService;

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

}
