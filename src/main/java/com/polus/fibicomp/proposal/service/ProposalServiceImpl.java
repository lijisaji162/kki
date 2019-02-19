package com.polus.fibicomp.proposal.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
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
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.prereview.dao.ProposalPreReviewDao;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.role.dao.RoleDao;
import com.polus.fibicomp.role.pojo.RoleMemberAttributeDataBo;
import com.polus.fibicomp.role.pojo.RoleMemberBo;
import com.polus.fibicomp.workflow.comparator.WorkflowComparator;
import com.polus.fibicomp.workflow.comparator.WorkflowDetailComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
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
	private BudgetService budgetService;

	@Value("${application.context.name}")
	private String context;

	@Autowired
	private BudgetDao budgetDao;

	@Autowired
	private ComplianceDao complianceDao;

	@Autowired
	public CommonDao commonDao;

	@Autowired
	private FibiEmailService fibiEmailService;

	@Autowired
	private ProposalPreReviewDao proposalPreReviewDao;

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
			List<ProposalAttachment> attachments = proposal.getProposalAttachments();
			List<ProposalAttachment> newAttachments = proposalVO.getNewAttachments();
			List<ProposalAttachment> proposalAttachments = new ArrayList<ProposalAttachment>();
			for (int i = 0; i < files.length; i++) {
				for (ProposalAttachment newAttachment : newAttachments) {
					if (newAttachment.getAttachmentId() != null) {
						for(ProposalAttachment attachment : attachments) {
							if (attachment.getAttachmentId() != null && attachment.getAttachmentId().equals(newAttachment.getAttachmentId())) {
								File file = new File(files[i].getOriginalFilename());
								String fileName = file.getName();
								attachment.setFileName(fileName);
								attachment.setAttachmentType(newAttachment.getAttachmentType());
								attachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
								attachment.setDescription(newAttachment.getDescription());
								attachment.setUpdateTimeStamp(newAttachment.getUpdateTimeStamp());
								attachment.setUpdateUser(newAttachment.getUpdateUser());
								attachment.setAttachment(files[i].getBytes());
								attachment.setNarrativeStatus(newAttachment.getNarrativeStatus());
								attachment.setNarrativeStatusCode(newAttachment.getNarrativeStatusCode());
								attachment.setMimeType(files[i].getContentType());
							}
						}
					} else {
						File file = new File(files[i].getOriginalFilename());
						String fileName = file.getName();
						if (newAttachment.getFileName().equals(fileName)) {
							ProposalAttachment proposalAttachment = new ProposalAttachment();
							proposalAttachment.setAttachmentType(newAttachment.getAttachmentType());
							proposalAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
							proposalAttachment.setDescription(newAttachment.getDescription());
							proposalAttachment.setUpdateTimeStamp(newAttachment.getUpdateTimeStamp());
							proposalAttachment.setUpdateUser(newAttachment.getUpdateUser());
							proposalAttachment.setAttachment(files[i].getBytes());
							proposalAttachment.setFileName(fileName);
							proposalAttachment.setNarrativeStatus(newAttachment.getNarrativeStatus());
							proposalAttachment.setNarrativeStatusCode(newAttachment.getNarrativeStatusCode());
							proposalAttachment.setMimeType(files[i].getContentType());
							proposalAttachments.add(proposalAttachment);
						}
					}
				}
			}
			if(newAttachments.get(0).getAttachmentId() == null) {
				proposal.getProposalAttachments().addAll(proposalAttachments);		
			}
			//proposal.getProposalAttachments().addAll(proposalAttachments);
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
			if(proposal.getEndDate().after(proposal.getBudgetHeader().getEndDate())) {
				List<BudgetPeriod> budgetPeriods = generateBudgetPeriods(proposal);
				proposal.getBudgetHeader().getBudgetPeriods().addAll(budgetPeriods);
				proposal.getBudgetHeader().setEndDate(proposal.getEndDate());
			}
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
		loadInitialData(vo);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	public List<BudgetPeriod> generateBudgetPeriods(Proposal proposal) {
		BudgetPeriod lastPeriod = budgetDao.getMaxBudgetPeriodByBudgetId(proposal.getBudgetHeader().getBudgetId());
		List<BudgetPeriod> budgetPeriods = new ArrayList<BudgetPeriod>();
		Calendar c = Calendar.getInstance();
		c.setTime(proposal.getBudgetHeader().getEndDate());
		c.add(Calendar.DAY_OF_YEAR, 1);
		Date newBudgetPeriodStartDate = c.getTime();
		Date projectEndDate = proposal.getEndDate();
		boolean budgetPeriodExists = true;

		Calendar cl = Calendar.getInstance();

		Date periodStartDate = newBudgetPeriodStartDate;
		int budgetPeriodNum = lastPeriod.getBudgetPeriod() + 1;
		while (budgetPeriodExists) {
			cl.setTime(periodStartDate);
			cl.add(Calendar.YEAR, 1);
			Date nextPeriodStartDate = new Date(cl.getTime().getTime());
			cl.add(Calendar.DATE, -1);
			Date periodEndDate = new Date(cl.getTime().getTime());
			/* check period end date gt project end date */
			switch (periodEndDate.compareTo(projectEndDate)) {
			case 1:
				periodEndDate = projectEndDate;
				// the break statement is purposefully missing.
			case 0:
				budgetPeriodExists = false;
				break;
			}
			BudgetPeriod budgetPeriod = new BudgetPeriod();
			budgetPeriod.setBudgetPeriod(budgetPeriodNum);
			Timestamp periodStartDateTimeStamp =new Timestamp(periodStartDate.getTime());
			Timestamp periodEndDateTimeStamp = new Timestamp(periodEndDate.getTime());
			budgetPeriod.setStartDate(periodStartDateTimeStamp);
			budgetPeriod.setEndDate(periodEndDateTimeStamp);
			budgetPeriod.setBudget(proposal.getBudgetHeader());

			budgetPeriods.add(budgetPeriod);
			periodStartDate = nextPeriodStartDate;
			budgetPeriodNum++;
		}
		return budgetPeriods;
	}

	@Override
	public String loadProposalById(Integer proposalId, String personId) {
		ProposalVO proposalVO = new ProposalVO();
		proposalVO.setPersonId(personId);
		Proposal proposal = proposalDao.fetchProposalById(proposalId);
		proposalVO.setProposal(proposal);
		List<ProposalPerson> proposalPersons = proposal.getProposalPersons();
		if (proposalPersons != null && !proposalPersons.isEmpty()) {
			proposalVO.setIsProposalPerson(isProposalEmployee(proposalPersons, personId));
		}
		int statusCode = proposal.getStatusCode();
		if (statusCode == Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS || statusCode == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS || statusCode == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
			loadInitialData(proposalVO);
		} else {
			Boolean isDeclarationSectionRequired = commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
					Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.IS_REQUIRED_DECLARATION_SECTION);
			proposalVO.setIsDeclarationSectionRequired(isDeclarationSectionRequired);
		}
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_AWARDED)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_RETURNED)) {
			canTakeRoutingAction(proposalVO);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			workflowService.prepareWorkflowDetails(workflow);
			proposalVO.setWorkflow(workflow);
			List<Workflow> WorkflowList = workflowDao.fetchWorkflowsByModuleItemId(proposal.getProposalId());
			if(WorkflowList != null) {
				workflowService.prepareWorkflowDetailsList(WorkflowList);
				Collections.sort(WorkflowList, new WorkflowComparator());
				proposalVO.setWorkflowList(WorkflowList);
				if (proposalVO.getFinalApprover()) {
					List<ProposalPreReview> reviewerReviews = proposalPreReviewDao.fetchPreReviewsByCriteria(proposalId, null, Constants.PRE_REVIEW_STATUS_INPROGRESS);
					if (reviewerReviews != null && !reviewerReviews.isEmpty()) {
						proposalVO.setIsPreReviewCompletionRequired(commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
								Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.PREREVIEW_COMPLETION_REQUIRED));
					}
				}
			}
		}
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
		proposal.setSubmissionDate(committeeDao.getCurrentTimestamp());
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String message = "The following proposal is successfully submitted for approval:<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
				+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
				+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
				+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
				+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
				+ proposal.getProposalId() +"\">this link</a> "
				+ "to review the proposal and provide your response by clicking on the Approve or Reject buttons.";
		String subject = "Action Required: Approval for "+ proposal.getTitle();

		String sponsorTypeCode = proposalDao.fetchSponsorTypeCodeBySponsorCode(proposal.getSponsorCode());
		Workflow workflow = workflowService.createWorkflow(proposal.getProposalId(), proposalVO.getUserName(), proposalVO.getProposalStatusCode(), sponsorTypeCode, subject, message);
		canTakeRoutingAction(proposalVO);
		workflowService.prepareWorkflowDetails(workflow);
		loadInitialData(proposalVO);
		proposalVO.setWorkflow(workflow);
		List<Workflow> workflowList = workflowDao.fetchWorkflowsByModuleItemId(proposal.getProposalId());
		if(workflowList != null) {
			workflowService.prepareWorkflowDetailsList(workflowList);
			Collections.sort(workflowList, new WorkflowComparator());
			proposalVO.setWorkflowList(workflowList);
		}
		proposalVO.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	public void canTakeRoutingAction(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
		Integer maxApprovalStopNumber = workflowDao.getMaxStopNumber(workflow.getWorkflowId());
		List<WorkflowDetail> finalWorkflowDetails = workflowDao.fetchFinalApprover(workflow.getWorkflowId(), maxApprovalStopNumber);
		if(finalWorkflowDetails != null && !finalWorkflowDetails.isEmpty()) {
			for(WorkflowDetail finalWorkflowDetail : finalWorkflowDetails) {
				if(finalWorkflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
					proposalVO.setFinalApprover(true);
				}
			}
		}
		List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
		Collections.sort(workflowDetails, new WorkflowDetailComparator());
		boolean currentPerson = true;
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)) {
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (currentPerson == true) {
					if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
						if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
								&& workflowDetail.getApprovalStatusCode()
										.equals(Constants.WORKFLOW_STATUS_CODE_WAITING)) {
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
					}
				}
			}
		}
	}

	@Override
	public String approveOrRejectProposal(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		Set<String> toAddresses = new HashSet<String>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalDao.fetchProposalById(proposalVO.getProposalId());
			String actionType = proposalVO.getActionType();
			String approverComment = proposalVO.getApproveComment();
			boolean isFinalApprover = true;

			logger.info("proposalId : " + proposal.getProposalId());
			logger.info("actionType : " + actionType);
			logger.info("personId : " + proposalVO.getPersonId());
			logger.info("approverComment : " + approverComment);

			String piName = getPrincipalInvestigator(proposal.getProposalPersons());
			String message = "The following proposal has routed for approval:<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
					+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
					+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
					+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
					+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
					+ proposal.getProposalId() +"\">this link</a> "
					+ "to review the proposal and provide your response by clicking on the Approve or Reject buttons.";
			String subject = "Action Required: Review for "+ proposal.getTitle();

			workflowService.approveOrRejectWorkflowDetail(actionType, proposal.getProposalId(), proposalVO.getPersonId(), proposalVO.getIsSuperUser(), approverComment, files, null, subject, message);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
			//String stopNumber = commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.WORKFLOW_STOP_NUMBER);
			for (WorkflowDetail workflowDetail1 : workflowDetails) {
				/*if (!(workflowDetail1.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_APPROVED) || workflowDetail1.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_APPROVED_BY_OTHER) || workflowDetail1.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_APPROVAL_BYPASSED))) {
					isFinalApprover = false;
				}*/
				if (workflowDetail1.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING) ) {
					isFinalApprover = false;
				}
				/*if(workflowDetail1.getApprovalStopNumber().equals(stopNumber) && workflowDetail1.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_APPROVED)) {
					String fyiMessage = "The following proposal is successfully routed and awarded :<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
							+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
							+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
							+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
							+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
							+ proposal.getProposalId() +"\">this link</a> "
							+ "to review the application.";
					String fyiSubject = "Action Required: Review for "+ proposal.getTitle();
					String fyiRecipients = commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.KKI_FYI_EMAIL_RECIPIENT);
					if (fyiRecipients != null && !fyiRecipients.isEmpty()) {
						List<String> recipients = Arrays.asList(fyiRecipients.split(","));
						for (String recipient : recipients) {
							toAddresses.add(recipient);
						}
						fibiEmailService.sendEmail(toAddresses, fyiSubject, null, null, fyiMessage, true);						
					}
				}*/
			}		
			logger.info("isFinalApprover : " + isFinalApprover);
			if (isFinalApprover && actionType.equals("A")) {
				String ipNumber = institutionalProposalService.generateInstitutionalProposalNumber();
				logger.info("Initial IP Number : " + ipNumber);
				boolean isIPCreated = institutionalProposalService.createInstitutionalProposal(proposal.getProposalId(), ipNumber, proposal.getUpdateUser());
				logger.info("isIPCreated : " + isIPCreated);
				if (isIPCreated) {
					String awardedMessage = "The following proposal is successfully routed and awarded :<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
							+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
							+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
							+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
							+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
							+ proposal.getProposalId() +"\">this link</a> "
							+ "to review the proposal.";
					String awardedSubject = "The proposal "+ proposal.getProposalId() + " is approved";
					logger.info("Generated IP Number : " + ipNumber);
					proposal.setIpNumber(ipNumber);
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_AWARDED);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_AWARDED));
					// toAddresses.add(getPIEmailAddress(proposal.getProposalPersons()));
					toAddresses.add(proposalDao.getCreateUserEmailAddress(proposal.getCreateUser()));
					String awardRecipients = commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE, Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.FINAL_NOTIFICATION_RECIPIENT);
					if (awardRecipients != null && !awardRecipients.isEmpty()) {
						List<String> recipients = Arrays.asList(awardRecipients.split(","));
						for (String recipient : recipients) {
							toAddresses.add(recipient);
						}
					}
					fibiEmailService.sendEmail(toAddresses, awardedSubject, null, null, awardedMessage, true);
				}
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			} else if (actionType.equals("R")) {
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_RETURNED);
					ProposalStatus proposalStatus = proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_RETURNED);
					logger.info("proposalStatus : " + proposalStatus);
					proposal.setProposalStatus(proposalStatus);
					proposal = proposalDao.saveOrUpdateProposal(proposal);
					logger.info("proposal staus code : " + proposal.getStatusCode());
					logger.info("proposal staus desc : " + proposal.getProposalStatus().getDescription());
					String rejectMessage = "The following proposal is returned  :<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
							+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
							+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
							+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
							+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
							+ proposal.getProposalId() +"\">this link</a> "
							+ "to review the proposal.";
					String rejectSubject = "Action Required: Returned for "+ proposal.getTitle();
					logger.info("rejectSubject : " + rejectMessage);
					// toAddresses.add(getPIEmailAddress(proposal.getProposalPersons()));
					toAddresses.add(proposalDao.getCreateUserEmailAddress(proposal.getCreateUser()));
					logger.info("toAddresses : " + toAddresses);
					fibiEmailService.sendEmail(toAddresses, rejectSubject, null, null, rejectMessage, true);
			}
			proposalVO.setProposal(proposal);
			if (proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED || proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS) {
				loadInitialData(proposalVO);
			}
			proposalVO.setFinalApprover(isFinalApprover);
			proposalVO.setIsApproved(true);
			proposalVO.setIsApprover(true);
			workflowService.prepareWorkflowDetails(workflow);
			proposalVO.setWorkflow(workflow);
			List<Workflow> workflowList = workflowDao.fetchWorkflowsByModuleItemId(proposal.getProposalId());
			if(workflowList != null) {
				workflowService.prepareWorkflowDetailsList(workflowList);
				Collections.sort(workflowList, new WorkflowComparator());
				proposalVO.setWorkflowList(workflowList);
			}
			proposalVO.setProposal(proposal);
			logger.info("end of proposal staus code : " + proposal.getStatusCode());
			logger.info("end of proposal staus desc : " + proposal.getProposalStatus().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public void loadInitialData(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Integer proposalId = proposal.getProposalId();
		if (proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS || proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
			Boolean isDeclarationSectionRequired = commonDao.getParameterValueAsBoolean(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
					Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.IS_REQUIRED_DECLARATION_SECTION);
			proposalVO.setIsDeclarationSectionRequired(isDeclarationSectionRequired);
			// proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
			proposalVO.setActivityTypes(proposalDao.fetchAllActivityTypes());
			// proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
			// proposalVO.setNonEmployeeList(proposalDao.fetchAllNonEmployees());
			if (isDeclarationSectionRequired) {
				proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
				proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
				proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
				proposalVO.setProtocols(proposalDao.fetchAllProtocols());
				proposalVO.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
				proposalVO.setSponsorTypes(grantCallDao.fetchAllSponsorTypes());
			}
			proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
			proposalVO.setProposalTypes(proposalDao.fetchAllProposalTypes());
			proposalVO.setDefaultGrantCallType(grantCallDao.fetchGrantCallTypeByGrantTypeCode(Constants.GRANT_CALL_TYPE_OTHERS));
			if (proposal.getBudgetHeader() != null) {
				// proposalVO.setCostElements(budgetDao.getAllCostElements());
				proposalVO.setSysGeneratedCostElements(budgetService.fetchSysGeneratedCostElements());
				Set<String> rateClassTypes = new HashSet<>();
				List<FibiProposalRate> proposalRates = proposal.getBudgetHeader().getProposalRates();
				if (proposalRates != null && !proposalRates.isEmpty()) {
					for (FibiProposalRate proposalRate : proposalRates) {
						rateClassTypes.add(proposalRate.getRateClass().getDescription());
						proposalVO.setRateClassTypes(rateClassTypes);
					}
				}
				// proposalVO.setBudgetCategories(budgetDao.fetchAllBudgetCategory());
				proposalVO.setTbnPersons(budgetDao.fetchAllTbnPerson());
			}
			// proposalVO.setSponsors(proposalDao.fetchAllSponsors());
			proposalVO.setReviewTypes(getSpecialReviewTypes());
			List<String> approvalTypeCodes = new ArrayList<>();
			approvalTypeCodes.add(Constants.SP_REV_APPROVAL_TYPE_LINK_TO_IRB);
			approvalTypeCodes.add(Constants.SP_REV_APPROVAL_TYPE_LINK_TO_IACUC);
			proposalVO.setSpecialReviewApprovalTypes(complianceDao.fetchSpecialReviewApprovalTypeNotInCodes(approvalTypeCodes));
			// proposalVO.setDepartments(proposalDao.fetchAllUnits());
			getHomeUnits(proposalVO);
			proposalVO.setNarrativeStatus(proposalDao.fetchAllNarrativeStatus());
			proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
			proposalVO.setPreReviewTypes(proposalPreReviewDao.fetchAllPreReviewTypes());
			if (proposalId != null) { // not required for create proposal state
				proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposalId));
				List<ProposalPreReview> reviewerReviews = proposalPreReviewDao.fetchPreReviewsByCriteria(proposalId, proposalVO.getPersonId(), Constants.PRE_REVIEW_STATUS_INPROGRESS);
				if (reviewerReviews != null && !reviewerReviews.isEmpty()) {
					proposal.setIsPreReviewer(true);
					proposal.setReviewerReview(reviewerReviews.get(0));
				}
			}
			proposalVO.setPreReviewers(proposalPreReviewDao.fetchAllPreReviewer());
		}
		if (proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS) {
			proposalVO.setNarrativeStatus(proposalDao.fetchAllNarrativeStatus());
			proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
			proposalVO.setPreReviewTypes(proposalPreReviewDao.fetchAllPreReviewTypes());
			proposal.setProposalPreReviews(proposalPreReviewDao.loadAllProposalPreReviewsByProposalId(proposalId));
			List<ProposalPreReview> reviewerReviews = proposalPreReviewDao.fetchPreReviewsByCriteria(proposalId, proposalVO.getPersonId(), Constants.PRE_REVIEW_STATUS_INPROGRESS);
			if (reviewerReviews != null && !reviewerReviews.isEmpty()) {
				proposal.setIsPreReviewer(true);
				proposal.setReviewerReview(reviewerReviews.get(0));
			}
			proposalVO.setPreReviewers(proposalPreReviewDao.fetchAllPreReviewer());
		}
		if (proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS || proposal.getStatusCode() == Constants.PROPOSAL_STATUS_CODE_RETURNED) {
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			workflowService.prepareWorkflowDetails(workflow);
			proposalVO.setWorkflow(workflow);
			List<Workflow> WorkflowList = workflowDao.fetchWorkflowsByModuleItemId(proposal.getProposalId());
			if(WorkflowList != null) {
				workflowService.prepareWorkflowDetailsList(WorkflowList);
				Collections.sort(WorkflowList, new WorkflowComparator());
				proposalVO.setWorkflowList(WorkflowList);
			}
		}
	}

	@Override
	public String getPrincipalInvestigator(List<ProposalPerson> proposalPersons) {
		String piName = "";
		for (ProposalPerson person : proposalPersons) {
			if (person.getProposalPersonRole().getCode().equals(Constants.PRINCIPAL_INVESTIGATOR)) {
				piName = person.getFullName();
			}
		}
		return piName;
	}

	public void getHomeUnits(ProposalVO proposalVO) {
		List<RoleMemberBo> memberBos = roleDao.fetchUserRole(proposalVO.getPersonId(), Constants.CREATE_PROPOSAL_ROLE);
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

			// logger.info("create proposal unitNumbers : " + unitNumbers);
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

	public String getPIEmailAddress(List<ProposalPerson> proposalPersons) {
		String emailAddress = "";
		for (ProposalPerson person : proposalPersons) {
			if (person.getProposalPersonRole().getCode().equals(Constants.PRINCIPAL_INVESTIGATOR)) {
				emailAddress = person.getEmailAddress();
			}
		}
		return emailAddress;
	}

	@Override
	public String sendAttachPINotification(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Set<String> toAddresses = new HashSet<String>();
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String attachmentMessage = "The following proposal contains incomplete attachment: :<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
				+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
				+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
				+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
				+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
				+ proposal.getProposalId() +"\">this link</a> "
				+ "to review the proposal.";
		String attachmentSubject = "Action Required: Complete Attachment for "+ proposal.getTitle();
		// toAddresses.add(getPIEmailAddress(proposal.getProposalPersons()));
		toAddresses.add(proposalDao.getCreateUserEmailAddress(proposal.getCreateUser()));
		fibiEmailService.sendEmail(toAddresses, attachmentSubject, null, null, attachmentMessage, true);
		return "SUCCESS";
	}

	@Override
	public String sendAttachApproverNotification(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Set<String> toAddresses = new HashSet<String>();
		Integer maxApprovalStopNumber = workflowDao.getMaxStopNumber(proposalVO.getWorkflow().getWorkflowId());
		List<WorkflowDetail> finalApprovers = workflowDao.fetchFinalApprover(proposalVO.getWorkflow().getWorkflowId(), maxApprovalStopNumber);
		for(WorkflowDetail workflowDetail : finalApprovers) {
			toAddresses.add(workflowDetail.getEmailAddress());
		}
		String piName = getPrincipalInvestigator(proposal.getProposalPersons());
		String attachmentMessage = "The following proposal contains attachments are completed: :<br/><br/>Proposal Number: "+ proposal.getProposalId() +"<br/>"
				+ "Proposal Title: "+ proposal.getTitle() +"<br/>Principal Investigator: "+ piName +"<br/>"
				+ "Lead Unit: "+ proposal.getHomeUnitNumber() +" - "+ proposal.getHomeUnitName() +"<br/>"
				+ "Deadline Date: "+ proposal.getSponsorDeadlineDate() +"<br/><br/>Please go to "
				+ "<a title=\"\" target=\"_self\" href=\""+ context +"/proposal/proposalHome?proposalId="
				+ proposal.getProposalId() +"\">this link</a> "
				+ "to review the proposal.";
		String attachmentSubject = "Action Required: Completed Attachments for "+ proposal.getTitle();
		fibiEmailService.sendEmail(toAddresses, attachmentSubject, null, null, attachmentMessage, true);
		return "SUCCESS";
	}

	public Boolean isProposalEmployee(List<ProposalPerson> proposalPersons, String personId) {
		Boolean isProposalPerson = false;
		for (ProposalPerson person : proposalPersons) {
			if (person.getPersonId() != null && personId.equals(person.getPersonId())) {
				isProposalPerson = true;
				break;
			}
		}
		return isProposalPerson;
	}

	@Override
	public String markAsInactive(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		Integer result = proposalDao.markAsInactive(proposal.getProposalId(), proposal.getIsInactive());
		if (result == 1) {
			proposalVO.setInactiveMessage(Constants.MARK_INACTIVE_SUCCESS_MESSAGE);
		} else {
			proposalVO.setInactiveMessage(Constants.MARK_INACTIVE_ERROR_MESSAGE);
		}
		return committeeDao.convertObjectToJSON(proposalVO);
	}

}
