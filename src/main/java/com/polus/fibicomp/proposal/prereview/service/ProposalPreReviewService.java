package com.polus.fibicomp.proposal.prereview.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface ProposalPreReviewService {

	public String createProposalPreReview(ProposalVO proposalVO);

}
