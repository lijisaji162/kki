package com.polus.fibicomp.proposal.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Transactional
@Service(value = "proposalService")
public interface ProposalService {

	/**
	 * This method is used to create proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return Set of values to create proposal.
	 */
	public String createProposal(ProposalVO vo);

	/**
	 * This method is used to add attachments for a proposal.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the attachment.
	 * @return A String of details of proposal data with list of attachments.
	 */
	public String addProposalAttachment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to save or update proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return A string of details of a proposal.
	 */
	public String saveOrUpdateProposal(ProposalVO vo);

	/**
	 * This method is used to load proposal based on id.
	 * @param proposalId - Id of the proposal.
	 * @param personId - currentUser.
	 * @return A string of details of a proposal.
	 */
	public String loadProposalById(Integer proposalId, String personId);

	/**
	 * This method is used fetch cost elements based on budget category.
	 * @param vo - Object of ProposalVO class.
	 * @return A string of details of proposal which include list of cost elements.
	 */
	public String fetchCostElementByBudgetCategory(ProposalVO vo);

	/**
	 * This method is used fetch all area of excellence.
	 * @param vo - Object of ProposalVO class.
	 * @return A string of details of proposal which include list of area of excellence.
	 */
	public String fetchAllAreaOfExcellence(ProposalVO vo);

	/**
	 * This method is used to delete keywords from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of keywords.
	 */
	public String deleteProposalKeyword(ProposalVO vo);

	/**
	 * This method is used to delete area of research from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of area of research.
	 */
	public String deleteProposalResearchArea(ProposalVO vo);

	/**
	 * This method is used to delete proposal person from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of persons.
	 */
	public String deleteProposalPerson(ProposalVO vo);

	/**
	 * This method is used to delete declared sponsors from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of sponsors.
	 */
	public String deleteProposalSponsor(ProposalVO vo);

	/**
	 * This method is used to delete declared protocols from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of protocols.
	 */
	public String deleteIrbProtocol(ProposalVO vo);

	/**
	 * This method is used to delete attachment from proposal.
	 * @param vo - Object of ProposalVO class. 
	 * @return a String of details of proposal with updated list of attachments.
	 */
	public String deleteProposalAttachment(ProposalVO vo);

	/**
	 * This method is used to download proposal attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadProposalAttachment(Integer attachmentId);

	/**
	 * This method is used to submit proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public String submitProposal(ProposalVO proposalVO);

	/**
	 * This method is used to approve or disapprove a proposal.
	 * @param files - Files need to be attached.
	 * @param formDataJSON - Request object data.
	 * @return a String of details of proposal.
	 */
	public String approveOrRejectProposal(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to delete special review of a proposal.
	 * @param proposalVO - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public String deleteProposalSpecialReview(ProposalVO proposalVO);

	/**
	 * This method is used to fetch filtered sponsors based on input string.
	 * @param searchString - input string.
	 * @return a list of sponsors.
	 */
	public List<SponsorSearchResult> findSponsor(String searchString);

	/**
	 * This method is used to load initial proposal data.
	 * @param vo - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public void loadInitialData(ProposalVO proposalVO);

	/**
	 * This method is used to send attachment complete notification to PI.
	 * @param vo - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public String sendAttachPINotification(ProposalVO proposalVO);

	/**
	 * This method is used to send attachments completed notification to final approver.
	 * @param vo - Object of ProposalVO class.
	 * @return a String of details of proposal.
	 */
	public String sendAttachApproverNotification(ProposalVO proposalVO);

	public List<Rolodex> getNonEmployee(String searchString);

	public String getPrincipalInvestigator(List<ProposalPerson> proposalPersons);

}
