package com.polus.fibicomp.workflow.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

@Service
public interface WorkflowService {

	/**
	 * @param proposalVO
	 * @return
	 */
	public Workflow createWorkflow(Integer moduleItemId, String userName, Integer statusCode, String subject, String message);

	public WorkflowDetail approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, Integer approverStopNumber, String subject, String message) throws IOException;

	public boolean isFinalApprover(Integer moduleItemId, String personId, Integer approverStopNumber);

	public ResponseEntity<byte[]> downloadWorkflowAttachment(Integer attachmentId);

	public boolean isFirstApprover(Integer moduleItemId, String personId, Integer approverStopNumber);

	public Workflow assignWorkflowReviewers(Integer moduleItemId, WorkflowDetail workflowDetail, String subject, String message);

	public List<WorkflowMapDetail> fetchAvailableReviewers(Integer workflowDetailId);

	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode);

	/**
	 * @param roleType specifies the type of user in routing
	 * @return set of email address of all grant managers
	 */
	public Set<String> getEmailAdressByUserType(String roleTypeCode);

}
