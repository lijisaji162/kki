package com.polus.fibicomp.proposal.service;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.ip.service.InstitutionalProposalService;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalBudget;
import com.polus.fibicomp.proposal.pojo.ProposalIrbProtocol;
import com.polus.fibicomp.proposal.pojo.ProposalKeyword;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalResearchArea;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.workflow.comparator.WorkflowDetailComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowReviewerDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;
import com.polus.fibicomp.workflow.service.WorkflowService;

@Transactional
@Configuration
@Service(value = "proposalService")
public class ProposalServiceImpl implements ProposalService {

	protected static Logger logger = Logger.getLogger(ProposalServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private GrantCallDao grantCallDao;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private InstitutionalProposalService institutionalProposalService;

	@Autowired
	private FibiEmailService fibiEmailService;

	@Value("${application.context.name}")
	private String context;

	@Override
	public String createProposal(ProposalVO proposalVO) {
		Integer grantCallId = proposalVO.getGrantCallId();
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
		proposal.setHomeUnitNumber(Constants.DEFAULT_HOME_UNIT_NUMBER);
		proposal.setHomeUnitName(Constants.DEFAULT_HOME_UNIT_NAME);
		if (grantCallId != null) {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(grantCallId);
			proposal.setGrantCall(grantCall);
			proposal.setGrantCallId(grantCallId);
			proposal.setGrantCallType(grantCallDao.fetchGrantCallTypeByGrantTypeCode(grantCall.getGrantTypeCode()));
			proposal.setGrantTypeCode(grantCall.getGrantTypeCode());
		} else {
			proposal.setGrantCallType(grantCallDao.fetchGrantCallTypeByGrantTypeCode(Constants.GRANT_CALL_TYPE_OTHERS));
			proposal.setGrantTypeCode(Constants.GRANT_CALL_TYPE_OTHERS);
		}
		loadInitialData(proposalVO);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String addProposalAttachment(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			ProposalAttachment newAttachment = proposalVO.getNewAttachment();
			List<ProposalAttachment> proposalAttachments = new ArrayList<ProposalAttachment>();
			for (int i = 0; i < files.length; i++) {
				ProposalAttachment proposalAttachment = new ProposalAttachment();
				proposalAttachment.setAttachmentType(newAttachment.getAttachmentType());
				proposalAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
				proposalAttachment.setDescription(newAttachment.getDescription());
				proposalAttachment.setUpdateTimeStamp(newAttachment.getUpdateTimeStamp());
				proposalAttachment.setUpdateUser(newAttachment.getUpdateUser());
				proposalAttachment.setAttachment(files[i].getBytes());
				proposalAttachment.setFileName(files[i].getOriginalFilename());
				proposalAttachment.setMimeType(files[i].getContentType());
				proposalAttachments.add(proposalAttachment);
			}
			proposal.getProposalAttachments().addAll(proposalAttachments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String saveOrUpdateProposal(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		vo.setStatus(true);
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			vo.setMessage("Proposal saved successfully");
		} else {
			vo.setMessage("Proposal updated successfully");
		}
		vo.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String loadProposalById(Integer proposalId, String personId) {
		ProposalVO proposalVO = new ProposalVO();
		proposalVO.setPersonId(personId);
		Proposal proposal = proposalDao.fetchProposalById(proposalId);
		proposalVO.setProposal(proposal);
		int statusCode = proposal.getStatusCode();
		if (statusCode == Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS || statusCode == Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED) {
			loadInitialData(proposalVO);
		}

		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVED)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_AWARDED)) {
			canTakeRoutingAction(proposalVO);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			WorkflowDetail finalWorkflowDetail = workflowDao.fetchFinalApprover(workflow.getWorkflowId());
			if (finalWorkflowDetail.getApproverPersonId().equals(personId) && !proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
				proposalVO.setFinalApprover(true);
			} else {
				proposalVO.setFinalApprover(false);
			}
			proposalVO.setWorkflow(workflow);
		}

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String fetchCostElementByBudgetCategory(ProposalVO vo) {
		vo.setProposalCostElements(proposalDao.fetchCostElementByBudgetCategory(vo.getBudgetCategoryCode()));
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String fetchAllAreaOfExcellence(ProposalVO vo) {
		vo.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String deleteProposalKeyword(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalKeyword> list = proposal.getProposalKeywords();
			List<ProposalKeyword> updatedlist = new ArrayList<ProposalKeyword>(list);
			Collections.copy(updatedlist, list);
			for (ProposalKeyword proposalKeyword : list) {
				if (proposalKeyword.getKeywordId().equals(vo.getKeywordId())) {
					updatedlist.remove(proposalKeyword);
				}
			}
			proposal.getProposalKeywords().clear();
			proposal.getProposalKeywords().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal keyword deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal keyword");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalResearchArea(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalResearchArea> list = proposal.getProposalResearchAreas();
			List<ProposalResearchArea> updatedlist = new ArrayList<ProposalResearchArea>(list);
			Collections.copy(updatedlist, list);
			for (ProposalResearchArea proposalResearchArea : list) {
				if (proposalResearchArea.getResearchAreaId().equals(vo.getResearchAreaId())) {
					updatedlist.remove(proposalResearchArea);
				}
			}
			proposal.getProposalResearchAreas().clear();
			proposal.getProposalResearchAreas().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal research area deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal research area");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalPerson(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalPerson> list = proposal.getProposalPersons();
			List<ProposalPerson> updatedlist = new ArrayList<ProposalPerson>(list);
			Collections.copy(updatedlist, list);
			for (ProposalPerson proposalPerson : list) {
				if (proposalPerson.getProposalPersonId().equals(vo.getProposalPersonId())) {
					updatedlist.remove(proposalPerson);
				}
			}
			proposal.getProposalPersons().clear();
			proposal.getProposalPersons().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal person deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal person");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalSponsor(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalSponsor> list = proposal.getProposalSponsors();
			List<ProposalSponsor> updatedlist = new ArrayList<ProposalSponsor>(list);
			Collections.copy(updatedlist, list);
			for (ProposalSponsor proposalSponsor : list) {
				if (proposalSponsor.getSponsorId().equals(vo.getSponsorId())) {
					updatedlist.remove(proposalSponsor);
				}
			}
			proposal.getProposalSponsors().clear();
			proposal.getProposalSponsors().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal sponsor deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal proposal");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteIrbProtocol(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalIrbProtocol> list = proposal.getProposalIrbProtocols();
			List<ProposalIrbProtocol> updatedlist = new ArrayList<ProposalIrbProtocol>(list);
			Collections.copy(updatedlist, list);
			for (ProposalIrbProtocol proposalIrbProtocol : list) {
				if (proposalIrbProtocol.getIrbProtocolId().equals(vo.getIrbProtocolId())) {
					updatedlist.remove(proposalIrbProtocol);
				}
			}
			proposal.getProposalIrbProtocols().clear();
			proposal.getProposalIrbProtocols().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal protocol deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal protocol");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalBudget(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalBudget> list = proposal.getProposalBudgets();
			List<ProposalBudget> updatedlist = new ArrayList<ProposalBudget>(list);
			Collections.copy(updatedlist, list);
			for (ProposalBudget proposalBudget : list) {
				if (proposalBudget.getBudgetId().equals(vo.getBudgetId())) {
					updatedlist.remove(proposalBudget);
				}
			}
			proposal.getProposalBudgets().clear();
			proposal.getProposalBudgets().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal budget deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal budget");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalAttachment(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalAttachment> list = proposal.getProposalAttachments();
			List<ProposalAttachment> updatedlist = new ArrayList<ProposalAttachment>(list);
			Collections.copy(updatedlist, list);
			for (ProposalAttachment proposalAttachment : list) {
				if (proposalAttachment.getAttachmentId().equals(vo.getAttachmentId())) {
					updatedlist.remove(proposalAttachment);
				}
			}
			proposal.getProposalAttachments().clear();
			proposal.getProposalAttachments().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal attachment deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal attachment");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public ResponseEntity<byte[]> downloadProposalAttachment(Integer attachmentId) {
		ProposalAttachment attachment = proposalDao.fetchAttachmentById(attachmentId);
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
	public String submitProposal(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String message = "The following application has routed for approval:<br/><br/>Application Title: "+ proposal.getTitle() +"<br/>"
				+ "Principal Investigator: "+ piName +"<br/>Sponsor Due Date: "+ proposal.getSubmissionDate() +"<br/><br/>Please go to "
				+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/createProposal?proposalId="+ proposal.getProposalId() +"\">this link</a> "
				+ "to review the application and provide your response by clicking on the Approve or Reject buttons. "
				+ "Please direct any questions to the application's Principal Investigator (PI) "
				+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
				+ "Application Details as follows:<br/>Application Number: "+ proposal.getProposalId() +"<br/>"
				+ "Application Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
				+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
				+ "Deadline Date: "+ proposal.getSubmissionDate() +"";//Sponsor: {SPONSOR_CODE} - {SPONSOR_NAME}<br/>
		String subject = "Action Required: Approval for "+ proposal.getTitle();

		Workflow workflow = workflowService.createWorkflow(proposal.getProposalId(), proposalVO.getUserName(), proposalVO.getProposalStatusCode(), subject, message);
		canTakeRoutingAction(proposalVO);
		proposalVO.setWorkflow(workflow);
		proposalVO.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	/**
	 * @param proposalVO
	 */
	public void canTakeRoutingAction(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
		List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
		Collections.sort(workflowDetails, new WorkflowDetailComparator());
		boolean currentPerson = true;
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED)) {
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (currentPerson == true) {
					if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
						if (!proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)
								&& !workflowDetail.getApprovalStatusCode()
										.equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
							if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_APPROVED)) {
								proposalVO.setIsApproved(true);
							} else {
								proposalVO.setIsApproved(false);
							}
							proposalVO.setIsApprover(true);
						}
						if (workflowDetail.getRoleTypeCode() == Constants.ADMIN_ROLE_TYPE_CODE) {
							proposalVO.setIsGrantAdmin(true);
						}
					} else if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)
							&& !proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
						currentPerson = false;
					}
				}
			}
		}
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (currentPerson == true) {
					/*if (workflowDetail.getApprovalStatusCode()
							.equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
						List<WorkflowReviewerDetail> reviewerDetails = workflowDetail.getWorkflowReviewerDetails();
						if (reviewerDetails != null && !reviewerDetails.isEmpty()) {
							for (WorkflowReviewerDetail reviewerDetail : reviewerDetails) {
								if (reviewerDetail.getReviewerPersonId().equals(proposalVO.getPersonId())) {
									currentPerson = false;
									proposalVO.setIsReviewer(true);
									proposalVO.setIsReviewed(false);
								}
								if (reviewerDetail.getApprovalStatusCode()
										.equals(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED)) {
									proposalVO.setIsReviewed(true);
								}
							}
						}
					} else*/ if (workflowDetail.getApprovalStatusCode()
							.equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
						if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
							if (workflowDetail.getRoleTypeCode() == Constants.ADMIN_ROLE_TYPE_CODE) {
								proposalVO.setIsGrantAdmin(true);
							}
						} else {
							List<WorkflowReviewerDetail> reviewerDetails = workflowDetail.getWorkflowReviewerDetails();
							if (reviewerDetails != null && !reviewerDetails.isEmpty()) {
								for (WorkflowReviewerDetail reviewerDetail : reviewerDetails) {
									if (reviewerDetail.getReviewerPersonId().equals(proposalVO.getPersonId())) {
										currentPerson = false;
										proposalVO.setIsReviewer(true);
										proposalVO.setIsReviewed(false);
									}
									if (reviewerDetail.getApprovalStatusCode()
											.equals(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED)) {
										proposalVO.setIsReviewed(true);
									}
								}
							}
						}
						/*if (workflowDetail.getRoleTypeCode() == Constants.ADMIN_ROLE_TYPE_CODE) {
							proposalVO.setIsGrantAdmin(true);
						}*/
					}
				}
			}
		}
	}

	public void canTakeRoutingAction1(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS) || proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			boolean currentPerson = true;
			List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
			Collections.sort(workflowDetails, new WorkflowDetailComparator());
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (currentPerson == true) {
					if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId()) && (workflowDetail.getApprovalStatusCode().equals("W") || workflowDetail.getApprovalStatusCode().equals("WR"))) {
						if (!proposal.getStatusCode().equals(8) && !workflowDetail.getApprovalStatusCode().equals("WR")) {
							proposalVO.setIsApproved(false);
							proposalVO.setIsApprover(true);
						}
						proposalVO.setApproverStopNumber(workflowDetail.getApprovalStopNumber());
						currentPerson = false;
						if (workflowDetail.getRoleTypeCode() == 3) {
							proposalVO.setIsReviewer(true);
							proposalVO.setIsReviewed(false);
						}
						if (workflowDetail.getRoleTypeCode() == 2) {
							proposalVO.setIsGrantAdmin(true);
						}
					} else if (workflowDetail.getApprovalStatusCode().equals("W") && !proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
						currentPerson = false;
					}
				}
				if (workflowDetail.getApprovalStatusCode().equals("RC")) {
					proposalVO.setIsReviewed(true);
				}
			}
		}
	}

	@Override
	public String approveOrRejectProposal(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			String actionType = proposalVO.getActionType();
			String approverComment = proposalVO.getApproveComment();
			Integer approvalStopNumber = proposalVO.getApproverStopNumber();

			logger.info("actionType : " + actionType);
			logger.info("personId : " + proposalVO.getPersonId());
			logger.info("approverComment : " + approverComment);
			logger.info("approvalStopNumber : " + approvalStopNumber);

			String piName = getPrincipalInvestigator(proposal.getProposalPersons());
			String message = "The following application has routed for approval:<br/><br/>Application Title: "+ proposal.getTitle() +"<br/>"
					+ "Principal Investigator: "+ piName +"<br/>Sponsor Due Date: "+ proposal.getSubmissionDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/createProposal?proposalId="+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the application and provide your response by clicking on the Approve or Reject buttons. "
					+ "Please direct any questions to the application's Principal Investigator (PI) "
					+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
					+ "Application Details as follows:<br/>Application Number: "+ proposal.getProposalId() +"<br/>"
					+ "Application Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSubmissionDate() +"";//Sponsor: {SPONSOR_CODE} - {SPONSOR_NAME}<br/>
			String subject = "Action Required: Review for "+ proposal.getTitle();

			WorkflowDetail workflowDetail = workflowService.approveOrRejectWorkflowDetail(actionType, proposal.getProposalId(), proposalVO.getPersonId(), approverComment, files, approvalStopNumber, subject, message);
			/*boolean isFirstApprover = workflowService.isFirstApprover(proposal.getProposalId(), proposalVO.getPersonId());
			if (isFirstApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			}*/
			boolean isFinalApprover = workflowService.isFinalApprover(proposal.getProposalId(), proposalVO.getPersonId(), approvalStopNumber);
			if (isFinalApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVED);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVED));
				proposal = proposalDao.saveOrUpdateProposal(proposal);

				subject = "Action Required: Submit '" + proposal.getTitle() + "' for endorsement";
				Set<String> toAddresses = new HashSet<String>();
				toAddresses = workflowService.getEmailAdressByUserType(Constants.SMU_GRANT_MANAGER_CODE);
				message = "The following application has been approved by grant administrator and waiting to be submitted for endorsement:"
						+ "<br/><br/>Application Title: "+ proposal.getTitle() +"<br/>"
						+ "Principal Investigator: "+ piName +"<br/>Sponsor Due Date: "+ proposal.getSubmissionDate() +"<br/><br/>Please go to "
						+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/createProposal?proposalId="+ proposal.getProposalId() +"\">this link</a> "
						+ "to review the application and forward to provost for endorsement by clicking on Submit to Provost button. "
						+ "Please direct any questions to the application's Principal Investigator (PI) "
						+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
						+ "Application Details as follows:<br/>Application Number: "+ proposal.getProposalId() +"<br/>"
						+ "Application Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
						+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
						+ "Deadline Date: "+ proposal.getSubmissionDate() +"";
				if(!toAddresses.isEmpty()) {
					fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
				}
			} else if (!isFinalApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			} else if (actionType.equals("R")) {
				if(workflowDetail.getRoleTypeCode() == 2) {
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED));
					proposal = proposalDao.saveOrUpdateProposal(proposal);
				} else {
					/*proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));*/
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED));
					proposal = proposalDao.saveOrUpdateProposal(proposal);
				}
			}
			if (proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS || proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED) {
				loadInitialData(proposalVO);
			}
			proposalVO.setIsApproved(true);
			proposalVO.setIsApprover(true);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			proposalVO.setWorkflow(workflow);
			proposalVO.setProposal(proposal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String assignReviewer(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		if (!proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
			proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS);
			proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS));
			proposal = proposalDao.saveOrUpdateProposal(proposal);
		}

		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String message = "The following application has assigned for review:<br/><br/>Application Title: "+ proposal.getTitle() +"<br/>"
				+ "Principal Investigator: "+ piName +"<br/>Sponsor Due Date: "+ proposal.getSubmissionDate() +"<br/><br/>Please go to "
				+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/createProposal?proposalId="+ proposal.getProposalId() +"\">this link</a> "
				+ "to review the application and provide your response by clicking on the Complete button. "
				+ "Please direct any questions to the application's Administrator "
				+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
				+ "Application Details as follows:<br/>Application Number: "+ proposal.getProposalId() +"<br/>"
				+ "Application Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
				+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
				+ "Deadline Date: "+ proposal.getSubmissionDate() +"";//Sponsor: {SPONSOR_CODE} - {SPONSOR_NAME}<br/>
		String subject = "Action Required: Review for "+ proposal.getTitle();

		Workflow workflow = workflowService.assignWorkflowReviewers(proposalVO.getProposalId(), proposalVO.getLoggedInWorkflowDetail(), subject, message);
		proposalVO.setWorkflow(workflow);
		proposalVO.setProposal(proposal);
		proposalVO.setIsGrantAdmin(true);
		proposalVO.setIsReviewed(false);
		proposalVO.setIsApprover(false);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	public String reviewCompleted1(MultipartFile[] files, String formDataJSON) {
		boolean reviewComplete = true;
		ProposalVO proposalVO = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);		
			Proposal proposal = proposalVO.getProposal();
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			List<WorkflowDetail> workflowDetails = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), null, Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW);
			for(WorkflowDetail workflowDetail : workflowDetails) {
				if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
					workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED);
					workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED));
					workflowDetail.setApprovalComment(proposalVO.getApproveComment());
					workflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
					if (files != null) {
						List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
						for (int i = 0; i < files.length; i++) {
							WorkflowAttachment workflowAttachment = new WorkflowAttachment();
							workflowAttachment.setDescription(proposalVO.getApproveComment());
							workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
							workflowAttachment.setUpdateUser(proposalVO.getPersonId());			
							workflowAttachment.setAttachment(files[i].getBytes());
							workflowAttachment.setFileName(files[i].getOriginalFilename());
							workflowAttachment.setMimeType(files[i].getContentType());
							workflowAttachment.setWorkflowDetail(workflowDetail);
							workflowAttachments.add(workflowAttachment);
						}
						workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
					}
					workflowDao.saveWorkflowDetail(workflowDetail);
				}
				if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
					reviewComplete = false;
				}
			}
			if (reviewComplete) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			}
			List<WorkflowDetail> details = workflow.getWorkflowDetails();
			Integer workflowDetailId = null;
			for (WorkflowDetail detail : details) {
				if (detail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
					workflowDetailId = detail.getWorkflowDetailId();
				}
			}
			WorkflowDetail adminWorkflow = workflowDao.fetchWorkflowDetailById(workflowDetailId);
			if (adminWorkflow != null) {
				adminWorkflow.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
				adminWorkflow.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
				workflowDao.saveWorkflowDetail(adminWorkflow);
			}
			proposalVO.setIsReviewed(true);
			proposalVO.setIsReviewer(true);
			proposalVO.setWorkflow(workflow);
			proposalVO.setProposal(proposal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String reviewCompleted(MultipartFile[] files, String formDataJSON) {
		boolean reviewComplete = true;
		ProposalVO proposalVO = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);		
			Proposal proposal = proposalVO.getProposal();
			String piName = getPrincipalInvestigator(proposal.getProposalPersons());
			String message = "The following application has routed for approval:<br/><br/>Application Title: "+ proposal.getTitle() +"<br/>"
					+ "Principal Investigator: "+ piName +"<br/>Sponsor Due Date: "+ proposal.getSubmissionDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/createProposal?proposalId="+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the application and provide your response by clicking on the Approve or Reject buttons. "
					+ "Please direct any questions to the application's Principal Investigator (PI) "
					+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
					+ "Application Details as follows:<br/>Application Number: "+ proposal.getProposalId() +"<br/>"
					+ "Application Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSubmissionDate() +"";//Sponsor: {SPONSOR_CODE} - {SPONSOR_NAME}<br/>
			String subject = "Action Required: Approval for "+ proposal.getTitle();

			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			List<WorkflowDetail> workflowDetails = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), null, Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW);
			for(WorkflowDetail workflowDetail : workflowDetails) {
				List<WorkflowReviewerDetail> reviewerDetails = workflowDetail.getWorkflowReviewerDetails();
				if (reviewerDetails != null && !reviewerDetails.isEmpty()) {
					for (WorkflowReviewerDetail reviewerDetail : reviewerDetails) {
						if (reviewerDetail.getReviewerPersonId().equals(proposalVO.getPersonId())) {
							reviewerDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED);
							reviewerDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED));
							reviewerDetail.setComments(proposalVO.getApproveComment());
							reviewerDetail.setReviewDate(new Date(committeeDao.getCurrentDate().getTime()));
							if (files != null) {
								List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
								for (int i = 0; i < files.length; i++) {
									WorkflowAttachment workflowAttachment = new WorkflowAttachment();
									workflowAttachment.setDescription(proposalVO.getApproveComment());
									workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
									workflowAttachment.setUpdateUser(proposalVO.getPersonId());
									workflowAttachment.setAttachment(files[i].getBytes());
									workflowAttachment.setFileName(files[i].getOriginalFilename());
									workflowAttachment.setMimeType(files[i].getContentType());
									workflowAttachment.setWorkflowReviewerDetail(reviewerDetail);
									workflowAttachments.add(workflowAttachment);
								}
								reviewerDetail.getWorkflowAttachments().addAll(workflowAttachments);
							}
							workflowDao.saveWorkflowReviewDetail(reviewerDetail);
							List<WorkflowReviewerDetail> remainingReviewers = workflowDao.getWorkflowReviewerDetails(workflowDetail.getWorkflowDetailId(), Constants.WORKFLOW_STATUS_CODE_WAITING);
							for (WorkflowReviewerDetail remainingReviewer : remainingReviewers) {
								if (!remainingReviewer.getReviewerPersonId().equals(proposalVO.getPersonId())) {
									remainingReviewer.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED);
									remainingReviewer.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED));
									workflowDao.saveWorkflowReviewDetail(reviewerDetail);
								}
							}
							if (reviewerDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
								reviewComplete = false;
							}
						}
						/*if (reviewerDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
							reviewComplete = false;
						}*/
					}
				}
			}

			if (reviewComplete) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);

				List<WorkflowDetail> details = workflow.getWorkflowDetails();
				Integer workflowDetailId = null;
				for (WorkflowDetail detail : details) {
					if (detail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
						workflowDetailId = detail.getWorkflowDetailId();
					}
				}
				WorkflowDetail adminWorkflow = workflowDao.fetchWorkflowDetailById(workflowDetailId);
				if (adminWorkflow != null) {
					adminWorkflow.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
					adminWorkflow.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
					workflowDao.saveWorkflowDetail(adminWorkflow);
					Set<String> toAddresses = new HashSet<String>();
					toAddresses.add(adminWorkflow.getEmailAddress());
					fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
				}
			}
			proposalVO.setIsReviewed(true);
			proposalVO.setIsReviewer(true);
			proposalVO.setWorkflow(workflow);
			proposalVO.setProposal(proposal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	public void loadInitialData(ProposalVO proposalVO) {
		proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
		proposalVO.setProposalCategories(proposalDao.fetchAllCategories());
		proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
		proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
		proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
		proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
		proposalVO.setProtocols(proposalDao.fetchAllProtocols());
		proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
		proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
		proposalVO.setProposalBudgetCategories(proposalDao.fetchAllBudgetCategories());
		proposalVO.setProposalInstituteCentreLabs(proposalDao.fetchAllInstituteCentrelabs());
		proposalVO.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
		proposalVO.setSponsorTypes(grantCallDao.fetchAllSponsorTypes());
		proposalVO.setProposalTypes(proposalDao.fetchAllProposalTypes());
		proposalVO.setDefaultGrantCallType(grantCallDao.fetchGrantCallTypeByGrantTypeCode(Constants.GRANT_CALL_TYPE_OTHERS));
		proposalVO.setHomeUnits(committeeDao.fetchAllHomeUnits());
	}

	@Override
	public String fetchReviewers(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
		List<WorkflowDetail> details = workflow.getWorkflowDetails();
		Integer workflowDetailId = null;
		for (WorkflowDetail detail : details) {
			if (detail.getApproverPersonId().equals(proposalVO.getPersonId())) {
				workflowDetailId = detail.getWorkflowDetailId();
			}
		}
		List<WorkflowMapDetail> availableReviewers = workflowService.fetchAvailableReviewers(workflowDetailId);
		WorkflowDetail workflowDetail = workflowDao.getCurrentWorkflowDetail(workflow.getWorkflowId(), proposalVO.getPersonId(), Constants.ADMIN_ROLE_TYPE_CODE);
		proposalVO.setAvailableReviewers(availableReviewers);
		proposalVO.setLoggedInWorkflowDetail(workflowDetail);
		Map<String, WorkflowStatus> workflowStatusMap = new HashMap<String, WorkflowStatus>();
		workflowStatusMap.put(Constants.WORKFLOW_STATUS_CODE_WAITING, workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
		workflowStatusMap.put(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW, workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW));
		proposalVO.setWorkflowStatusMap(workflowStatusMap);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String submitForEndorsement(ProposalVO proposalVO) {
		Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT));
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		proposalVO.setProposal(proposal);
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String subject = "Action Required: Endorse " + proposal.getTitle();
		Set<String> toAddresses = new HashSet<String>();
		toAddresses = workflowService.getEmailAdressByUserType(Constants.SMU_GRANT_PROVOST_CODE);
		String message = "The following application has been routed for endorsement: <br/><br/>Application Title: "
				+ proposal.getTitle() + "<br/>" + "Principal Investigator: " + piName + "<br/>Sponsor Due Date: "
				+ proposal.getSubmissionDate() + "<br/><br/>Please go to " + "<a title=\"\" target=\"_self\" href=\""
				+ context + "/proposal/createProposal?proposalId=" + proposal.getProposalId() + "\">this link</a> "
				+ "to review the application and provide your response with endorse by clicking on Endorse button. "
				+ "Please direct any questions to the application's Principal Investigator (PI) "
				+ "or the contact associated with a given application.<br/><br/>Thank you.<br/><br/>"
				+ "Application Details as follows:<br/>Application Number: " + proposal.getProposalId() + "<br/>"
				+ "Application Title: " + proposal.getTitle() + "<br/>Principal Investigator: " + piName + "<br/>"
				+ "Lead Unit: " + proposal.getHomeUnitNumber() + " - " + proposal.getHomeUnitName() + "<br/>"
				+ "Deadline Date: " + proposal.getSubmissionDate() + "";
		if (!toAddresses.isEmpty()) {
			fibiEmailService.sendEmail(toAddresses, subject, null, null, message, true);
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String approveProvost(ProposalVO proposalVO) {
		Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
		String ipNumber = institutionalProposalService.generateInstitutionalProposalNumber();
		logger.info("ipNumber : " + ipNumber);
		boolean isIPCreated = institutionalProposalService.createInstitutionalProposal(proposal.getProposalId(), ipNumber, proposalVO.getUserName());
		logger.info("isIPCreated : " + isIPCreated);
		if (isIPCreated) {
			proposal.setIpNumber(ipNumber);
			proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_AWARDED);
			proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_AWARDED));
			proposal = proposalDao.saveOrUpdateProposal(proposal);
		}
		proposalVO.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String deleteReviewer(ProposalVO proposalVO) {
		try {
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposalVO.getProposalId());
			List<WorkflowDetail> workflowdetailList = workflowDao.fetchWorkflowDetailByWorkflowId(workflow.getWorkflowId());
			for(WorkflowDetail workflowDetail : workflowdetailList) {
				List<WorkflowReviewerDetail> list = workflowDao.fetchWorkflowReviewerByCriteria(workflowDetail.getWorkflowDetailId());
				List<WorkflowReviewerDetail> updatedlist = new ArrayList<WorkflowReviewerDetail>(list);
				Collections.copy(updatedlist, list);
				for (WorkflowReviewerDetail workflowReviewerDetail : list) {
					if (workflowReviewerDetail.getReviewerPersonId().equals(proposalVO.getReviewerId())) {
						updatedlist.remove(workflowReviewerDetail);
					}
				}
				workflowDetail.getWorkflowReviewerDetails().clear();
				workflowDetail.getWorkflowReviewerDetails().addAll(updatedlist);
				if (workflowDetail.getWorkflowReviewerDetails() == null || workflowDetail.getWorkflowReviewerDetails().isEmpty()) {
					proposalVO.setIsApproved(false);
					proposalVO.setIsApprover(true);
					proposalVO.setIsGrantAdmin(true);
					Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
					proposal = proposalDao.saveOrUpdateProposal(proposal);
					workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
					workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
				}
				List<WorkflowMapDetail> availableReviewers = workflowService.fetchAvailableReviewers(workflowDetail.getWorkflowDetailId());
				proposalVO.setAvailableReviewers(availableReviewers);
				workflowDao.saveWorkflowDetail(workflowDetail);
			}
			workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposalVO.getProposalId());
			proposalVO.setWorkflow(workflow);
			proposalVO.setStatus(true);
			proposalVO.setMessage("Reviewer deleted successfully");
		} catch (Exception e) {
			proposalVO.setStatus(true);
			proposalVO.setMessage("Problem occurred in deleting reviewer");
			e.printStackTrace();
		}
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
}
