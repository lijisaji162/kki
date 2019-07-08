package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.view.MobileProposalView;
import com.polus.fibicomp.view.ResearchSummaryPieChart;
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
	 * @param unitNumber - department Id selected by the user.
	 * @param isAdmin - flag that tells whether logged in user is admin or PI.
	 * @return Set of values used to figure out piechart and research summary
	 *         table.
	 * @throws Exception
	 */
	public String getDashBoardResearchSummary(String personId, String unitNumber, boolean isAdmin, String userName, Boolean isSuperUser) throws Exception;

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
	//public DashBoardProfile getDashBoardDataForProposal(CommonVO vo);

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
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of award data based on award type.
	 * @throws Exception
	 */
	public String getAwardBySponsorTypes(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception;

	/**
	 * This method is used to retrieve proposal data in piechart based on sponsor type.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in piechart
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of award data based on award type.
	 * @throws Exception
	 */
	public String getProposalBySponsorTypes(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception;

	/**
	 * This method is used to retrieve list of pending action.
	 * @param personId - ID of the user.
	 * @return A list of actions.
	 */
	public List<ActionItem> getUserNotification(String personId);

	/**
	 * This method is used to retrieve list of in_progress proposals.
	 * @param personId - ID of the user.
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of Proposals in progress.
	 * @throws Exception 
	 */
	public DashBoardProfile getProposalsInProgress(String personId, boolean isAdmin, String unitNumber, String userName, Boolean isSuperUser) throws Exception;

	/**
	 * This method is used to retrieve list of submitted proposals.
	 * @param personId - ID of the user.
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of Submitted proposals.
	 * @throws Exception 
	 */
	public DashBoardProfile getSubmittedProposals(String personId, boolean isAdmin, String unitNumber, String userName, Boolean isSuperUser) throws Exception;

	/**
	 * This method is used to retrieve list of active awards.
	 * @param personId - ID of the user.
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of active awards
	 * @throws Exception 
	 */
	public DashBoardProfile getActiveAwards(String personId, boolean isAdmin, String unitNumber) throws Exception;

	public DashBoardProfile getApprovalInProposals(String personId, boolean isAdmin, String unitNumber, String userName, Boolean isSuperUser) throws Exception;

	/**
	 * This method is used to retrieve inProgress proposal data by sponsor in donutChart.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user. 
	 * @return A list of proposal data based on status = inProgress.
	 * @throws Exception
	 */
	public String getInProgressProposalsBySponsorExpanded(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception;

	/**
	 * This method is used to retrieve awarded proposal data by sponsor in donutChart.
	 * @param personId - Logged User ID
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart
	 * @param isAdmin - flag that tells logged user is admin or PI.
	 * @param unitNumber - unit selected by the user.
	 * @return A list of proposal data based on status = Awarded.
	 * @throws Exception
	 */
	public String getAwardedProposalsBySponsorExpanded(String personId, String sponsorCode, boolean isAdmin, String unitNumber) throws Exception;

	/**
	 * @param person_id - Logged User ID
	 * @param summaryTable
	 * @return List of Research Summary View
	 */
	public List<ResearchSummaryView> getSummaryTable(String person_id, String unitNumber, boolean isAdmin, String userName, Boolean isSuperUser, List<ResearchSummaryView> summaryTable);

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
	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo);

	/**
	 * This method is used to get list of my proposals.
	 * @param vo - Object of CommonVO class.
	 * @return A list of proposals.
	 */
	public DashBoardProfile getDashBoardDataForMyProposal(CommonVO vo);

	/**
	 * This method is used to get list of pending review proposals.
	 * @param vo - Object of CommonVO class.
	 * @param proposalIds - A list of proposal id's.
	 * @return A list of proposals.
	 */
	public DashBoardProfile getDashBoardDataForReviewPendingProposal(CommonVO vo, List<Integer> proposalIds);

	/**
	 * This method is used to get list of pending review proposals.
	 * @param vo - Object of CommonVO class.
	 * @param proposalIds - A list of proposal id's.
	 * @return A list of proposals.
	 */
	public List<Integer> getApprovalInprogressProposalIds(String personId, String approvalStatusCode, Integer moduleCode);

	/**
	 * This method is used to get list of inprogress proposals.
	 * @param personId - Logged in person Id.
	 * @param proposals - for inprogress proposals data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @param userName - user name.
	 * @return A list of inprogress proposals.
	 * @throws Exception
	 */
	public List<Object[]> getInprogressProposalsForDownload(String personId, List<Object[]> proposals, String unitNumber, boolean isAdmin, String userName, Boolean isSuperUser) throws Exception;

	/**
	 * This method is used to get list of submitted proposals.
	 * @param personId - Logged in person Id.
	 * @param proposals - for submitted proposals data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @param userName - user name.
	 * @return A list of submitted proposals.
	 * @throws Exception
	 */
	public List<Object[]> getSubmittedProposalsForDownload(String personId, List<Object[]> proposals, String unitNumber, boolean isAdmin, String userName, Boolean isSuperUser) throws Exception;

	public List<Object[]> getApprovalInprogressProposalsForDownload(String personId, List<Object[]> proposals, String unitNumber, boolean isAdmin, String userName, Boolean isSuperUser) throws Exception;

	/**
	 * This method is used to get list of active awards.
	 * @param personId - Logged in person Id.
	 * @param awards - for active awards data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @return A list of submitted proposals.
	 * @throws Exception
	 */
	public List<Object[]> getActiveAwardsForDownload(String personId, List<Object[]> awards, String unitNumber, boolean isAdmin) throws Exception;

	/**
	 * This method is used to get list of inprogress proposals by sponsor.
	 * @param personId - Logged in person Id.
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart
	 * @param proposals - for inprogress proposals by sponsor data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @return A list of inprogress proposals by sponsor.
	 * @throws Exception
	 */
	public List<Object[]> getInProgressProposalsBySponsorForDownload(String personId, String sponsorCode, List<Object[]> proposals, String unitNumber, boolean isAdmin) throws Exception;

	/**
	 * This method is used to get list of awarded proposals by sponsor.
	 * @param personId - Logged in person Id.
	 * @param sponsorCode - sponsor_type_code clicked by user in donutChart.
	 * @param proposals - for awarded proposals by sponsor data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @return A list of awarded proposals by sponsor.
	 * @throws Exception
	 */
	public List<Object[]> getAwardedProposalsBySponsorForDownload(String personId, String sponsorCode, List<Object[]> proposals, String unitNumber, boolean isAdmin) throws Exception;

	/**
	 * This method is used to get list of awards by sponsor types.
	 * @param personId - Logged in person Id.
	 * @param sponsorCode - sponsor_type_code clicked by user in piechart.
	 * @param awards for award by sponsor types data.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @return A list of awards by sponsor types.
	 * @throws Exception
	 */
	public List<Object[]> getAwardBySponsorTypesForDownload(String personId, String sponsorCode, List<Object[]> awards, String unitNumber, boolean isAdmin) throws Exception;

	/**
	 * This method is used to get list of proposals by sponsor types.
	 * @param personId - Logged in person Id.
	 * @param sponsorCode - sponsor_type_code clicked by user in piechart.
	 * @param unitNumber - Unit number.
	 * @param isAdmin - Flag that determines logged user is admin or not.
	 * @param proposals - for proposal by sponsor types data.
	 * @return A list of proposals by sponsor types.
	 * @throws Exception
	 */
	public List<Object[]> getProposalBySponsorTypesForDownload(String personId, String sponsorCode, List<Object[]> proposals, String unitNumber, boolean isAdmin) throws Exception;

	/**
	 * This method is used to get list of proposals.
	 * @param proposals - for proposal data.
	 * @return A list of proposals.
	 * @throws Exception
	 */
	public List<Object[]> getDashBoardDataOfProposalForDownload(List<Object[]> proposals, CommonVO vo) throws Exception;

	/**
	 * This method is used to get list of my proposals.
	 * @param vo - Object of CommonVO class.
	 * @param proposals - for my proposal data.
	 * @return A list of my proposals.
	 * @throws Exception
	 */
	public List<Object[]> getDashBoardDataOfMyProposalForDownload(CommonVO vo,List<Object[]> proposals) throws Exception;

	/**
	 * This method is used to get list of review pending proposals.
	 * @param vo - Object of CommonVO class.
	 * @param proposals - for my proposal data.
	 * @return A list of my review pending proposals.
	 * @throws Exception
	 */
	public List<Object[]> getDashBoardDataOfReviewPendingProposalForDownload(CommonVO vo,List<Object[]> proposals) throws Exception;

	/**
	 * This method is used to get list of awards.
	 * @param vo - Object of CommonVO class.
	 * @param awards - for award data.
	 * @return A list of awards.
	 * @throws Exception
	 */
	public List<Object[]> getDashBoardDataForAwardForDownload(String personId, String sponsorCode,List<Object[]> awards) throws Exception;

	/**
	 * This method is used to get list of protocols.
	 * @param vo - Object of CommonVO class.
	 * @param protocols - for protocol data.
	 * @return A list of protocols.
	 * @throws Exception
	 */
	public List<Object[]> getProtocolDashboardDataForDownload(String personId, String sponsorCode,List<Object[]> protocols) throws Exception;

	/**
	 * This method is used to get password of an user.
	 * @param personId - Logged in person Id.
	 * @return Principal Object.
	 * @throws Exception
	 */
	public PrincipalBo getCurrentPassword(String personId) throws Exception;

	/**
	 * This mathod is used to update password.
	 * @param encryptedPWD - New password to update.
	 * @param personId - Logged in person Id. 
	 * @return An integer value to indicate the response.
	 */
	public Integer changePassword(String encryptedPWD, String personId);

	public List<ResearchSummaryPieChart> getSummaryProposalPieChart(String person_id, String unitNumber, boolean isAdmin,
			List<ResearchSummaryPieChart> summaryProposalPiechart);
}
