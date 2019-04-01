package com.polus.fibicomp.proposal.prereview.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.email.service.FibiEmailService;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
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

	@Autowired
	private FibiEmailService fibiEmailService;

	@Value("${application.context.name}")
	private String context;

	@Override
	public String createProposalPreReview(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		ProposalPreReview preReview = proposalVO.getNewProposalPreReview();
		List<ProposalPreReview> preReviewExists = proposalPreReviewDao.fetchPreReviewsByCriteria(preReview.getProposalId(), preReview.getReviewerPersonId(), Constants.PRE_REVIEW_STATUS_INPROGRESS);
		if (preReviewExists != null && !preReviewExists.isEmpty()) {
			proposal.setPreReviewExist(true);
		} else {
			if (proposalVO.getPersonId().equals(preReview.getReviewerPersonId())) {
				proposal.setReviewerReview(preReview);
				proposal.setIsPreReviewer(true);
			}
			PreReviewStatus reviewStatus = proposalPreReviewDao.getPreReviewStatusByCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setReviewStatusCode(Constants.PRE_REVIEW_STATUS_INPROGRESS);
			preReview.setPreReviewStatus(reviewStatus);
			preReview = proposalPreReviewDao.saveOrUpdatePreReview(preReview);
			Set<String> toAddresses = new HashSet<String>();
			toAddresses.add(preReview.getReviewerEmailAddress());
			String piName = getPrincipalInvestigator(proposal.getProposalPersons());
			String createPreReviewMessage = "The following application has assigned a pre review: <br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
					+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
					+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the application.";
			String createPreReviewSubject = "Action Required: Pre Review for "+ proposal.getTitle();
			fibiEmailService.sendEmail(toAddresses, createPreReviewSubject, null, null, createPreReviewMessage, true);
		}
		proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposal.getProposalId()));
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	public String getPrincipalInvestigator(List<ProposalPerson> proposalPersons) {
		String piName = "";
		for (ProposalPerson person : proposalPersons) {
			if (person.getProposalPersonRole().getCode().equals(Constants.PRINCIPAL_INVESTIGATOR)) {
				piName = person.getFullName();
			}
		}
		return piName;
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
							proposalPreReviewAttachment.setProposalPreReviewComment(reviewComment);
							proposalPreReviewAttachment.setPreReviewId(preReview.getPreReviewId());
							proposalPreReviewAttachment.setProposalId(preReview.getProposalId());
							proposalPreReviewAttachment.setFileName(fileName);
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

	@Override
	public String approveOrDisapprovePreReview(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		String actionType = proposalVO.getActionType();
		ProposalPreReview preReview = proposal.getReviewerReview();
		PreReviewStatus reviewStatus = null;
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add(preReview.getRequestorEmailAddress());
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String preReviewMessage = "";
		String preReviewSubject = "";
		if (actionType.equals("APPROVE")) {
			reviewStatus = proposalPreReviewDao.getPreReviewStatusByCode(Constants.PRE_REVIEW_STATUS_COMPLETE);
			preReview.setReviewStatusCode(Constants.PRE_REVIEW_STATUS_COMPLETE);
			preReview.setPreReviewStatus(reviewStatus);
			preReviewMessage = "The following application has completed a pre review: <br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
					+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
					+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the application.";
			preReviewSubject = "Action Required: Pre Review Completed for "+ proposal.getTitle();
		} else {
			reviewStatus = proposalPreReviewDao.getPreReviewStatusByCode(Constants.PRE_REVIEW_STATUS_REVISION);
			preReview.setReviewStatusCode(Constants.PRE_REVIEW_STATUS_REVISION);
			preReview.setPreReviewStatus(reviewStatus);
			preReviewMessage = "The following application's pre review has been returned: <br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
					+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
					+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the application.";
			preReviewSubject = "Action Required: Pre Review Revision Requested for "+ proposal.getTitle();
		}
		preReview.setCompletionDate(committeeDao.getCurrentTimestamp());
		preReview = proposalPreReviewDao.saveOrUpdatePreReview(preReview);
		// proposal.setReviewerReview(preReview);
		if (proposalVO.getPersonId().equals(preReview.getReviewerPersonId())) {
			proposal.setIsPreReviewer(false);
		}
		proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposal.getProposalId()));		
		fibiEmailService.sendEmail(toAddresses, preReviewSubject, null, null, preReviewMessage, true);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public ResponseEntity<byte[]> downloadPreReviewAttachment(Integer attachmentId) {
		ProposalPreReviewAttachment attachment = proposalPreReviewDao.fetchAttachmentById(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] data = attachment.getAttachment();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
			String filename = attachment.getFileName();
			headers.setContentDispositionFormData(filename, filename);
			headers.setContentLength(data.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentData;
	}

}
