package com.polus.fibicomp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.service.LoginService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class DashboardController {

	protected static Logger logger = Logger.getLogger(DashboardController.class.getName());

	@Autowired
	@Qualifier(value = "dashboardService")
	private DashboardService dashboardService;

	@Autowired
	@Qualifier(value = "loginService")
	private LoginService loginService;

	@RequestMapping(value = "/getResearchSummaryData", method = RequestMethod.POST)
	public String requestResearchSummaryData(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getDashBoardResearchSummary(vo);
	}

	@RequestMapping(value = "/fibiDashBoard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String requestInitialLoad(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getDashBoardData(vo);
	}

	@RequestMapping(value = "/getPieChartDataByType", method = RequestMethod.POST)
	public String requestPieChartDataBySponsorTypes(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getPieChartDataByType(vo);
	}

	@RequestMapping(value = "/getDetailedResearchSummary", method = RequestMethod.POST)
	public String requestDetailedResearchSummary(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getDetailedSummaryData(vo);
	}

	@RequestMapping(value = "/getUserNotification", method = RequestMethod.POST)
	public List<ActionItem> getUserNotification(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getUserNotification(vo.getPersonId());
	}

	@RequestMapping(value = "/getDonutChartDataBySponsor", method = RequestMethod.POST)
	public String requestDonutChartDataBySponsor(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.getDonutChartDataBySponsor(vo);
	}

	@RequestMapping(value = "/exportResearchSummaryDatas", method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportResearchSummaryDatas(HttpServletRequest request, @RequestBody CommonVO vo) throws Exception {
		XSSFWorkbook workbook = dashboardService.getXSSFWorkbookForResearchSummary(vo);
		return dashboardService.getResponseEntityForDownload(vo, workbook);
	}

	@RequestMapping(value = "/exportDashboardDatas", method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportDashboardData(HttpServletRequest request, @RequestBody CommonVO vo) throws Exception {
		XSSFWorkbook workbook = dashboardService.getXSSFWorkbookForDashboard(vo);
		return dashboardService.getResponseEntityForDownload(vo, workbook);
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return dashboardService.changePassword(vo);
	}

}
