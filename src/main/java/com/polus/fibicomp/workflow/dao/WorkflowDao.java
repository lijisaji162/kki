package com.polus.fibicomp.workflow.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

@Service
public interface WorkflowDao {

	/**
	 * This method is used to save workflow.
	 * @param workflow - workflow object.
	 * @return an object of workflow.
	 */
	public Workflow saveWorkflow(Workflow workflow);

	/**
	 * This method is used to fetch workflow map details.
	 * @return a list of workflow map detail.
	 */
	public List<WorkflowMapDetail> fetchWorkflowMapDetail();

	/**
	 * This method is used to fetch workflow status based on workflow status code.
	 * @param approveStatusCode - workflow status code.
	 * @return an object of workflow status.
	 */
	public WorkflowStatus fetchWorkflowStatusByStatusCode(String approveStatusCode);

	/**
	 * This method is used to fetch active workflow based on module item Id.
	 * @param moduleItemId - Id of the module.
	 * @return an object of workflow.
	 */
	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId);

	/**
	 * This method is used to fetch workflow detail.
	 * @param workflowId - Id of the workflow.
	 * @param personId - Person Id of the logged in user.
	 * @param approverStopNumber - Stop Number of the approver.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail findUniqueWorkflowDetailByCriteria(Integer workflowId, String personId, Integer approverStopNumber);

	/**
	 * This method is used to save workflow detail.
	 * @param workflowDetail - workflow detail object.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail saveWorkflowDetail(WorkflowDetail workflowDetail);

	/**
	 * This method is used to fetch final workflow detail.
	 * @param workflowId - Id of the workflow. 
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail fetchFinalApprover(Integer workflowId);

	/**
	 * This method is used to fetch workflow attachment based on attachment Id.
	 * @param attachmentId - Id of the workflow attachment.
	 * @return an object of workflow attachment.
	 */
	public WorkflowAttachment fetchWorkflowAttachmentById(Integer attachmentId);

	/**
	 * This method is used to fetch workflow details based on stop number.
	 * @param workflowId - Id of the workflow.
	 * @param approvalStopNumber - Stop Number of the approver.
	 * @param approvalStatusCode - status code of workflow detail.
	 * @return a list of workflow detail.
	 */
	public List<WorkflowDetail> fetchWorkflowDetailListByApprovalStopNumber(Integer workflowId, Integer approvalStopNumber, String approvalStatusCode);

	/**
	 * This method is used to fetch the maximum stop number.
	 * @param workflowId - Id of the workflow.
	 * @return the max value of stop number.
	 */
	public Integer getMaxStopNumber(Integer workflowId);

	/**
	 * This method is used to fetch workflow detail based on input params.
	 * @param workflowId - Id of the workflow.
	 * @param personId - Person Id of the logged in user.
	 * @param stopNumber - Stop Number of the approver.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail fetchWorkflowByParams(Integer workflowId, String personId, Integer stopNumber);

	/**
	 * This method is used to fetch workflow detail based on id.
	 * @param workflowId - Id of the workflow.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail fetchWorkflowDetailById(Integer workflowId);

	/**
	 * This method is used to fetch the count of active workflow.
	 * @param moduleItemId - Id of the module.
	 * @return count of workflow.
	 */
	public Long activeWorkflowCountByModuleItemId(Integer moduleItemId);

	/**
	 * This method is used to fetch current workflow detail.
	 * @param workflowId - Id of the workflow.
	 * @param personId - Person Id of the logged in user.
	 * @param roleCode - role code of the user.
	 * @return an object of workflow detail.
	 */
	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode);

	/**
	 * This method is used to fetch workflow details.
	 * @param workflowId - Id of the workflow.
	 * @return a list of workflow details.
	 */
	public List<WorkflowDetail> fetchWorkflowDetailByWorkflowId(Integer workflowId);

	/**
	 * This method is used to get mail address based on user type.
	 * @param roleTypeCode - specifies the type of user in routing
	 * @return set of email address of routing users.
	 */
	public Set<String> fetchEmailAdressByUserType(String roleTypeCode);

	public List<WorkflowMapDetail> fetchWorkflowMapDetailByRoleType(Integer roleTypeCode);

}
