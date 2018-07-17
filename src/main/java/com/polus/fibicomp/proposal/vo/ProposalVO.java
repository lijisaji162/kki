package com.polus.fibicomp.proposal.vo;

import java.util.List;
import java.util.Map;

import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalBudgetCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalCostElement;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalInstituteCentreLab;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalType;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

public class ProposalVO {

	private Integer grantCallId;

	private Proposal proposal;

	private List<ProposalCategory> proposalCategories;

	private List<ScienceKeyword> scienceKeywords;

	private List<ResearchArea> researchAreas;

	private List<ProposalResearchType> proposalResearchTypes;

	private List<FundingSourceType> fundingSourceTypes;

	private List<Protocol> protocols;

	private List<ProposalAttachmentType> proposalAttachmentTypes;

	private List<GrantCall> grantCalls;

	private List<ProposalPersonRole> proposalPersonRoles;

	private ProposalAttachment newAttachment;

	private List<ProposalCostElement> proposalCostElements;

	private List<ProposalBudgetCategory> proposalBudgetCategories;

	private List<ProposalInstituteCentreLab> proposalInstituteCentreLabs;

	private List<ProposalExcellenceArea> proposalExcellenceAreas;

	private List<SponsorType> sponsorTypes;

	private List<Sponsor> sponsors;

	private Integer keywordId;

	private Integer researchAreaId;

	private Integer attachmentId;

	private Integer budgetId;

	private Integer proposalPersonId;

	private Integer irbProtocolId;

	private Integer sponsorId;

	private Boolean status;

	private String message;

	private String updateType;

	private Integer proposalId;

	private String budgetCategoryCode;

	private String sponsorTypeCode;

	private List<ProposalType> proposalTypes;

	private Workflow workflow;

	private String actionType;

	private String userName;

	private String personId;

	private String approveComment;

	private Boolean isApprover = false;

	private Boolean isApproved = false;

	private Boolean isReviewer = false;

	private Boolean isReviewed = false;

	private Boolean isGrantAdmin = false;

	private Integer approverStopNumber;

	private Integer proposalStatusCode;

	private Boolean finalApprover;

	private GrantCallType defaultGrantCallType;

	private List<WorkflowMapDetail> availableReviewers;

	private WorkflowDetail loggedInWorkflowDetail;

	private Map<String, WorkflowStatus> workflowStatusMap;

	private String reviewerId;

	private List<Unit> homeUnits;

	public ProposalVO() {
		proposal = new Proposal();
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public List<ProposalCategory> getProposalCategories() {
		return proposalCategories;
	}

	public void setProposalCategories(List<ProposalCategory> proposalCategories) {
		this.proposalCategories = proposalCategories;
	}

	public List<ScienceKeyword> getScienceKeywords() {
		return scienceKeywords;
	}

	public void setScienceKeywords(List<ScienceKeyword> scienceKeywords) {
		this.scienceKeywords = scienceKeywords;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public List<ProposalResearchType> getProposalResearchTypes() {
		return proposalResearchTypes;
	}

	public void setProposalResearchTypes(List<ProposalResearchType> proposalResearchTypes) {
		this.proposalResearchTypes = proposalResearchTypes;
	}

	public List<FundingSourceType> getFundingSourceTypes() {
		return fundingSourceTypes;
	}

	public void setFundingSourceTypes(List<FundingSourceType> fundingSourceTypes) {
		this.fundingSourceTypes = fundingSourceTypes;
	}

	public List<Protocol> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}

	public List<ProposalAttachmentType> getProposalAttachmentTypes() {
		return proposalAttachmentTypes;
	}

	public void setProposalAttachmentTypes(List<ProposalAttachmentType> proposalAttachmentTypes) {
		this.proposalAttachmentTypes = proposalAttachmentTypes;
	}

	public List<GrantCall> getGrantCalls() {
		return grantCalls;
	}

	public void setGrantCalls(List<GrantCall> grantCalls) {
		this.grantCalls = grantCalls;
	}

	public List<ProposalPersonRole> getProposalPersonRoles() {
		return proposalPersonRoles;
	}

	public void setProposalPersonRoles(List<ProposalPersonRole> proposalPersonRoles) {
		this.proposalPersonRoles = proposalPersonRoles;
	}

	public ProposalAttachment getNewAttachment() {
		return newAttachment;
	}

	public void setNewAttachment(ProposalAttachment newAttachment) {
		this.newAttachment = newAttachment;
	}

	public List<ProposalCostElement> getProposalCostElements() {
		return proposalCostElements;
	}

	public void setProposalCostElements(List<ProposalCostElement> proposalCostElements) {
		this.proposalCostElements = proposalCostElements;
	}

	public List<ProposalBudgetCategory> getProposalBudgetCategories() {
		return proposalBudgetCategories;
	}

	public void setProposalBudgetCategories(List<ProposalBudgetCategory> proposalBudgetCategories) {
		this.proposalBudgetCategories = proposalBudgetCategories;
	}

	public List<ProposalInstituteCentreLab> getProposalInstituteCentreLabs() {
		return proposalInstituteCentreLabs;
	}

	public void setProposalInstituteCentreLabs(List<ProposalInstituteCentreLab> proposalInstituteCentreLabs) {
		this.proposalInstituteCentreLabs = proposalInstituteCentreLabs;
	}

	public List<ProposalExcellenceArea> getProposalExcellenceAreas() {
		return proposalExcellenceAreas;
	}

	public void setProposalExcellenceAreas(List<ProposalExcellenceArea> proposalExcellenceAreas) {
		this.proposalExcellenceAreas = proposalExcellenceAreas;
	}

	public List<SponsorType> getSponsorTypes() {
		return sponsorTypes;
	}

	public void setSponsorTypes(List<SponsorType> sponsorTypes) {
		this.sponsorTypes = sponsorTypes;
	}

	public List<Sponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<Sponsor> sponsors) {
		this.sponsors = sponsors;
	}

