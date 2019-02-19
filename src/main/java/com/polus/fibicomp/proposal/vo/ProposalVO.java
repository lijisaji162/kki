package com.polus.fibicomp.proposal.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.polus.fibicomp.budget.pojo.BudgetCategory;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.TbnPerson;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.compilance.pojo.SpecialReviewApprovalType;
import com.polus.fibicomp.compilance.pojo.SpecialReviewType;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.proposal.pojo.NarrativeStatus;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalExcellenceArea;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalType;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewStatus;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewType;
import com.polus.fibicomp.proposal.prereview.pojo.PreReviewer;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

public class ProposalVO {

	private Integer grantCallId;

	private Proposal proposal;

	private List<ActivityType> activityTypes;

	private List<ScienceKeyword> scienceKeywords;

	private List<ResearchArea> researchAreas;

	private List<ProposalResearchType> proposalResearchTypes;

	private List<FundingSourceType> fundingSourceTypes;

	private List<Protocol> protocols;

	private List<ProposalAttachmentType> proposalAttachmentTypes;

	private List<GrantCall> grantCalls;

	private List<ProposalPersonRole> proposalPersonRoles;

	private List<ProposalAttachment> newAttachments;

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

	private String userFullName;

	private String personId;

	private String approveComment;

	private Boolean isApprover = false;

	private Boolean isApproved = false;

	private Boolean isReviewed = false;

	private Boolean isGrantAdmin = false;

	private Integer approverStopNumber;

	private Integer proposalStatusCode;

	private Boolean finalApprover = false;

	private GrantCallType defaultGrantCallType;

	private List<WorkflowMapDetail> availableReviewers;

	private WorkflowDetail loggedInWorkflowDetail;

	private Map<String, WorkflowStatus> workflowStatusMap;

	private String reviewerId;

	private List<Unit> homeUnits;

	private List<CostElement> costElements;

	private Integer budgetPeriodId;

	private List<CostElement> sysGeneratedCostElements;

	private Integer budgetDetailId;

	private Set<String> rateClassTypes;

	private List<BudgetCategory> budgetCategories;

	private Integer copyPeriodId;

	private Integer currentPeriodId;

	private List<SpecialReviewType> reviewTypes;

	private List<SpecialReviewApprovalType> specialReviewApprovalTypes;

	private List<TbnPerson> tbnPersons;

	private Integer proposalSpecialReviewId;

	private Boolean isDeclarationSectionRequired = false;

	private Boolean isPreReviewCompletionRequired = false;

	private List<Unit> departments;

	private List<NarrativeStatus> narrativeStatus;

	private List<PreReviewType> preReviewTypes;

	private List<PreReviewStatus> preReviewStatus;

	private ProposalPreReview newProposalPreReview;

	private List<Workflow> workflowList;

	private Boolean isProposalPerson = false;

	private Boolean isSuperUser = false;

	private List<PreReviewer> preReviewers;

	private Boolean isInactive = false;

	private String inactiveMessage;

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

	public List<CostElement> getCostElements() {
		return costElements;
	}

