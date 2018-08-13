package com.polus.fibicomp.proposal.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalBudgetCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCostElement;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalInstituteCentreLab;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.pojo.ProposalType;

@Service
public interface ProposalDao {

	/**
	 * This method is used to fetch status based on status code.
	 * @param statusCode - status code of the proposal.
	 * @return An object of proposal status.
	 */
	public ProposalStatus fetchStatusByStatusCode(Integer statusCode);

	/**
	 * This method is used to fetch all proposal categories.
	 * @return A list of proposal categories.
	 */
	public List<ProposalCategory> fetchAllCategories();

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
	 * This method is used to fetch all budget categories.
	 * @return A list of proposal buget categories.
	 */
	public List<ProposalBudgetCategory> fetchAllBudgetCategories();

	/**
	 * This method is used fetch cost elements based on budget category.
	 * @param budgetCategoryCode - category code of a budget.
	 * @return A list of cost elements.
	 */
	public List<ProposalCostElement> fetchCostElementByBudgetCategory(String budgetCategoryCode);

	/**
	 * This method is used to fetch all institutes, Centres and Labs.
	 * @return A list of institutes, Centres and Labs.
	 */
	public List<ProposalInstituteCentreLab> fetchAllInstituteCentrelabs();

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

}
