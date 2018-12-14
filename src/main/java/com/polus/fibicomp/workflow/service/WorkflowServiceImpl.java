package com.polus.fibicomp.workflow.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.polus.fibicomp.view.PersonDetailsView;
import com.polus.fibicomp.workflow.comparator.WorkflowMapDetailComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

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
	public Workflow createWorkflow(Integer moduleItemId, String userName, Integer statusCode, String sponsorTypeCode, String subject, String message) {
		// for re submission case
		Set<String> toAddresses = new HashSet<String>();
		Workflow activeWorkflow = null;
		Long workflowCount = 0L;
		activeWorkflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		if (activeWorkflow != null) {
			activeWorkflow.setIsWorkflowActive(false);
			workflowDao.saveWorkflow(activeWorkflow);
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
	
		List<WorkflowMapDetail> workflowMapDetails = new ArrayList<WorkflowMapDetail>();
		if(sponsorTypeCode.equals(Constants.FEDERAL_ROLE_TYPE_CODE)) {
			workflowMapDetails = workflowDao.fetchWorkflowMapDetailByRoleType(Integer.parseInt(Constants.FEDERAL_ROLE_TYPE_CODE));
		} else {
			workflowMapDetails = workflowDao.fetchWorkflowMapDetail();
		}
		Collections.sort(workflowMapDetails, new WorkflowMapDetailComparator());
		List<WorkflowDetail> workflowDetails = new ArrayList<WorkflowDetail>();
		for (WorkflowMapDetail workflowMapDetail : workflowMapDetails) {
			WorkflowDetail workflowDetail = new WorkflowDetail();
			if (workflowMapDetail.getApprovalStopNumber().equals(Constants.WORKFLOW_FIRST_STOP_NUMBER)) {
				workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
				workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
				toAddresses.add(workflowMapDetail.getEmailAddress());
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
		fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		workflow.setWorkflowDetails(workflowDetails);
		workflow = workflowDao.saveWorkflow(workflow);
		return workflow;
	}

	public WorkflowDetail approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, boolean isSuperUser, String approverComment, MultipartFile[] files, Integer approverStopNumber, String subject, String message) throws IOException {
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(moduleItemId);
		WorkflowDetail workflowDetail = workflowDao.findUniqueWorkflowDetailByCriteria(workflow.getWorkflowId(), personId, isSuperUser, null);
		if (workflowDetail == null) {
			if (isSuperUser) {
				workflowDetail = superUserApproveOrReject(actionType, moduleItemId, personId, approverComment, files, subject, message, workflow);
			}
		} else {
			if(isSuperUser) {
				if(workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
					workflowDetail = approveOrReject(actionType, moduleItemId, personId, approverComment, files, subject, message, workflow, workflowDetail);					
				} else {
					workflowDetail = superUserApproveOrReject(actionType, moduleItemId, personId, approverComment, files, subject, message, workflow);									
				}
			} else {
				workflowDetail = approveOrReject(actionType, moduleItemId, personId, approverComment, files, subject, message, workflow, workflowDetail);				
			}
		}
		return workflowDetail;
	}

	public WorkflowDetail superUserApproveOrReject(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, String subject, String message, Workflow workflow) throws IOException {
		Set<String> toAddresses = new HashSet<String>();
		Integer stopNumber = workflowDao.getWaitingForApprovalStopNumber(Constants.WORKFLOW_STATUS_CODE_WAITING, workflow.getWorkflowId());
		WorkflowDetail superUserWorkflowDetail = new WorkflowDetail();
		PersonDetailsView personDetail = workflowDao.getPersonDetail(personId);
		superUserWorkflowDetail.setApprovalComment(approverComment);
		superUserWorkflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
		superUserWorkflowDetail.setApprovalStopNumber(stopNumber);
		superUserWorkflowDetail.setApproverNumber(1);
		superUserWorkflowDetail.setApproverPersonId(personId);
		superUserWorkflowDetail.setApproverPersonName(personDetail.getFullName());
		superUserWorkflowDetail.setPrimaryApproverFlag(true);
		superUserWorkflowDetail.setMapId(1);
		superUserWorkflowDetail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		superUserWorkflowDetail.setUpdateUser(personDetail.getPrncplName());
		superUserWorkflowDetail.setRoleTypeCode(0);
		superUserWorkflowDetail.setEmailAddress(personDetail.getEmailAddress());
		superUserWorkflowDetail.setWorkflow(workflow);
		if (files != null) {
			superUserWorkflowDetail = addWorkflowAttachments(personId, approverComment, files, superUserWorkflowDetail);
		}
		List<WorkflowDetail> workflowDetailLists = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), stopNumber, Constants.WORKFLOW_STATUS_CODE_WAITING);
		if (actionType.equals("A")) {
			superUserWorkflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
			superUserWorkflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));		
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				if(workflowDetailList.getApprovalStatusCode().equalsIgnoreCase(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
					workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVAL_BYPASSED);
					workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVAL_BYPASSED));
				}
			}
		} else {
			superUserWorkflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED);
			superUserWorkflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED));				
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				if(workflowDetailList.getApprovalStatusCode().equalsIgnoreCase(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
					workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED_BY_ADMIN);
					workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED_BY_ADMIN));
				}
			}
		}
		superUserWorkflowDetail.setApprovalComment(approverComment);
		superUserWorkflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
		Integer maxApprovalStopNumber = workflowDao.getMaxStopNumber(workflow.getWorkflowId());
		Integer nextApproveStopNumber = superUserWorkflowDetail.getApprovalStopNumber() + 1;
		if(!superUserWorkflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_REJECTED) && nextApproveStopNumber <= maxApprovalStopNumber) {
			List<WorkflowDetail> workflowDetailList = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), nextApproveStopNumber, Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
			if (workflowDetailList == null || workflowDetailList.isEmpty()) {
				nextApproveStopNumber = nextApproveStopNumber + 1;
				workflowDetailList = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), nextApproveStopNumber, Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
			}
			for(WorkflowDetail newWorkflowDetail : workflowDetailList) {
				newWorkflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
				newWorkflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
				toAddresses.add(newWorkflowDetail.getEmailAddress());
			}
		}
		workflow.getWorkflowDetails().add(superUserWorkflowDetail);
		superUserWorkflowDetail = workflowDao.saveWorkflowDetail(superUserWorkflowDetail);
		fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		return superUserWorkflowDetail;
	}
	
	public WorkflowDetail approveOrReject(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, String subject, String message, Workflow workflow, WorkflowDetail workflowDetail) throws IOException {
		Set<String> toAddresses = new HashSet<String>();
		if (workflowDetail.getApprovalStopNumber() == Constants.WORKFLOW_FIRST_STOP_NUMBER) {
			workflow.setWorkflowStartDate(new Date(committeeDao.getCurrentDate().getTime()));
			workflow.setWorkflowStartPerson(personId);
		}
		if (files != null) {
			workflowDetail = addWorkflowAttachments(personId, approverComment, files, workflowDetail);
		}
		List<WorkflowDetail> workflowDetailLists = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), workflowDetail.getApprovalStopNumber(), Constants.WORKFLOW_STATUS_CODE_WAITING);
		if (actionType.equals("A")) {
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED));		
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				if(workflowDetailList.getApprovalStatusCode().equalsIgnoreCase(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
					workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED_BY_OTHER);
					workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_APPROVED_BY_OTHER));
				}
			}
		} else {
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED));				
			for(WorkflowDetail workflowDetailList : workflowDetailLists) {
				if(workflowDetailList.getApprovalStatusCode().equalsIgnoreCase(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
					workflowDetailList.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED_BY_OTHER);
					workflowDetailList.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REJECTED_BY_OTHER));
				}
			}
		}
		workflowDetail.setApprovalComment(approverComment);
		workflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
		Integer maxApprovalStopNumber = workflowDao.getMaxStopNumber(workflow.getWorkflowId());
		Integer nextApproveStopNumber = workflowDetail.getApprovalStopNumber() + 1;
		if(!workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_REJECTED) && nextApproveStopNumber <= maxApprovalStopNumber) {
			List<WorkflowDetail> workflowDetailList = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), nextApproveStopNumber, Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
			if (workflowDetailList == null || workflowDetailList.isEmpty()) {
				nextApproveStopNumber = nextApproveStopNumber + 1;
				workflowDetailList = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), nextApproveStopNumber, Constants.WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED);
			}
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

	public WorkflowDetail addWorkflowAttachments(String personId, String approverComment, MultipartFile[] files, WorkflowDetail workflowDetail) throws IOException {
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
		return workflowDetail;	
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
	public WorkflowDetail getCurrentWorkflowDetail(Integer workflowId, String personId, Integer roleCode) {
		return workflowDao.getCurrentWorkflowDetail(workflowId, personId, roleCode);
	}

	@Override
	public Set<String> getEmailAdressByUserType(String roleTypeCode) {
		return workflowDao.fetchEmailAdressByUserType(roleTypeCode);
	}

	@Override
	public void prepareWorkflowDetails(Workflow workflow) {
		Map<Integer, List<WorkflowDetail>> workflowDetailMap = new HashMap<Integer, List<WorkflowDetail>>();
		List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
		if (workflowDetails != null && !workflowDetails.isEmpty()) {
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (workflowDetailMap.get(workflowDetail.getApprovalStopNumber()) != null) {
					workflowDetailMap.get(workflowDetail.getApprovalStopNumber()).add(workflowDetail);
				} else {
					List<WorkflowDetail> details = new ArrayList<>();
					details.add(workflowDetail);
					workflowDetailMap.put(workflowDetail.getApprovalStopNumber(), details);
				}
			}
		}
		workflow.setWorkflowDetailMap(workflowDetailMap);
	}

	@Override
	public void prepareWorkflowDetailsList(List<Workflow> workflowList) {
		for (Workflow workflow : workflowList) {
			Map<Integer, List<WorkflowDetail>> workflowDetailMap = new HashMap<Integer, List<WorkflowDetail>>();
			List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
			if (workflowDetails != null && !workflowDetails.isEmpty()) {
				for (WorkflowDetail workflowDetail : workflowDetails) {
					if (workflowDetailMap.get(workflowDetail.getApprovalStopNumber()) != null) {
						workflowDetailMap.get(workflowDetail.getApprovalStopNumber()).add(workflowDetail);
					} else {
						List<WorkflowDetail> details = new ArrayList<>();
						details.add(workflowDetail);
						workflowDetailMap.put(workflowDetail.getApprovalStopNumber(), details);
					}
				}
			}
			workflow.setWorkflowDetailMap(workflowDetailMap);
		}
	}

}
