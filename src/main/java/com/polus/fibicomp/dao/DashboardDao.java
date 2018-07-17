package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.view.MobileProposalView;
import com.polus.fibicomp.view.ResearchSummaryView;
import com.polus.fibicomp.vo.CommonVO;

/**
 * @author sasi
 *
 */
@Service
public interface DashboardDao {

	/**
	 * This method is used to get dashboard research summary data including
	 * piechart and table.
	 * 
	 * @param personId
	 *            - ID of the logged in person.
	 * @return Set of values used to figure out piechart and research summary
	 *         table.
	 * @throws Exception
	 */
	public String getDashBoardResearchSummary(String personId) throws Exception;

	/**
	 * This method is used to get list of awards.
	 * @param vo- Object of CommonVO class.
	 * @return A list of active awards.
	 */
	public DashBoardProfile getDashBoardDataForAward(CommonVO vo);

	/**
	 * This method is used to get list of proposal.
	 * @param vo - Object of CommonVO class.
	 * @return A list of proposal.
	 */
	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo);

	/**
	 * This method is used to get list of IRB protocols.
	 * @param vo - Object of CommonVO class.
	 * @return A list of IRB protocols.
	 */
	public DashBoardProfile getProtocolDashboardData(CommonVO vo);

	/**
	 * This method is used to get list of IACUC protocols.
	 * 
	 * @param vo
	 *            - Object of CommonVO class.
	 * @return A list of IACUC protocols.
	 */
	public DashBoardProfile getDashBoardDataForIacuc(CommonVO vo);

	/**
	 * This method is used to get list of COI Disclosure.
	 * @param vo - Object of CommonVO class.
	 * @return A list of disclosure.
	 */
	public DashBoardProfile getDashBoardDataForDisclosures(CommonVO vo);

	/**
	 * This method is used to get list of Committee.
	 * @param vo - Object of CommonVO class.
	 * @return A list of committee.
	 */
	public DashBoardProfile getDashBoardDataForCommittee(CommonVO vo);

	/**
	 * This method is used to get list of CommitteeSchedule.
	 * @param vo - Object of CommonVO class.
	 * @return A list of committe schedule.
	 */
	public DashBoardProfile getDashBoardDataForCommitteeSchedule(CommonVO vo);

	/**
	 * This method is used to retrieve award data in piechart based on sponsor type.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in piechart 
	 * @return A list of award data based on award type.
	 * @throws Exception
	 */
	public String getAwardBySponsorTypes(String personId, String sponsorCode) throws Exception;

	/**
	 * This method is used to retrieve proposal data in piechart based on sponsor type.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in piechart 
	 * @return A list of award data based on award type.
	 * @throws Exception
	 */
	public String getProposalBySponsorTypes(String personId, String sponsorCode) throws Exception;

	/**
	 * This method is used to retrieve list of pending action.
	 * @param personId - ID of the user.
	 * @return A list of actions.
	 */
	public List<ActionItem> getUserNotification(String personId);

	/**
	 * This method is used to retrieve list of in_progress proposals.
	 * @param personId - ID of the user.
	 * @return A list of Proposals in progress.
	 * @throws Exception 
	 */
	public DashBoardProfile getProposalsInProgress(String personId) throws Exception;

	/**
	 * This method is used to retrieve list of submitted proposals.
	 * @param personId - ID of the user.
	 * @return A list of Submitted proposals.
	 * @throws Exception 
	 */
	public DashBoardProfile getSubmittedProposals(String personId) throws Exception;

	/**
	 * This method is used to retrieve list of active awards.
	 * @param personId - ID of the user.
	 * @return A list of active awards
	 * @throws Exception 
	 */
	public DashBoardProfile getActiveAwards(String personId) throws Exception;

	/**
	 * This method is used to retrieve inProgress proposal data by sponsor in donutChart.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart 
	 * @return A list of proposal data based on status = inProgress.
	 * @throws Exception
	 */
	public String getInProgressProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception;

	/**
	 * This method is used to retrieve awarded proposal data by sponsor in donutChart.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart 
	 * @return A list of proposal data based on status = Awarded.
	 * @throws Exception
	 */
	public String getAwardedProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception;

	/**
	 * @param person_id - Logged User ID
	 * @param summaryTable
	 * @return List of Research Summary View
	 */
	public List<ResearchSummaryView> getSummaryTable(String person_id, List<ResearchSummaryView> summaryTable);

	/**
	 * @param personId
	 * @param summaryTable for Research summary data for Mobile
	 * @return List of summary object
	 */
	public List<Object[]> getFibiSummaryTable(String personId, List<Object[]> summaryTable);

	/**
	 * This method is used to get list of proposal.
	 * @param vo - Object of CommonVO class.
	 * @return A list of proposal.
	 */
	public List<MobileProposalView> getProposalsByParams(CommonVO vo);

	/**
	 * This method is used to get list of proposals for certification.
	 * @param personId - Logged User ID
	 * @return a String of details of selected item
	 */
	public List<MobileProposalView> getProposalsForCertification(String personId);

	/**
	 * This method is used to get list of Grant Calls.
	 * @param vo - Object of CommonVO class.
	 * @return A list of grantCalls.
	 */
	public DashBoardProfile getDashBoardDataForGrantCall(CommonVO vo);

	/**
	 * This method is used to get list of SMU proposals.
	 * @param vo - Object of CommonVO class.
	 * @return A list of proposals.
	 */
	public DashBoardProfile getDashBoardDataForSmuProposal(CommonVO vo);

}
