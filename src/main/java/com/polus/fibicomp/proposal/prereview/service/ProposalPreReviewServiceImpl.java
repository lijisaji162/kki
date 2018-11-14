package com.polus.fibicomp.proposal.prereview.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.prereview.dao.ProposalPreReviewDao;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReviewAttachment;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReviewComment;
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
		Proposal proposal = proposalVO.getProposal();
		ProposalPreReview preReview = proposalVO.getNewProposalPreReview();
		List<ProposalPreReview> preReviewExists = proposalPreReviewDao.fetchPreReviewsByCriteria(preReview.getProposalId(), preReview.getReviewerPersonId(), Constants.PRE_REVIEW_STATUS_INPROGRESS);
		if (preReviewExists != null && !preReviewExists.isEmpty()) {
			proposal.setPreReviewExist(true);
		} else {
			PreReviewStatus reviewStatus = proposalPreReviewDao.getPreReviewStatusByCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setReviewStatusCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setPreReviewStatus(reviewStatus);
			preReview = proposalPreReviewDao.saveOrUpdatePreReview(preReview);
		}
		proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposal.getProposalId()));
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	@Override
	public String addPreReviewComment(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			ProposalPreReview preReview = proposal.getReviewerReview();
			List<ProposalPreReviewComment> preReviewComments = preReview.getProposalPreReviewComments();
			for (ProposalPreReviewComment reviewComment : preReviewComments) {
				if (reviewComment.getPreReviewCommentId() == null) {
					if (files != null && files.length > 0) {
						List<ProposalPreReviewAttachment> newPreReviewAttachments = new ArrayList<>();
						for (int i = 0; i < files.length; i++) {
							File file = new File(files[i].getOriginalFilename());
							String fileName = file.getName();
							ProposalPreReviewAttachment proposalPreReviewAttachment = new ProposalPreReviewAttachment();
							proposalPreReviewAttachment.setPreReviewId(preReview.getPreReviewId());
							proposalPreReviewAttachment.setProposalId(preReview.getProposalId());
							proposalPreReviewAttachment.setFileName(fileName);
							// proposalPreReviewAttachment.setDescription("");
							proposalPreReviewAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
							proposalPreReviewAttachment.setUpdateUser(proposalVO.getUserName());
							proposalPreReviewAttachment.setAttachment(files[i].getBytes());
							proposalPreReviewAttachment.setMimeType(files[i].getContentType());
							newPreReviewAttachments.add(proposalPreReviewAttachment);
						}
						reviewComment.getProposalPreReviewAttachments().addAll(newPreReviewAttachments);
					}
				}
			}
			preReview = proposalPreReviewDao.saveOrUpdatePreReview(preReview);
			proposal.setReviewerReview(preReview);
			proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposal.getProposalId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

}
