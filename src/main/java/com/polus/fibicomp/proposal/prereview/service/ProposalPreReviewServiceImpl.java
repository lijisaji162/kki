package com.polus.fibicomp.proposal.prereview.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.prereview.dao.ProposalPreReviewDao;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "proposalPreReviewService")
public class ProposalPreReviewServiceImpl implements ProposalPreReviewService {

	protected static Logger logger = Logger.getLogger(ProposalPreReviewServiceImpl.class.getName());

	@Autowired
	private ProposalPreReviewDao proposalPreReviewDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public String createProposalPreReview(ProposalVO proposalVO) {
		ProposalPreReview preReview = proposalVO.getNewProposalPreReview();
		List<ProposalPreReview> preReviewExists = proposalPreReviewDao.fetchPreReviewsByCriteria(preReview.getProposalId(), preReview.getReviewerPersonId(), Constants.PRE_REVIEW_STATUS_INPROGRESS);
		if (preReviewExists != null && !preReviewExists.isEmpty()) {
			proposalVO.setPreReviewExist(true);
		} else {
			PreReviewStatus reviewStatus = proposalPreReviewDao.getPreReviewStatusByCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setReviewStatusCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setPreReviewStatus(reviewStatus);
			preReview = proposalPreReviewDao.saveOrUpdatePreReview(preReview);
		}
		proposalVO.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviews());
		return committeeDao.convertObjectToJSON(proposalVO);
	}

}
