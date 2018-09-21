package com.polus.fibicomp.proposal.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.compilance.pojo.ProposalSpecialReview;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.pojo.ProposalType;
import com.polus.fibicomp.vo.SponsorSearchResult;

@Service
public interface ProposalDao {

	/**
	 * This method is used to fetch status based on status code.
	 * @param statusCode - status code of the proposal.
	 * @return An object of proposal status.
	 */
	public ProposalStatus fetchStatusByStatusCode(Integer statusCode);

	/**
	 * This method is used to fetch all protocols.
	 * @return A list of protocols.
	 */
	public List<Protocol> fetchAllProtocols();

	/**
	 * This method is used to fetch all proposal attachment types.
	 * @return A list of proposal attachment types.
	 */
	public List<ProposalAttachmentType> fetchAllProposalAttachmentTypes();

	/**
	 * This method is used to fetch all grant calls.
	 * @return A list of grant calls.
	 */
	public List<GrantCall> fetchAllGrantCalls();

	/**
	 * This method is used to fetch all proposal person roles.
	 * @return A list of roles of proposal person.
	 */
	public List<ProposalPersonRole> fetchAllProposalPersonRoles();

	/**
	 * This method is used to fetch all proposal research types.
	 * @return A list of research types of proposal.
	 */
	public List<ProposalResearchType> fetchAllProposalResearchTypes();

	/**
	 * This method is used to save or update a proposal
	 * @param proposal - Object of a proposal.
	 * @return An object of proposal.
	 */
	public Proposal saveOrUpdateProposal(Proposal proposal);

	/**
	 * This method is used to fetch proposal based on id.
	 * @param proposalId - Id of a proposal.
	 * @return A proposal object.
	 */
	public Proposal fetchProposalById(Integer proposalId);

	/**
	 * This method is used to fetch all area of excellence.
	 * @return A list of area of excellence.
	 */
	public List<ProposalExcellenceArea> fetchAllAreaOfExcellence();

	/**
	 * This method is used to fetch attachment based on Id.
	 * @param attachmentId - Id of the attachment.
	 * @return An attachment object.
	 */
	public ProposalAttachment fetchAttachmentById(Integer attachmentId);

	/**
	 * This method is used to fetch all proposal types.
	 * @return A list of proposal types.
	 */
	public List<ProposalType> fetchAllProposalTypes();

	/**
	 * This method is used to fetch all activity types.
	 * @return A list of activity types.
	 */
	public List<ActivityType> fetchAllActivityTypes();

	/**
	 * This method is used to fetch all sponsors.
	 * @return A list of sponsors.
	 */
	public List<Sponsor> fetchAllSponsors();

	/**
	 * This method is used to fetch lead units based on unit numbers.
	 * @param unitNumbers - lead unit numbers.
	 * @return A list of units.
	 */
	public List<Unit> fetchLeadUnitsByUnitNumbers(Set<String> unitNumbers);

	/**
	 * This method is used to delete special review of a proposal.
	 * @param specialReview - special review object.
	 * @return an object of deleted special review.
	 */
	public ProposalSpecialReview deleteProposalSpecialReview(ProposalSpecialReview specialReview);

	/**
	 * This method is used to fetch filtered sponsors based on input string.
	 * @param searchString - input string.
	 * @return a list of sponsors.
	 */
	public List<SponsorSearchResult> findSponsor(String searchString);

	/**
	 * @param sponsorCode
	 * @return
	 */
	public String fetchSponsorTypeCodeBySponsorCode(String sponsorCode);
}
