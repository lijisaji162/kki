package com.polus.fibicomp.workflow.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.email.service.FibiEmailService;
import com.polus.fibicomp.workflow.comparator.WorkflowMapDetailComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowReviewerDetail;

@Transactional
@Service(value = "workflowService")
public class WorkflowServiceImpl implements WorkflowService {

	protected static Logger logger = Logger.getLogger(WorkflowServiceImpl.class.getName());

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private FibiEmailService fibiEmailService;

	@Override
	public Workflow createWorkflow(Integer moduleItemId, String userName, Integer statusCode, String subject, String message) {
		// for re submission case
		Set<String> toAddresses = new HashSet<String>();
		Workflow activeWorkflow = null;
		Long workflowCount = 0L;
		boolean isResubmission = false;
		activeWorkflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		if (activeWorkflow != null) {
			activeWorkflow.setIsWorkflowActive(false);
			workflowDao.saveWorkflow(activeWorkflow);
			isResubmission = true;
		}

		workflowCount = workflowDao.activeWorkflowCountByModuleItemId(moduleItemId);
		Workflow workflow = new Workflow();
		workflow.setIsWorkflowActive(true);
		workflow.setModuleCode(1);
		workflow.setModuleItemId(moduleItemId);
		workflow.setCreateTimeStamp(committeeDao.getCurrentTimestamp());
		workflow.setCreateUser(userName);
		workflow.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		workflow.setUpdateUser(userName);
		workflow.setWorkflowSequence((int) (workflowCount + 1));

		List<WorkflowMapDetail> workflowMapDetails = workflowDao.fetchWorkflowMapDetail();
		Collections.sort(workflowMapDetails, new WorkflowMapDetailComparator());
		List<WorkflowDetail> workflowDetails = new ArrayList<WorkflowDetail>();
		for (WorkflowMapDetail workflowMapDetail : workflowMapDetails) {
			if (!workflowMapDetail.getRoleTypeCode().equals(Constants.REVIEWER_ROLE_TYPE_CODE)) {
				WorkflowDetail workflowDetail = new WorkflowDetail();
				if (!isResubmission && workflowMapDetail.getApprovalStopNumber().equals(Constants.WORKFLOW_FIRST_STOP_NUMBER)) {
					workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
					workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
					toAddresses.add(workflowMapDetail.getEmailAddress());
				} else if (isResubmission) {
					WorkflowDetail rejectedWorkflowDetail = workflowDao.fetchWorkflowByParams(activeWorkflow.getWorkflowId(), workflowMapDetail.getApproverPersonId(), Constants.WORKFLOW_FIRST_STOP_NUMBER);
					/*if (statusCode.equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
							&& workflowMapDetail.getApprovalStopNumber().equals(Constants.WORKFLOW_FIRST_STOP_NUMBER)) {
						workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
						workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
						toAddresses.add(workflowMapDetail.getEmailAddress());
					} else */if (statusCode.equals(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED)
							&& workflowMapDetail.getApprovalStopNumber().equals(Constants.WORKFLOW_FIRST_STOP_NUMBER)) {
						/*workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
						workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));*/
						if (rejectedWorkflowDetail.getApprovalStopNumber().equals(Constants.WORKFLOW_FIRST_STOP_NUMBER) && rejectedWorkflowDetail.getApprovalStatusCode().equals("R")) {
							workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
							workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
							toAddresses.add(workflowMapDetail.getEmailAddress());	
						} else {
							workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
							workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));
							if (rejectedWorkflowDetail != null) {
								if(rejectedWorkflowDetail.getApproverPersonId().equals(workflowMapDetail.getApproverPersonId())){
									workflowDetail.setApprovalDate(rejectedWorkflowDetail.getApprovalDate());
									workflowDetail.setApprovalComment(rejectedWorkflowDetail.getApprovalComment());
									List<WorkflowAttachment> files = rejectedWorkflowDetail.getWorkflowAttachments();
									if (files != null && !files.isEmpty()) {
										List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
										for (WorkflowAttachment attachment : files) {
											WorkflowAttachment workflowAttachment = new WorkflowAttachment();
											workflowAttachment.setDescription(attachment.getDescription());
											workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
											workflowAttachment.setUpdateUser(userName);
											workflowAttachment.setAttachment(attachment.getAttachment());
											workflowAttachment.setFileName(attachment.getFileName());
											workflowAttachment.setMimeType(attachment.getMimeType());
											workflowAttachment.setWorkflowDetail(workflowDetail);
											workflowAttachments.add(workflowAttachment);
										}
										workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
									}
								}
								/*workflowDetail.setApprovalDate(rejectedWorkflowDetail.getApprovalDate());
								workflowDetail.setApprovalComment(rejectedWorkflowDetail.getApprovalComment());
								List<WorkflowAttachment> files = rejectedWorkflowDetail.getWorkflowAttachments();
								if (files != null && !files.isEmpty()) {
									List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
									for (WorkflowAttachment attachment : files) {
										WorkflowAttachment workflowAttachment = new WorkflowAttachment();
										workflowAttachment.setDescription(attachment.getDescription());
										workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
										workflowAttachment.setUpdateUser(userName);
										workflowAttachment.setAttachment(attachment.getAttachment());
										workflowAttachment.setFileName(attachment.getFileName());
										workflowAttachment.setMimeType(attachment.getMimeType());
										workflowAttachment.setWorkflowDetail(workflowDetail);
										workflowAttachments.add(workflowAttachment);
									}
									workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
								}*/
							}
						}
						/*if (rejectedWorkflowDetail != null) {
							workflowDetail.setApprovalDate(rejectedWorkflowDetail.getApprovalDate());
							workflowDetail.setApprovalComment(rejectedWorkflowDetail.getApprovalComment());
							List<WorkflowAttachment> files = rejectedWorkflowDetail.getWorkflowAttachments();
							if (files != null && !files.isEmpty()) {
								List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
								for (WorkflowAttachment attachment : files) {
									WorkflowAttachment workflowAttachment = new WorkflowAttachment();
									workflowAttachment.setDescription(attachment.getDescription());
									workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
									workflowAttachment.setUpdateUser(userName);
									workflowAttachment.setAttachment(attachment.getAttachment());
									workflowAttachment.setFileName(attachment.getFileName());
									workflowAttachment.setMimeType(attachment.getMimeType());
									workflowAttachment.setWorkflowDetail(workflowDetail);
									workflowAttachments.add(workflowAttachment);
								}
								workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
							}
						}*/
					} else if (statusCode.equals(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED)) {
						if (rejectedWorkflowDetail.getApprovalStatusCode().equals("A")) {
							workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
							workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
						} else {
							workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
							workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED));
						}
						toAddresses.add(workflowMapDetail.getEmailAddress());
					} else {
						workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
						workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED));
					}
				} else {
					workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
					workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED));
				}
				workflowDetail.setApprovalStopNumber(workflowMapDetail.getApprovalStopNumber());
				workflowDetail.setApproverNumber(workflowMapDetail.getApproverNumber());
				workflowDetail.setApproverPersonId(workflowMapDetail.getApproverPersonId());
				workflowDetail.setMapId(workflowMapDetail.getMapId());
				workflowDetail.setWorkflowMap(workflowMapDetail.getWorkflowMap());
				workflowDetail.setPrimaryApproverFlag(workflowMapDetail.getPrimaryApproverFlag());
				workflowDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				workflowDetail.setUpdateUser(userName);
				workflowDetail.setApproverPersonName(workflowMapDetail.getApproverPersonName());
				workflowDetail.setRoleTypeCode(workflowMapDetail.getRoleTypeCode());
				workflowDetail.setWorkflowRoleType(workflowMapDetail.getWorkflowRoleType());
				workflowDetail.setEmailAddress(workflowMapDetail.getEmailAddress());
				workflowDetail.setWorkflow(workflow);
				workflowDetails.add(workflowDetail);
			}
		}
		fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		workflow.setWorkflowDetails(workflowDetails);
		workflow = workflowDao.saveWorkflow(workflow);
		return workflow;
	}

	@Override
	public WorkflowDetail approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, Integer approverStopNumber, String subject, String message) throws IOException {
		Set<String> toAddresses = new HashSet<String>();
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		WorkflowDetail workflowDetail = workflowDao.findUniqueWorkflowDetailByCriteria(workflow.getWorkflowId(), personId, null);
		if (workflowDetail.getApprovalStopNumber() == Constants.WORKFLOW_FIRST_STOP_NUMBER) {
			workflow.setWorkflowStartDate(new Date(committeeDao.getCurrentDate().getTime()));
			workflow.setWorkflowStartPerson(personId);
		}
		if (files != null) {
			List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
			for (int i = 0; i < files.length; i++) {
				WorkflowAttachment workflowAttachment = new WorkflowAttachment();
				workflowAttachment.setDescription(approverComment);
				workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				workflowAttachment.setUpdateUser(personId);			
				workflowAttachment.setAttachment(files[i].getBytes());
				workflowAttachment.setFileName(files[i].getOriginalFilename());
				workflowAttachment.setMimeType(files[i].getContentType());
				workflowAttachment.setWorkflowDetail(workflowDetail);
				workflowAttachments.add(workflowAttachment);
			}
			workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
		}
		List<WorkflowDetail> workflowDetailLists = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), workflowDetail.getApprovalStopNumber(), Constants.WORKFLOW_STATUS_CODE_WAITING);
		if (actionType.equals("A")) {
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));		
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
				workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));
			}
		} else {
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED));				
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED);
				workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED));
			}
		}
		workflowDetail.setApprovalComment(approverComment);
		workflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
		Integer maxApprovalStopNumber = workflowDao.getMaxStopNumber(workflow.getWorkflowId());
		Integer nextApproveStopNumber = workflowDetail.getApprovalStopNumber() + 1;
		/*if (nextApproveStopNumber==3) {
			nextApproveStopNumber = nextApproveStopNumber + 1;
		}*/
		if(!workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_REJECTED) && nextApproveStopNumber <= maxApprovalStopNumber && nextApproveStopNumber != 3) {
			List<WorkflowDetail> workflowDetailList = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), nextApproveStopNumber, Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
			for(WorkflowDetail newWorkflowDetail : workflowDetailList) {
				newWorkflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
				newWorkflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
				toAddresses.add(newWorkflowDetail.getEmailAddress());
			}
		}
		workflowDetail = workflowDao.saveWorkflowDetail(workflowDetail);
		fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		return workflowDetail;
	}

	@Override
	public boolean isFinalApprover(Integer moduleItemId, String personId, Integer approverStopNumber) {
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		WorkflowDetail currentWorkflowDetail = workflowDao.findUniqueWorkflowDetailByCriteria(workflow.getWorkflowId(), personId, null);
		WorkflowDetail finalWorkflowDetail = workflowDao.fetchFinalApprover(workflow.getWorkflowId());
		if (currentWorkflowDetail.getApprovalStopNumber() == finalWorkflowDetail.getApprovalStopNumber()
				&& currentWorkflowDetail.getApproverNumber() == finalWorkflowDetail.getApproverNumber()) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<byte[]> downloadWorkflowAttachment(Integer attachmentId) {
		WorkflowAttachment attachment = workflowDao.fetchWorkflowAttachmentById(attachmentId);
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

	@Override
	public boolean isFirstApprover(Integer moduleItemId, String personId, Integer approverStopNumber) {
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		WorkflowDetail currentWorkflowDetail = workflowDao.findUniqueWorkflowDetailByCriteria(workflow.getWorkflowId(), personId, approverStopNumber);
		WorkflowDetail finalWorkflowDetail = workflowDao.fetchFirstApprover(workflow.getWorkflowId());
		if (currentWorkflowDetail.getApprovalStopNumber() == finalWorkflowDetail.getApprovalStopNumber()
				&& currentWorkflowDetail.getApproverNumber() == finalWorkflowDetail.getApproverNumber()) {
			return true;
		}
		return false;
	}

	@Override
	public Workflow assignWorkflowReviewers(Integer moduleItemId, WorkflowDetail workflowDetail, String subject, String message) {
		Set<String> toAddresses = new HashSet<String>();
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		if (!workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW));
			List<WorkflowReviewerDetail> reviewerDetails = workflowDetail.getWorkflowReviewerDetails();
			if (reviewerDetails != null && !reviewerDetails.isEmpty()) {
				for (WorkflowReviewerDetail reviewer : reviewerDetails) {
					if (reviewer.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
						toAddresses.add(reviewer.getEmailAddress());
					}
				}
			}
		}
		fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		workflowDetail.setWorkflow(workflow);
		workflowDao.saveWorkflowDetail(workflowDetail);
		workflow = workflowDao.saveWorkflow(workflow);
		return workflow;
	}

	@Override
	public List<WorkflowMapDetail> fetchAvailableReviewers(Integer workflowDetailId) {
		List<String> personIds = fetchReviewersPersonIds(workflowDetailId, Constants.WORKFLOW_STATUS_CODE_WAITING);
		List<WorkflowMapDetail> workflowMapDetails = workflowDao.fetchWorkflowMapDetailByNotInPersonId(personIds);
		return workflowMapDetails;
	}

	public List<String> fetchReviewersPersonIds(Integer workflowDetailId, String workflowStatus) {
		List<WorkflowReviewerDetail> reviewerDetails = workflowDao.fetchPersonIdByCriteria(workflowDetailId, workflowStatus);
		List<String> personIds = new ArrayList<String>();
		if (reviewerDetails != null && !reviewerDetails.isEmpty()) {
			for (WorkflowReviewerDetail detail : reviewerDetails) {
				personIds.add(detail.getReviewerPersonId());
			}
		}
		return personIds;
	}

	@Override
	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode) {
		return workflowDao.getCurrentWorkflowDetail(workflowId, personId, roleCode);
	}

	@Override
	public Set<String> getEmailAdressByUserType(String roleTypeCode) {
		return workflowDao.fetchEmailAdressByUserType(roleTypeCode);
	}

}