	public void setCostElements(List<CostElement> costElements) {
		this.costElements = costElements;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public List<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(List<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public List<CostElement> getSysGeneratedCostElements() {
		return sysGeneratedCostElements;
	}

	public void setSysGeneratedCostElements(List<CostElement> sysGeneratedCostElements) {
		this.sysGeneratedCostElements = sysGeneratedCostElements;
	}

	public Integer getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}

	public Integer getBudgetPeriodId() {
		return budgetPeriodId;
	}

	public void setBudgetPeriodId(Integer budgetPeriodId) {
		this.budgetPeriodId = budgetPeriodId;
	}

	public Integer getBudgetDetailId() {
		return budgetDetailId;
	}

	public void setBudgetDetailId(Integer budgetDetailId) {
		this.budgetDetailId = budgetDetailId;
	}

	public Set<String> getRateClassTypes() {
		return rateClassTypes;
	}

	public void setRateClassTypes(Set<String> rateClassTypes) {
		this.rateClassTypes = rateClassTypes;
	}

	public List<BudgetCategory> getBudgetCategories() {
		return budgetCategories;
	}

	public void setBudgetCategories(List<BudgetCategory> budgetCategories) {
		this.budgetCategories = budgetCategories;
	}

	public Integer getCopyPeriodId() {
		return copyPeriodId;
	}

	public void setCopyPeriodId(Integer copyPeriodId) {
		this.copyPeriodId = copyPeriodId;
	}

	public Integer getCurrentPeriodId() {
		return currentPeriodId;
	}

	public void setCurrentPeriodId(Integer currentPeriodId) {
		this.currentPeriodId = currentPeriodId;
	}

	public List<SpecialReviewType> getReviewTypes() {
		return reviewTypes;
	}

	public void setReviewTypes(List<SpecialReviewType> reviewTypes) {
		this.reviewTypes = reviewTypes;
	}

	public List<SpecialReviewApprovalType> getSpecialReviewApprovalTypes() {
		return specialReviewApprovalTypes;
	}

	public void setSpecialReviewApprovalTypes(List<SpecialReviewApprovalType> specialReviewApprovalTypes) {
		this.specialReviewApprovalTypes = specialReviewApprovalTypes;
	}

	public List<TbnPerson> getTbnPersons() {
		return tbnPersons;
	}

	public void setTbnPersons(List<TbnPerson> tbnPersons) {
		this.tbnPersons = tbnPersons;
	}

	public Integer getProposalSpecialReviewId() {
		return proposalSpecialReviewId;
	}

	public void setProposalSpecialReviewId(Integer proposalSpecialReviewId) {
		this.proposalSpecialReviewId = proposalSpecialReviewId;
	}

	public Boolean getIsDeclarationSectionRequired() {
		return isDeclarationSectionRequired;
	}

	public void setIsDeclarationSectionRequired(Boolean isDeclarationSectionRequired) {
		this.isDeclarationSectionRequired = isDeclarationSectionRequired;
	}

	public List<Unit> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Unit> departments) {
		this.departments = departments;
	}

	public List<NarrativeStatus> getNarrativeStatus() {
		return narrativeStatus;
	}

	public void setNarrativeStatus(List<NarrativeStatus> narrativeStatus) {
		this.narrativeStatus = narrativeStatus;
	}

	public List<ProposalAttachment> getNewAttachments() {
		return newAttachments;
	}

	public void setNewAttachments(List<ProposalAttachment> newAttachments) {
		this.newAttachments = newAttachments;
	}

	public List<PreReviewType> getPreReviewTypes() {
		return preReviewTypes;
	}

	public void setPreReviewTypes(List<PreReviewType> preReviewTypes) {
		this.preReviewTypes = preReviewTypes;
	}

	public List<PreReviewStatus> getPreReviewStatus() {
		return preReviewStatus;
	}

	public void setPreReviewStatus(List<PreReviewStatus> preReviewStatus) {
		this.preReviewStatus = preReviewStatus;
	}

	public ProposalPreReview getNewProposalPreReview() {
		return newProposalPreReview;
	}

	public void setNewProposalPreReview(ProposalPreReview newProposalPreReview) {
		this.newProposalPreReview = newProposalPreReview;
	}

	public List<Workflow> getWorkflowList() {
		return workflowList;
	}

	public void setWorkflowList(List<Workflow> workflowList) {
		this.workflowList = workflowList;
	}

	public Boolean getIsPreReviewCompletionRequired() {
		return isPreReviewCompletionRequired;
	}

	public void setIsPreReviewCompletionRequired(Boolean isPreReviewCompletionRequired) {
		this.isPreReviewCompletionRequired = isPreReviewCompletionRequired;
	}

	public Boolean getIsProposalPerson() {
		return isProposalPerson;
	}

	public void setIsProposalPerson(Boolean isProposalPerson) {
		this.isProposalPerson = isProposalPerson;
	}

	public Boolean getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(Boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public List<PreReviewer> getPreReviewers() {
		return preReviewers;
	}

	public void setPreReviewers(List<PreReviewer> preReviewers) {
		this.preReviewers = preReviewers;
	}

	public Boolean getIsInactive() {
		return isInactive;
	}

	public void setIsInactive(Boolean isInactive) {
		this.isInactive = isInactive;
	}

	public String getInactiveMessage() {
		return inactiveMessage;
	}

	public void setInactiveMessage(String inactiveMessage) {
		this.inactiveMessage = inactiveMessage;
	}

}
