package com.polus.fibicomp.workflow.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;

@Service
public interface WorkflowService {

	/**
	 * This method is used to create a new workflow.
	 * @param moduleItemId - Id of the module(proposal).
	 * @param userName - username of the logged in user.
	 * @param statusCode - status code of the proposal.
	 * @param subject - subject for sending notification mail.
	 * @param message - message for sending notification mail.
	 * @return an object of workflow.
	 */
	public Workflow createWorkflow(Integer moduleItemId, String userName, Integer statusCode, String sponsorTypeCode, String subject, String message);

	/**
	 * This method is used to approve or disapprove a proposal.
	 * @param actionType - action request type.
	 * @param moduleItemId - Id of the module(proposal).
	 * @param personId - Person Id of the logged in user.
	 * @param approverComment - Approver Comment.
	 * @param files - Files need to be attached.
	 * @param approverStopNumber - Stop Number of the approver.
	 * @param subject - subject for sending notification mail.
	 * @param message - message for sending notification mail.
	 * @return an object of workflow detail.
	 * @throws IOException
	 */
	public WorkflowDetail approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, Integer approverStopNumber, String subject, String message) throws IOException;

	/**
	 * This method is used to download Workflow Attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadWorkflowAttachment(Integer attachmentId);

	/**
	 * This method is used to fetch current workflow detail.
	 * @param workflowId - Id of the workflow.
	 * @param personId - Person Id of the logged in user.
	 * @param roleCode - role code of the user.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode);

	/**
	 * This method is used to get mail address based on user type.
	 * @param roleType - specifies the type of user in routing
	 * @return set of email address of routing users.
	 */
	public Set<String> getEmailAdressByUserType(String roleTypeCode);

}