	public Integer getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(Integer keywordId) {
		this.keywordId = keywordId;
	}

	public Integer getResearchAreaId() {
		return researchAreaId;
	}

	public void setResearchAreaId(Integer researchAreaId) {
		this.researchAreaId = researchAreaId;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}

	public Integer getProposalPersonId() {
		return proposalPersonId;
	}

	public void setProposalPersonId(Integer proposalPersonId) {
		this.proposalPersonId = proposalPersonId;
	}

	public Integer getIrbProtocolId() {
		return irbProtocolId;
	}

	public void setIrbProtocolId(Integer irbProtocolId) {
		this.irbProtocolId = irbProtocolId;
	}

	public Integer getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(Integer sponsorId) {
		this.sponsorId = sponsorId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public String getBudgetCategoryCode() {
		return budgetCategoryCode;
	}

	public void setBudgetCategoryCode(String budgetCategoryCode) {
		this.budgetCategoryCode = budgetCategoryCode;
	}

	public String getSponsorTypeCode() {
		return sponsorTypeCode;
	}

	public void setSponsorTypeCode(String sponsorTypeCode) {
		this.sponsorTypeCode = sponsorTypeCode;
	}

	public List<ProposalType> getProposalTypes() {
		return proposalTypes;
	}

	public void setProposalTypes(List<ProposalType> proposalTypes) {
		this.proposalTypes = proposalTypes;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getApproveComment() {
		return approveComment;
	}

	public void setApproveComment(String approveComment) {
		this.approveComment = approveComment;
	}

	public Boolean getIsApprover() {
		return isApprover;
	}

	public void setIsApprover(Boolean isApprover) {
		this.isApprover = isApprover;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Boolean getIsReviewer() {
		return isReviewer;
	}

	public void setIsReviewer(Boolean isReviewer) {
		this.isReviewer = isReviewer;
	}

	public Boolean getIsReviewed() {
		return isReviewed;
	}

	public void setIsReviewed(Boolean isReviewed) {
		this.isReviewed = isReviewed;
	}

	public Boolean getIsGrantAdmin() {
		return isGrantAdmin;
	}

	public void setIsGrantAdmin(Boolean isGrantAdmin) {
		this.isGrantAdmin = isGrantAdmin;
	}

	public Integer getApproverStopNumber() {
		return approverStopNumber;
	}

	public void setApproverStopNumber(Integer approverStopNumber) {
		this.approverStopNumber = approverStopNumber;
	}

	public Integer getProposalStatusCode() {
		return proposalStatusCode;
	}

	public void setProposalStatusCode(Integer proposalStatusCode) {
		this.proposalStatusCode = proposalStatusCode;
	}

	public Boolean getFinalApprover() {
		return finalApprover;
	}

	public void setFinalApprover(Boolean finalApprover) {
		this.finalApprover = finalApprover;
	}

	public GrantCallType getDefaultGrantCallType() {
		return defaultGrantCallType;
	}

	public void setDefaultGrantCallType(GrantCallType defaultGrantCallType) {
		this.defaultGrantCallType = defaultGrantCallType;
	}

	public WorkflowDetail getLoggedInWorkflowDetail() {
		return loggedInWorkflowDetail;
	}

	public void setLoggedInWorkflowDetail(WorkflowDetail loggedInWorkflowDetail) {
		this.loggedInWorkflowDetail = loggedInWorkflowDetail;
	}

	public List<WorkflowMapDetail> getAvailableReviewers() {
		return availableReviewers;
	}

	public void setAvailableReviewers(List<WorkflowMapDetail> availableReviewers) {
		this.availableReviewers = availableReviewers;
	}

	public Map<String, WorkflowStatus> getWorkflowStatusMap() {
		return workflowStatusMap;
	}

	public void setWorkflowStatusMap(Map<String, WorkflowStatus> workflowStatusMap) {
		this.workflowStatusMap = workflowStatusMap;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

}
