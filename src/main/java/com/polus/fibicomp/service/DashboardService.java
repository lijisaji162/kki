package com.polus.fibicomp.service;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.vo.CommonVO;

/**
 * Dashboard Service class to get dashboard details.
 *
 */
@Service
public interface DashboardService {

	/**
	 * This method is used to get dashboard research summary data for piechart and table. 
	 * @param vo - Object of CommonVO.
	 * @return Set of values used to figure out piechart and research summary table.
	 */
	public String getDashBoardResearchSummary(CommonVO vo) throws Exception;

	/**
	 * This method is used to retrieve dashboard data based on tabIndex.
	 * @param vo - Object of CommonVO
	 * @return A list of dashboard data based on tabIndex.
	 * @throws Exception
	 */
	public String getDashBoardData(CommonVO vo) throws Exception;

	/**
	 * This method is used to retrieve award data in piechart based on sponsor type.
	 * @param personId - Logged User ID
	 * @param vo - Object of CommonVO
	 * @return A list of selected pie chart items based on type.
	 * @throws Exception
	 */
	public String getPieChartDataByType(CommonVO vo) throws Exception;

	/**
	 * This method is used to get list of pending actions.
	 * @param personId - Logged User ID
	 * @return a list of pending actions.
	 * @throws Exception
	 */

	public List<ActionItem> getUserNotification(String personId);

	/**
	 * This method is used to get list of selected type in ResearchSummaryTable.
	 * @param personId - Logged User ID
	 * @param researchSummaryIndex - Selected row in Summary table
	 * @return a String of details of selected item
	 * @throws Exception
	 */
	public String getDetailedSummaryData(CommonVO vo) throws Exception;

	/**
	 * This method is used to retrieve proposal data in donutChart based on status(in progress/awarded).
	 * @param vo - Object of CommonVO
	 * @return A list of selected donutChart items based on status.
	 * @throws Exception
	 */
	public String getDonutChartDataBySponsor(CommonVO vo);

	/**
	 * This method is used to retrieve dashboard summary table data for FibiMobile.
	 * @param personId
	 * @return research summary table for fibiMobile
	 * @throws Exception 
	 */
	public String getFibiResearchSummary(String personId) throws Exception;

	/**
	 * This method is used for searching proposal/award/protocol data using specific fields for FibiMobile.
	 * @param vo - object of CommonVO
	 * @return Search Result
	 * @throws Exception 
	 */
	public String getProposalsBySearchCriteria(CommonVO vo) throws Exception;

	/**
	 * This method is used to get list of selected type in ResearchSummaryTable.
	 * @param personId - Logged User ID
	 * @param researchSummaryIndex - Selected row in Summary table
	 * @return a String of details of selected item
	 * @throws Exception
	 */
	public String getFibiResearchSummary(String personId, String researchSummaryIndex) throws Exception;						

	/**
	 * This method is used to get list of proposals for certification.
	 * @param personId - Logged User ID
	 * @return a String of details of selected item
	 */
	public String getProposalsForCertification(String personId);

	/**
	 * This method is used to get XSSFWorkbook based on index clicked in research summary tab.
	 * @param vo - for input of user, dashboardIndex and sponsorCode.
	 * @return XSSFWorkbook that contains excel sheet with data.
	 * @throws Exception
	 */
	public XSSFWorkbook getXSSFWorkbookForResearchSummary(CommonVO vo) throws Exception;

	/**
	 * This method is used to get XSSFWorkbook based on index tab clicked in dashboard.
	 * @param vo - object of CommonVO.
	 * @param XSSFWorkbook for excel sheet preparation.
	 * @return XSSFWorkbookthat contains excel sheet with data.
	 * @throws Exception
	 */
	public XSSFWorkbook getXSSFWorkbookForDashboard(CommonVO vo) throws Exception;

	/**
	 * This method is used to get excel sheet in byte array format.
	 * @param XSSFWorkbook for excel sheet.
	 * @return ResponseEntity<byte[]> that contains data in byte array.
	 * @throws Exception
	 */
	public ResponseEntity<byte[]> getResponseEntityForDownload(CommonVO vo, XSSFWorkbook workbook) throws Exception;

	/**
	 * This method is used to change the existing password.
	 * @param vo - object of CommonVO
	 * @return A String of details having updated message.
	 * @throws Exception
	 */
	public String changePassword(CommonVO vo) throws Exception;

}
