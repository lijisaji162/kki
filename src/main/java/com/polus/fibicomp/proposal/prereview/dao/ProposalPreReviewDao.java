package com.polus.fibicomp.proposal.prereview.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewType;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewer;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReviewAttachment;

@Service
public interface ProposalPreReviewDao {

	/**
	 * This method is used to fetch all pre review types.
	 * @return An list of pre review types.
	 */
	public List<PreReviewType> fetchAllPreReviewTypes();

	/**
	 * This method is used to fetch all pre review status.
	 * @return An list of pre review status.
	 */
	public List<PreReviewStatus> fetchAllPreReviewStatus();

	/**
	 * This method is used to get pre review status by status code.
	 * @param statusCode - Id of the PreReviewStatus.
	 * @return An object of pre review status.
	 */
	public PreReviewStatus getPreReviewStatusByCode(String statusCode);

	/**
	 * This method is used to save or update proposal pre review.
	 * @param preReview - Object of ProposalPreReview class.
	 * @return An object of ProposalPreReview.
	 */
	public ProposalPreReview saveOrUpdatePreReview(ProposalPreReview preReview);

	/**
	 * This method is used to load all ProposalPreReview by Proposal id.
	 * @param proposalId - Id of Proposal object.
	 * @return An list of ProposalPreReview.
	 */
	public List<ProposalPreReview> loadAllProposalPreReviewsByProposalId(Integer proposalId);

	/**
	 * This method is used to fetch ProposalPreReview by criteria.
	 * @param proposalId - Id of Proposal object.
	 * @param personId - Id of reviewer person id.
	 * @param preReviewStatus - status code of PreReviewStatus object.
	 * @return An list of ProposalPreReview.
	 */
	public List<ProposalPreReview> fetchPreReviewsByCriteria(Integer proposalId, String personId, String preReviewStatus);

	/**
	 * This method is used to fetch ProposalPreReviewAttachment by Id.
	 * @param attachmentId - Id of ProposalPreReviewAttachment object.
	 * @return An object of ProposalPreReviewAttachment.
	 */
	public ProposalPreReviewAttachment fetchAttachmentById(Integer attachmentId);

	/**
	 * This method is used to fetch all PreReviewer.
	 * @return A list of PreReviewer.
	 */
	public List<PreReviewer> fetchAllPreReviewer();

}
