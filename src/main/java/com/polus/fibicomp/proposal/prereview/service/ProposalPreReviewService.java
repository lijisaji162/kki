package com.polus.fibicomp.proposal.prereview.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface ProposalPreReviewService {

	/**
	 * This method is used to create a pre review by PI for a proposal.
	 * @param proposalVO - Object of ProposalVO class.
	 * @return A String of details of pre review.
	 */
	public String createProposalPreReview(ProposalVO proposalVO);

	/**
	 * This method is used to add pre review comment by the reviewer for a proposal.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the and comment attachments.
	 * @return A String of details of proposal data with pre review data.
	 */
	public String addPreReviewComment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to complete pre review by the reviewer for a proposal.
	 * @param proposalVO - Object of ProposalVO class.
	 * @return A String of details of proposal data with pre review data.
	 */
	public String completePreReview(ProposalVO proposalVO);

	/**
	 * This method is used to download proposal pre review attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadPreReviewAttachment(Integer attachmentId);

}
