package com.polus.fibicomp.proposal.prereview.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewType;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;

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

	public PreReviewStatus getPreReviewStatusByCode(String statusCode);

	public ProposalPreReview saveOrUpdatePreReview(ProposalPreReview preReview);

	public List<ProposalPreReview> loadAllProposalPreReviewsByProposalId(Integer proposalId);

	public List<ProposalPreReview> fetchPreReviewsByCriteria(Integer proposalId, String personId, String preReviewStatus);

}
