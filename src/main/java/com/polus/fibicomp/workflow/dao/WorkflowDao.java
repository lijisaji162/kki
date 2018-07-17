package com.polus.fibicomp.workflow.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowReviewerDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

@Service
public interface WorkflowDao {

	public Workflow saveWorkflow(Workflow workflow);

	public List<WorkflowMapDetail> fetchWorkflowMapDetail();

	public WorkflowStatus fetchWorkflowStatusByStatusCode(String approveStatusCode);

	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId);

	public WorkflowDetail findUniqueWorkflowDetailByCriteria(Integer workflowId, String personId, Integer approverStopNumber);

	public WorkflowDetail saveWorkflowDetail(WorkflowDetail workflowDetail);

	public WorkflowDetail fetchFinalApprover(Integer workflowId);

	public WorkflowAttachment fetchWorkflowAttachmentById(Integer attachmentId);

	public WorkflowDetail fetchFirstApprover(Integer workflowId);

	public List<WorkflowDetail> fetchWorkflowDetailListByApprovalStopNumber(Integer workflowId, Integer approvalStopNumber, String approvalStatusCode);

	public Integer getMaxStopNumber(Integer workflowId);

	public List<WorkflowMapDetail> fetchWorkflowMapDetailReviewers();

	public WorkflowDetail fetchWorkflowByParams(Integer workflowId, String personId, Integer stopNumber);

	public WorkflowDetail fetchWorkflowDetailById(Integer workflowId);

	public List<WorkflowReviewerDetail> fetchPersonIdByCriteria(Integer workflowDetailId, String approvalStatusCode);

	public Long activeWorkflowCountByModuleItemId(Integer moduleItemId);

	public WorkflowReviewerDetail saveWorkflowReviewDetail(WorkflowReviewerDetail workflowReviewerDetail);

	public List<WorkflowMapDetail> fetchWorkflowMapDetailByNotInPersonId(List<String> personIds);

	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode);

	public List<WorkflowDetail> fetchWorkflowDetailByWorkflowId(Integer workflowId);
 
	public List<WorkflowReviewerDetail> fetchWorkflowReviewerByCriteria(Integer workflowDetailId);

	public Set<String> fetchEmailAdressByUserType(String roleTypeCode);

	public List<WorkflowReviewerDetail> getWorkflowReviewerDetails(Integer workflowDetailId, String approvalStatusCode);

}
