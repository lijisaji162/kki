package com.polus.fibicomp.proposal.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import com.itextpdf.text.DocumentException;
import com.polus.fibicomp.budget.common.pojo.ValidCeRateType;
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetDetailCalcAmount;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.budget.service.BudgetService;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.common.dao.CommonDao;
import com.polus.fibicomp.compilance.dao.ComplianceDao;
import com.polus.fibicomp.compilance.pojo.ProposalSpecialReview;
import com.polus.fibicomp.compilance.pojo.SpecialReviewType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewUsage;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.email.service.FibiEmailService;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.ip.service.InstitutionalProposalService;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalIrbProtocol;
import com.polus.fibicomp.proposal.pojo.ProposalKeyword;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalResearchArea;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;
import com.polus.fibicomp.util.GenerateBudgetPdfReport;
import com.polus.fibicomp.util.GeneratePdfReport;
import com.polus.fibicomp.vo.SponsorSearchResult;
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
	@Qualifier(value = "roleDao")
	private RoleDao roleDao;

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

	@Autowired
	private BudgetService budgetService;

	@Value("${application.context.name}")
	private String context;

	@Autowired
	private BudgetDao budgetDao;

	@Autowired
	private ComplianceDao complianceDao;

	@Autowired
	public CommonDao commonDao;

	@Override
	public String createProposal(ProposalVO proposalVO) {
		Integer grantCallId = proposalVO.getGrantCallId();
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
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

		getHomeUnits(proposalVO);

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
		if (proposal.getBudgetHeader() != null) {
			proposal = budgetService.saveOrUpdateProposalBudget(vo);
		}
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
		} else {
			Boolean isDeclarationSectionRequired = commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
					Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.IS_REQUIRED_DECLARATION_SECTION);
			proposalVO.setIsDeclarationSectionRequired(isDeclarationSectionRequired);
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

		getHomeUnits(proposalVO);

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String fetchCostElementByBudgetCategory(ProposalVO vo) {
		// vo.setProposalCostElements(proposalDao.fetchCostElementByBudgetCategory(vo.getBudgetCategoryCode()));
		vo.setCostElements(budgetDao.fetchCostElementByBudgetCategory(vo.getBudgetCategoryCode()));
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
		//String sponsorDueDate = proposal.getSubmissionDate() != null ? proposal.getSubmissionDate().toString() : "";
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
			//Proposal proposal = proposalVO.getProposal();
			Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
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
			//Proposal proposal = proposalVO.getProposal();
			Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
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
		Boolean isDeclarationSectionRequired = commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
				Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.IS_REQUIRED_DECLARATION_SECTION);
		proposalVO.setIsDeclarationSectionRequired(isDeclarationSectionRequired);
		Proposal proposal = proposalVO.getProposal();
		proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
		proposalVO.setActivityTypes(proposalDao.fetchAllActivityTypes());
		proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
		proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
		proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
		if (isDeclarationSectionRequired) {
			proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
		}
		proposalVO.setProtocols(proposalDao.fetchAllProtocols());
		proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
		proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
		proposalVO.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
		proposalVO.setSponsorTypes(grantCallDao.fetchAllSponsorTypes());
		proposalVO.setProposalTypes(proposalDao.fetchAllProposalTypes());
		proposalVO.setDefaultGrantCallType(grantCallDao.fetchGrantCallTypeByGrantTypeCode(Constants.GRANT_CALL_TYPE_OTHERS));
		if (proposal.getBudgetHeader() != null) {
			proposalVO.setCostElements(budgetDao.getAllCostElements());
			proposalVO.setSysGeneratedCostElements(budgetService.fetchSysGeneratedCostElements(proposalVO.getProposal().getActivityTypeCode()));
			Set<String> rateClassTypes = new HashSet<>();
			List<FibiProposalRate> proposalRates = proposal.getBudgetHeader().getProposalRates();
			if (proposalRates != null && !proposalRates.isEmpty()) {
				for (FibiProposalRate proposalRate : proposalRates) {
					rateClassTypes.add(proposalRate.getRateClass().getDescription());
					proposalVO.setRateClassTypes(rateClassTypes);
				}
			}
			proposalVO.setBudgetCategories(budgetDao.fetchAllBudgetCategory());
			proposalVO.setTbnPersons(budgetDao.fetchAllTbnPerson());
		}
		// proposalVO.setSponsors(proposalDao.fetchAllSponsors());
		proposalVO.setReviewTypes(getSpecialReviewTypes());
		List<String> approvalTypeCodes = new ArrayList<>();
		approvalTypeCodes.add("5");
		approvalTypeCodes.add("6");
		proposalVO.setSpecialReviewApprovalTypes(complianceDao.fetchSpecialReviewApprovalTypeNotInCodes(approvalTypeCodes));
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

	@Override
	public ByteArrayInputStream generateProposalPdf(Integer proposalId) throws DocumentException {
		Proposal proposalData = proposalDao.fetchProposalById(proposalId);
		ByteArrayInputStream bis = GeneratePdfReport.proposalPdfReport(proposalData);
		return bis;
	}

	@Override
	public ByteArrayInputStream generateBudgetPdf(Integer proposalId) throws DocumentException {
		Proposal budgetData = proposalDao.fetchProposalById(proposalId);
		ByteArrayInputStream bis = GenerateBudgetPdfReport.proposalPdfReport(budgetData);
		return bis;
	}

	public void getHomeUnits(ProposalVO proposalVO) {
		List<RoleMemberBo> memberBos = roleDao.fetchCreateProposalPersonRole(proposalVO.getPersonId(), "10013");
		if (memberBos != null && !memberBos.isEmpty()) {
			Set<String> unitNumbers = new HashSet<>();
			for (RoleMemberBo memberBo : memberBos) {
				List<RoleMemberAttributeDataBo> attributeDataBos = memberBo.getAttributeDetails();
				if (attributeDataBos != null && !attributeDataBos.isEmpty()) {
					for (RoleMemberAttributeDataBo bo : attributeDataBos) {
						unitNumbers.add(bo.getAttributeValue());
					}
				}
			}

			logger.info("create proposal unitNumbers : " + unitNumbers);
			if (!unitNumbers.isEmpty()) {
				proposalVO.setHomeUnits(proposalDao.fetchLeadUnitsByUnitNumbers(unitNumbers));
			}
		}
	}

	public List<SpecialReviewType> getSpecialReviewTypes() {
		List<SpecialReviewType> specialReviewTypes = complianceDao.fetchAllSpecialReviewType();
		List<SpecialReviewUsage> specialReviewUsages = complianceDao.fetchSpecialReviewUsageByModuleCode("3");
		List<SpecialReviewType> reviewTypes = new ArrayList<>();
		for (SpecialReviewType specialReviewType : specialReviewTypes) {
			SpecialReviewUsage itemSpecialReviewUsage = null;
			for (SpecialReviewUsage specialReviewUsage : specialReviewUsages) {
				if (StringUtils.equals(specialReviewUsage.getSpecialReviewTypeCode(),
						String.valueOf(specialReviewType.getSpecialReviewTypeCode()))) {
					itemSpecialReviewUsage = specialReviewUsage;
					break;
				}
			}
			if (itemSpecialReviewUsage != null && itemSpecialReviewUsage.isActive()) {
				if (itemSpecialReviewUsage.isGlobal()) {// || canViewNonGlobalSpecialReviewTypes
					reviewTypes.add(specialReviewType);
				}
			}
		}
		return reviewTypes;
	}

	@Override
	public String deleteProposalSpecialReview(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalSpecialReview> list = proposal.getPropSpecialReviews();
			List<ProposalSpecialReview> updatedlist = new ArrayList<ProposalSpecialReview>(list);
			Collections.copy(updatedlist, list);
			for (ProposalSpecialReview proposalSpecialReview : list) {
				if (proposalSpecialReview.getId().equals(vo.getProposalSpecialReviewId())) {
					proposalSpecialReview = proposalDao.deleteProposalSpecialReview(proposalSpecialReview);
					updatedlist.remove(proposalSpecialReview);
				}
			}
			proposal.getPropSpecialReviews().clear();
			proposal.getPropSpecialReviews().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal special review deleted successfully");
		} catch (Exception e) {
			vo.setStatus(false);
			vo.setMessage("Problem occurred in deleting proposal special review");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public List<SponsorSearchResult> findSponsor(String searchString) {
		return proposalDao.findSponsor(searchString);
	}

	@Override
	public String copyProposal(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		Proposal copyProposal = new Proposal();
		copyProposal.setTitle(proposal.getTitle());
		copyProposal.setActivityTypeCode(proposal.getActivityTypeCode());
		copyProposal.setActivityType(proposal.getActivityType());
		copyProposal.setTypeCode(proposal.getTypeCode());
		copyProposal.setProposalType(proposal.getProposalType());
		copyProposal.setHomeUnitNumber(proposal.getHomeUnitNumber());
		copyProposal.setHomeUnitName(proposal.getHomeUnitName());
		copyProposal.setSponsorCode(proposal.getSponsorCode());
		copyProposal.setSponsorName(proposal.getSponsorName());
		copyProposal.setStartDate(proposal.getStartDate());
		copyProposal.setEndDate(proposal.getEndDate());
		copyProposal.setProposalPersons(copyProposalPersons(copyProposal, proposal));
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);

		copyProposal.setGrantCallId(proposal.getGrantCallId());
		copyProposal.setGrantCall(proposal.getGrantCall());
		copyProposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		copyProposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
		copyProposal.setSubmissionDate(proposal.getSubmissionDate());
		copyProposal.setIsSmu(proposal.getIsSmu());
		copyProposal.setAbstractDescription(proposal.getAbstractDescription());
		copyProposal.setFundingStrategy(proposal.getFundingStrategy());
		copyProposal.setDetails(proposal.getDetails());
		copyProposal.setDeliverables(proposal.getDeliverables());
		copyProposal.setResearchDescription(proposal.getResearchDescription());
		copyProposal.setCreateUser(proposal.getCreateUser());
		copyProposal.setUpdateUser(proposal.getUpdateUser());
		copyProposal.setIpNumber(proposal.getIpNumber());
		copyProposal.setGrantTypeCode(proposal.getGrantTypeCode());
		copyProposal.setGrantCallType(proposal.getGrantCallType());
		copyProposal.setSubmitUser(proposal.getSubmitUser());
		copyProposal.setSponsorProposalNumber(proposal.getSponsorProposalNumber());
		copyProposal.setPrincipalInvestigator(proposal.getPrincipalInvestigator());
		copyProposal.setApplicationActivityType(proposal.getApplicationActivityType());
		copyProposal.setApplicationType(proposal.getApplicationType());
		copyProposal.setApplicationStatus(proposal.getApplicationStatus());
		copyProposal.setCreateTimeStamp(vo.getProposal().getCreateTimeStamp());
		copyProposal.setUpdateTimeStamp(vo.getProposal().getUpdateTimeStamp());
		copyProposal.setProposalAttachments(copyProposalAttachments(copyProposal, proposal));
		copyProposal.setPropSpecialReviews(copyProposalSpecialReview(copyProposal, proposal));
		copyProposal.setProposalKeywords(copyProposalKeywords(copyProposal, proposal));
		copyProposal.setProposalIrbProtocols(copyProposalIrbProtocols(copyProposal, proposal));
		copyProposal.setProposalResearchAreas(copyProposalResearchAreas(copyProposal, proposal));
		copyProposal.setProposalSponsors(copyProposalSponsors(copyProposal, proposal));
		if(proposal.getBudgetHeader() != null) {
			BudgetHeader budgetHeader = copyProposalBudgetHeader(copyProposal, proposal, vo);
			copyProposal.setBudgetHeader(budgetHeader);
			copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
			if (copyProposal.getBudgetHeader().getIsAutoCalc() != null && !copyProposal.getBudgetHeader().getIsAutoCalc()) {
				copyProposal = budgetService.calculateCost(copyProposal);			
			}
			vo.setProposal(copyProposal);
			copyProposal = budgetService.saveOrUpdateProposalBudget(vo);
		}
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		vo.setStatus(true);
		vo.setMessage("Proposal copied successfully");
		vo.setProposal(copyProposal);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	private List<ProposalPerson> copyProposalPersons(Proposal copyProposal, Proposal proposal) {
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		List<ProposalPerson> proposalPersons = proposal.getProposalPersons();
		List<ProposalPerson> copiedProposalPersons = new ArrayList<>(proposalPersons);
		Collections.copy(copiedProposalPersons, proposalPersons);
		List<ProposalPerson> newProposalPersons = new ArrayList<>();
		for (ProposalPerson copiedPersonDetail : copiedProposalPersons) {
			ProposalPerson personDetail = new ProposalPerson();
			personDetail.setProposal(copyProposal);
			personDetail.setPersonId(copiedPersonDetail.getPersonId());
			personDetail.setRolodexId(copiedPersonDetail.getRolodexId());
			personDetail.setFullName(copiedPersonDetail.getFullName());
			personDetail.setPersonRoleId(copiedPersonDetail.getPersonRoleId());
			personDetail.setProposalPersonRole(copiedPersonDetail.getProposalPersonRole());
			personDetail.setLeadUnitNumber(copiedPersonDetail.getLeadUnitNumber());
			personDetail.setLeadUnitName(copiedPersonDetail.getLeadUnitName());
			personDetail.setDepartment(copiedPersonDetail.getDepartment());
			personDetail.setUpdateUser(copiedPersonDetail.getUpdateUser());
			personDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newProposalPersons.add(personDetail);
		}
		return newProposalPersons;
	}

	public List<ProposalAttachment> copyProposalAttachments(Proposal copyProposal, Proposal proposal) {
		List<ProposalAttachment> proposalAttachments = proposal.getProposalAttachments();
		List<ProposalAttachment> copiedProposalAttachments = new ArrayList<>(proposalAttachments);
		Collections.copy(copiedProposalAttachments, proposalAttachments);
		List<ProposalAttachment> newAttachments = new ArrayList<>();
		for (ProposalAttachment copiedAttachmentDetail : copiedProposalAttachments) {
			ProposalAttachment attachmentDetail = new ProposalAttachment();
			attachmentDetail.setProposal(copyProposal);
			attachmentDetail.setAttachment(copiedAttachmentDetail.getAttachment());
			attachmentDetail.setAttachmentTypeCode(copiedAttachmentDetail.getAttachmentTypeCode());
			attachmentDetail.setAttachmentType(copiedAttachmentDetail.getAttachmentType());
			attachmentDetail.setDescription(copiedAttachmentDetail.getDescription());
			attachmentDetail.setFileName(copiedAttachmentDetail.getFileName());
			attachmentDetail.setMimeType(copiedAttachmentDetail.getMimeType());
			attachmentDetail.setUpdateUser(copiedAttachmentDetail.getUpdateUser());
			attachmentDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newAttachments.add(attachmentDetail);
		}
		return newAttachments;
	}

	private BudgetHeader copyProposalBudgetHeader(Proposal copyProposal, Proposal proposal, ProposalVO vo) {
		BudgetHeader budget = proposal.getBudgetHeader();
		BudgetHeader copyBudget = new BudgetHeader();
		copyBudget.setStartDate(budget.getStartDate());
		copyBudget.setEndDate(budget.getEndDate());
		copyBudget.setCreateTimeStamp(budget.getCreateTimeStamp());
		copyBudget.setCreateUser(budget.getCreateUser());
		copyBudget.setCreateUserName(budget.getCreateUserName());
		copyBudget.setUpdateTimeStamp(budget.getUpdateTimeStamp());
		copyBudget.setUpdateUser(budget.getUpdateUser());
		copyBudget.setUpdateUserName(budget.getUpdateUserName());
		copyBudget.setRateType(budget.getRateType());
		copyBudget.setRateClassCode(budget.getRateClassCode());
		copyBudget.setRateTypeCode(budget.getRateTypeCode());
		copyBudget.setIsAutoCalc(budget.getIsAutoCalc());
		copyBudget = budgetDao.saveBudgetHeader(copyBudget);
		copyProposal.setBudgetHeader(copyBudget);
		copyProposal = proposalDao.saveOrUpdateProposal(copyProposal);
		List<FibiProposalRate> fibiProposalRates = budget.getProposalRates();
		if (fibiProposalRates == null || fibiProposalRates.isEmpty()) {
			Set<String> rateClassTypes = new HashSet<>();
			fibiProposalRates = budgetService.fetchFilteredProposalRates(copyProposal, rateClassTypes);
			budget.setProposalRates(fibiProposalRates);
			vo.setRateClassTypes(rateClassTypes);
		}
		copyBudget.getBudgetPeriods().addAll(copyBudgetPeriods(copyBudget, budget, proposal.getActivityTypeCode()));
		return copyBudget;
	}

	private List<BudgetPeriod> copyBudgetPeriods(BudgetHeader copyBudget, BudgetHeader budget, String activityTypeCode) {
		List<BudgetPeriod> budgetPeriods = budget.getBudgetPeriods();
		List<BudgetPeriod> copiedBudgetPeriods = new ArrayList<>(budgetPeriods);
		Collections.copy(copiedBudgetPeriods, budgetPeriods);
		List<BudgetPeriod> newPeriods = new ArrayList<>();
		for (BudgetPeriod period : copiedBudgetPeriods) {
				BudgetPeriod copyPeriod = new BudgetPeriod();
				copyPeriod.setModuleItemCode(period.getModuleItemCode());
				copyPeriod.setModuleItemKey(period.getModuleItemKey());
				copyPeriod.setVersionNumber(period.getVersionNumber());
				copyPeriod.setBudgetPeriod(period.getBudgetPeriod());
				copyPeriod.setStartDate(period.getStartDate());
				copyPeriod.setEndDate(period.getEndDate());
				copyPeriod.setTotalCost(period.getTotalCost());
				copyPeriod.setTotalDirectCost(period.getTotalDirectCost());
				copyPeriod.setTotalIndirectCost(period.getTotalIndirectCost());
				copyPeriod.setPeriodLabel(period.getPeriodLabel());
				copyPeriod.setIsObligatedPeriod(period.getIsObligatedPeriod());
				copyPeriod.setBudget(copyBudget);
				//periodDetail = budgetDao.saveBudgetPeriod(periodDetail);
				copyBudgetDetails(copyPeriod, period, activityTypeCode);
				copyPeriod.setUpdateTimeStamp(budget.getUpdateTimeStamp());
				copyPeriod.setUpdateUser(period.getUpdateUser());
				copyPeriod = budgetDao.saveBudgetPeriod(copyPeriod);
				newPeriods.add(copyPeriod);
			}
			return newPeriods;
	}

	private void copyBudgetDetails(BudgetPeriod copyPeriod, BudgetPeriod period, String activityTypeCode) {	
		List<BudgetDetail> budgetDetails = period.getBudgetDetails();
		if (budgetDetails != null && !budgetDetails.isEmpty()) {
			List<BudgetDetail> copiedBudgetDetails = new ArrayList<>(budgetDetails);
			Collections.copy(copiedBudgetDetails, budgetDetails);
			List<BudgetDetail> newLineItems = new ArrayList<>();
			for (BudgetDetail budgetDetail : copiedBudgetDetails) {
				BudgetDetail copyBudgetDetail = new BudgetDetail();
				copyBudgetDetail.setBudgetCategory(budgetDetail.getBudgetCategory());
				copyBudgetDetail.setBudgetCategoryCode(budgetDetail.getBudgetCategoryCode());
				copyBudgetDetail.setBudgetJustification(budgetDetail.getBudgetJustification());
				copyBudgetDetail.setBudgetPeriod(budgetDetail.getBudgetPeriod());
				copyBudgetDetail.setEndDate(budgetDetail.getEndDate());
				copyBudgetDetail.setIsSystemGeneratedCostElement(budgetDetail.getIsSystemGeneratedCostElement());
				copyBudgetDetail.setSystemGeneratedCEType(budgetDetail.getSystemGeneratedCEType());
				// apply inflation here
				CostElement costElement = budgetDetail.getCostElement();
				costElement = budgetDao.fetchCostElementsById(costElement.getCostElement());
				copyBudgetDetail.setCostElement(costElement);
				copyBudgetDetail.setCostElementCode(budgetDetail.getCostElementCode());
				BigDecimal lineItemCost = budgetDetail.getLineItemCost();
				BigDecimal updatedLineItemCost = BigDecimal.ZERO;
				List<ValidCeRateType> ceRateTypes = costElement.getValidCeRateTypes();
				BudgetDetailCalcAmount budgetCalculatedAmount = null;
				if (ceRateTypes != null && !ceRateTypes.isEmpty()) {
					for (ValidCeRateType ceRateType : ceRateTypes) {
						FibiProposalRate applicableRate = budgetDao.fetchApplicableProposalRate(copyPeriod.getBudget().getBudgetId(), copyPeriod.getStartDate(),
								ceRateType.getRateClassCode(), ceRateType.getRateTypeCode(), activityTypeCode);
						if (applicableRate != null
								&& (applicableRate.getRateClass().getRateClassTypeCode().equals("I") && "7".equals(applicableRate.getRateClassCode()))) {
							BigDecimal validRate = BigDecimal.ZERO;
							validRate = validRate.add(applicableRate.getApplicableRate());
							if (validRate.compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal hundred = new BigDecimal(100);
								BigDecimal percentageFactor = validRate.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
								BigDecimal calculatedCost = ((lineItemCost.multiply(percentageFactor)));
								updatedLineItemCost = updatedLineItemCost.add(calculatedCost);
								budgetCalculatedAmount = budgetService.getNewBudgetCalculatedAmount(copyPeriod, budgetDetail, applicableRate);
								budgetCalculatedAmount.setCalculatedCost(calculatedCost);
								copyBudgetDetail.getBudgetDetailCalcAmounts().add(budgetCalculatedAmount);
							}
						}
					}
				}
				if (updatedLineItemCost.compareTo(BigDecimal.ZERO) > 0) {
					lineItemCost = lineItemCost.add(updatedLineItemCost);
					copyBudgetDetail.setLineItemCost(lineItemCost);
				} else {
					copyBudgetDetail.setLineItemCost(lineItemCost);
				}
				copyBudgetDetail.setLineItemDescription(budgetDetail.getLineItemDescription());
				copyBudgetDetail.setLineItemNumber(budgetDetail.getLineItemNumber());
				copyBudgetDetail.setOnOffCampusFlag(budgetDetail.getOnOffCampusFlag());
				copyBudgetDetail.setPeriod(copyPeriod);
				copyBudgetDetail.setPrevLineItemCost(budgetDetail.getPrevLineItemCost());
				copyBudgetDetail.setStartDate(budgetDetail.getStartDate());
				copyBudgetDetail.setUpdateTimeStamp(budgetDetail.getUpdateTimeStamp());
				copyBudgetDetail.setUpdateUser(budgetDetail.getUpdateUser());
				copyBudgetDetail.setFullName(budgetDetail.getFullName());
				copyBudgetDetail.setRolodexId(budgetDetail.getRolodexId());
				copyBudgetDetail.setPersonId(budgetDetail.getPersonId());
				copyBudgetDetail.setTbnId(budgetDetail.getTbnId());
				copyBudgetDetail.setTbnPerson(budgetDetail.getTbnPerson());
				copyBudgetDetail.setPersonType(budgetDetail.getPersonType());
				//copyBudgetDetail = budgetDao.saveBudgetDetail(copyBudgetDetail);
				newLineItems.add(copyBudgetDetail);
			}
			copyPeriod.getBudgetDetails().addAll(newLineItems);
		}
	}

	private List<ProposalKeyword> copyProposalKeywords(Proposal copyProposal, Proposal proposal) {
		List<ProposalKeyword> proposalKeywords = proposal.getProposalKeywords();
		List<ProposalKeyword> copiedProposalKeywords = new ArrayList<>(proposalKeywords);
		Collections.copy(copiedProposalKeywords, proposalKeywords);
		List<ProposalKeyword> newKeywords = new ArrayList<>();
		for (ProposalKeyword copiedKeywordDetail : copiedProposalKeywords) {
			ProposalKeyword keywordtDetail = new ProposalKeyword();
			keywordtDetail.setProposal(copyProposal);
			keywordtDetail.setScienceKeywordCode(copiedKeywordDetail.getScienceKeywordCode());
			keywordtDetail.setScienceKeyword(copiedKeywordDetail.getScienceKeyword());
			keywordtDetail.setUpdateUser(copiedKeywordDetail.getUpdateUser());
			keywordtDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newKeywords.add(keywordtDetail);
		}
		return newKeywords;
	}

	private List<ProposalIrbProtocol> copyProposalIrbProtocols(Proposal copyProposal, Proposal proposal) {
		List<ProposalIrbProtocol> proposalIrbProtocols = proposal.getProposalIrbProtocols();
		List<ProposalIrbProtocol> copiedProposalIrbProtocols = new ArrayList<>(proposalIrbProtocols);
		Collections.copy(copiedProposalIrbProtocols, proposalIrbProtocols);
		List<ProposalIrbProtocol> newIrbProtocols = new ArrayList<>();
		for (ProposalIrbProtocol copiedIrbProtocolDetail : copiedProposalIrbProtocols) {
			ProposalIrbProtocol irbProtocolDetail = new ProposalIrbProtocol();
			irbProtocolDetail.setProposal(copyProposal);
			irbProtocolDetail.setProtocolId(copiedIrbProtocolDetail.getProtocolId());
			irbProtocolDetail.setProtocol(copiedIrbProtocolDetail.getProtocol());
			irbProtocolDetail.setUpdateUser(copiedIrbProtocolDetail.getUpdateUser());
			irbProtocolDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newIrbProtocols.add(irbProtocolDetail);
		}
		return newIrbProtocols;
	}

	private List<ProposalResearchArea> copyProposalResearchAreas(Proposal copyProposal, Proposal proposal) {
		List<ProposalResearchArea> proposalResearchAreas = proposal.getProposalResearchAreas();
		List<ProposalResearchArea> copiedProposalResearchAreas = new ArrayList<>(proposalResearchAreas);
		Collections.copy(copiedProposalResearchAreas, proposalResearchAreas);
		List<ProposalResearchArea> newproposalResearchAreas = new ArrayList<>();
		for (ProposalResearchArea copiedResearchAreaDetail : copiedProposalResearchAreas) {
			ProposalResearchArea researchAreaDetail = new ProposalResearchArea();
			researchAreaDetail.setProposal(copyProposal);
			researchAreaDetail.setResearchAreaCode(copiedResearchAreaDetail.getResearchAreaCode());
			researchAreaDetail.setResearchArea(copiedResearchAreaDetail.getResearchArea());
			researchAreaDetail.setResearchTypeCode(copiedResearchAreaDetail.getResearchTypeCode());
			researchAreaDetail.setProposalResearchType(copiedResearchAreaDetail.getProposalResearchType());
			researchAreaDetail.setExcellenceAreaCode(copiedResearchAreaDetail.getExcellenceAreaCode());
			researchAreaDetail.setProposalExcellenceArea(copiedResearchAreaDetail.getProposalExcellenceArea());
			researchAreaDetail.setUpdateUser(copiedResearchAreaDetail.getUpdateUser());
			researchAreaDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newproposalResearchAreas.add(researchAreaDetail);
		}
		return newproposalResearchAreas;
	}

	private List<ProposalSponsor> copyProposalSponsors(Proposal copyProposal, Proposal proposal) {
		List<ProposalSponsor> proposalSponsors = proposal.getProposalSponsors();
		List<ProposalSponsor> copiedProposalSponsors = new ArrayList<>(proposalSponsors);
		Collections.copy(copiedProposalSponsors, proposalSponsors);
		List<ProposalSponsor> newProposalSponsors = new ArrayList<>();
		for (ProposalSponsor copiedProposalSponsorsDetail : copiedProposalSponsors) {
			ProposalSponsor sponsorsDetail = new ProposalSponsor();
			sponsorsDetail.setProposal(copyProposal);
			sponsorsDetail.setSponsorCode(copiedProposalSponsorsDetail.getSponsorCode());
			sponsorsDetail.setSponsor(copiedProposalSponsorsDetail.getSponsor());
			sponsorsDetail.setStartDate(copiedProposalSponsorsDetail.getStartDate());
			sponsorsDetail.setEndDate(copiedProposalSponsorsDetail.getEndDate());
			sponsorsDetail.setAmount(copiedProposalSponsorsDetail.getAmount());
			sponsorsDetail.setUpdateUser(copiedProposalSponsorsDetail.getUpdateUser());
			sponsorsDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newProposalSponsors.add(sponsorsDetail);
		}
		return newProposalSponsors;
	}

	private List<ProposalSpecialReview> copyProposalSpecialReview(Proposal copyProposal, Proposal proposal) {
		List<ProposalSpecialReview> proposalSpecialReviews = proposal.getPropSpecialReviews();
		List<ProposalSpecialReview> copiedProposalSpecialReviews = new ArrayList<>(proposalSpecialReviews);
		Collections.copy(copiedProposalSpecialReviews, proposalSpecialReviews);
		List<ProposalSpecialReview> newSpecialReviews = new ArrayList<>();
		for (ProposalSpecialReview copiedSpecialReviewDetail : copiedProposalSpecialReviews) {
			ProposalSpecialReview specialReviewDetail = new ProposalSpecialReview();
			specialReviewDetail.setProposal(copyProposal);
			specialReviewDetail.setSpecialReviewTypeCode(copiedSpecialReviewDetail.getSpecialReviewTypeCode());
			specialReviewDetail.setSpecialReviewType(copiedSpecialReviewDetail.getSpecialReviewType());
			specialReviewDetail.setApprovalTypeCode(copiedSpecialReviewDetail.getApprovalTypeCode());
			specialReviewDetail.setApprovalType(copiedSpecialReviewDetail.getApprovalType());
			specialReviewDetail.setProtocolNumber(copiedSpecialReviewDetail.getProtocolNumber());
			specialReviewDetail.setProtocolStatus(copiedSpecialReviewDetail.getProtocolStatus());
			specialReviewDetail.setApplicationDate(copiedSpecialReviewDetail.getApplicationDate());
			specialReviewDetail.setApprovalDate(copiedSpecialReviewDetail.getApprovalDate());
			specialReviewDetail.setExpirationDate(copiedSpecialReviewDetail.getExpirationDate());
			specialReviewDetail.setComments(copiedSpecialReviewDetail.getComments());
			specialReviewDetail.setUpdateUser(copiedSpecialReviewDetail.getUpdateUser());
			specialReviewDetail.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
			newSpecialReviews.add(specialReviewDetail);
		}
		return newSpecialReviews;
	}

}
