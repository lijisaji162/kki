package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.compilance.pojo.ProposalSpecialReview;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.proposal.comparator.ProposalAttachmentComparator;
import com.polus.fibicomp.proposal.prereview.pojo.ProposalPreReview;
import com.polus.fibicomp.util.JpaCharBooleanConversion;
import com.polus.fibicomp.workflow.pojo.Workflow;

@Entity
@Table(name = "FIBI_PROPOSAL")
public class Proposal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalIdGenerator", strategy = "com.polus.fibicomp.generator.FibiPKGenerator")
	@GeneratedValue(generator = "proposalIdGenerator")
	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;

	@Column(name = "GRANT_HEADER_ID")
	private Integer grantCallId;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK4_FIBI_PROPOSAL"), name = "GRANT_HEADER_ID", referencedColumnName = "GRANT_HEADER_ID", insertable = false, updatable = false)
	private GrantCall grantCall;

	@Column(name = "STATUS_CODE")
	private Integer statusCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_PROPOSAL"), name = "STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false)
	private ProposalStatus proposalStatus;

	@Column(name = "TYPE_CODE")
	private Integer typeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_PROPOSAL"), name = "TYPE_CODE", referencedColumnName = "TYPE_CODE", insertable = false, updatable = false)
	private ProposalType proposalType;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "START_DATE")
	private Timestamp startDate;

	@Column(name = "END_DATE")
	private Timestamp endDate;

	@Column(name = "SUBMISSION_DATE")
	private Timestamp submissionDate;

	@Column(name = "SPONSOR_DEADLINE_DATE")
	private Timestamp sponsorDeadlineDate;

	@Column(name = "INTERNAL_DEADLINE_DATE")
	private Timestamp internalDeadLineDate;

	@Column(name = "ABSTRACT_DESC")
	private String abstractDescription;

	@Column(name = "RESEARCH_AREA_DESC")
	private String researchDescription;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimeStamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "IP_NUMBER")
	private String ipNumber;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalAttachment> proposalAttachments;

	@ManyToOne(optional = true, cascade = { CascadeType.ALL })
	@JoinColumn(foreignKey = @ForeignKey(name = "FK7_FIBI_PROPOSAL"), name = "BUDGET_HEADER_ID", referencedColumnName = "BUDGET_HEADER_ID")
	private BudgetHeader budgetHeader;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalKeyword> proposalKeywords;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalPerson> proposalPersons;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalIrbProtocol> proposalIrbProtocols;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalResearchArea> proposalResearchAreas;

	@JsonManagedReference
	@OneToMany(mappedBy = "proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<ProposalSponsor> proposalSponsors;

	@Column(name = "GRANT_TYPE_CODE")
	private Integer grantTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK6_FIBI_PROPOSAL"), name = "GRANT_TYPE_CODE", referencedColumnName = "GRANT_TYPE_CODE", insertable = false, updatable = false)
	private GrantCallType grantCallType;

	@Column(name = "HOME_UNIT_NUMBER")
	private String homeUnitNumber;

	@Column(name = "HOME_UNIT_NAME")
	private String homeUnitName;

	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;

	@ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(foreignKey = @ForeignKey(name = "FK8_FIBI_PROPOSAL"), name="ACTIVITY_TYPE_CODE", referencedColumnName="ACTIVITY_TYPE_CODE", insertable = false, updatable = false)
    private ActivityType activityType;

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;

	@Column(name = "SPONSOR_NAME")
	private String sponsorName;

	@Column(name = "SUBMIT_USER")
	private String submitUser;

	@JsonManagedReference
	@OneToMany(mappedBy="proposal", orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<ProposalSpecialReview> propSpecialReviews;

	@Column(name = "SPONSOR_PROPOSAL_NUMBER")
    private String sponsorProposalNumber;

	@Column(name = "IS_SUBCONTRACT")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isSubcontract = false;

	@Column(name = "IS_MULTISITE_STUDY")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isMultisiteStudy = false;

	@Column(name = "IS_DOMESTIC_SITE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isDomesticSite = false;

	@Transient
	private String principalInvestigator;

	@Transient
	private String applicationActivityType;

	@Transient
	private String applicationType;

	@Transient
	private String applicationStatus;

	@Transient
	private Workflow workflow;

	@Transient
	private List<ProposalPreReview> proposalPreReviews;

	@Transient
	private ProposalPreReview reviewerReview;

	@Transient
	private Boolean preReviewExist = false;	

	@Transient
	private Boolean isPreReviewer = false;

	@Transient
	private List<Workflow> workflowList;

	@Column(name = "IS_INACTIVE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isInactive = false;

	@Column(name = "SUBCONTRACT_TEXT")
    private String subcontractText;

	public Proposal() {
		proposalAttachments = new ArrayList<ProposalAttachment>();
		proposalKeywords = new ArrayList<ProposalKeyword>();
		proposalPersons = new ArrayList<ProposalPerson>();
		proposalIrbProtocols = new ArrayList<ProposalIrbProtocol>();
		proposalResearchAreas = new ArrayList<ProposalResearchArea>();
		proposalSponsors = new ArrayList<ProposalSponsor>();
		propSpecialReviews = new ArrayList<ProposalSpecialReview>();
	}

	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public ProposalStatus getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(ProposalStatus proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public ProposalType getProposalType() {
		return proposalType;
	}

	public void setProposalType(ProposalType proposalType) {
		this.proposalType = proposalType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Timestamp submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getAbstractDescription() {
		return abstractDescription;
	}

	public void setAbstractDescription(String abstractDescription) {
		this.abstractDescription = abstractDescription;
	}

	public String getResearchDescription() {
		return researchDescription;
	}

	public void setResearchDescription(String researchDescription) {
		this.researchDescription = researchDescription;
	}

	public Timestamp getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Timestamp createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<ProposalAttachment> getProposalAttachments() {
		if (proposalAttachments != null && !proposalAttachments.isEmpty()) {
			Collections.sort(proposalAttachments, new ProposalAttachmentComparator());
		}
		return proposalAttachments;
	}

	public void setProposalAttachments(List<ProposalAttachment> proposalAttachments) {
		this.proposalAttachments = proposalAttachments;
	}

	public List<ProposalKeyword> getProposalKeywords() {
		return proposalKeywords;
	}

	public void setProposalKeywords(List<ProposalKeyword> proposalKeywords) {
		this.proposalKeywords = proposalKeywords;
	}

	public List<ProposalPerson> getProposalPersons() {
		return proposalPersons;
	}

	public void setProposalPersons(List<ProposalPerson> proposalPersons) {
		this.proposalPersons = proposalPersons;
	}

	public List<ProposalIrbProtocol> getProposalIrbProtocols() {
		return proposalIrbProtocols;
	}

	public void setProposalIrbProtocols(List<ProposalIrbProtocol> proposalIrbProtocols) {
		this.proposalIrbProtocols = proposalIrbProtocols;
	}

	public List<ProposalResearchArea> getProposalResearchAreas() {
		return proposalResearchAreas;
	}

	public void setProposalResearchAreas(List<ProposalResearchArea> proposalResearchAreas) {
		this.proposalResearchAreas = proposalResearchAreas;
	}

	public List<ProposalSponsor> getProposalSponsors() {
		return proposalSponsors;
	}

	public void setProposalSponsors(List<ProposalSponsor> proposalSponsors) {
		this.proposalSponsors = proposalSponsors;
	}

	public GrantCallType getGrantCallType() {
		return grantCallType;
	}

	public void setGrantCallType(GrantCallType grantCallType) {
		this.grantCallType = grantCallType;
	}

	public Integer getGrantTypeCode() {
		return grantTypeCode;
	}

	public void setGrantTypeCode(Integer grantTypeCode) {
		this.grantTypeCode = grantTypeCode;
	}

	public String getHomeUnitNumber() {
		return homeUnitNumber;
	}

	public void setHomeUnitNumber(String homeUnitNumber) {
		this.homeUnitNumber = homeUnitNumber;
	}

	public String getHomeUnitName() {
		return homeUnitName;
	}

	public void setHomeUnitName(String homeUnitName) {
		this.homeUnitName = homeUnitName;
	}

	public String getIpNumber() {
		return ipNumber;
	}

	public void setIpNumber(String ipNumber) {
		this.ipNumber = ipNumber;
	}

	public String getPrincipalInvestigator() {
		ProposalPerson pi = null;
		for (ProposalPerson person : proposalPersons) {
			if (StringUtils.equals(person.getProposalPersonRole().getCode(), Constants.PRINCIPAL_INVESTIGATOR)) {
				pi = person;
				break;
			}
		}
		principalInvestigator = pi != null ? pi.getFullName() : null;
		return principalInvestigator;
	}

	public BudgetHeader getBudgetHeader() {
		return budgetHeader;
	}

	public void setBudgetHeader(BudgetHeader budgetHeader) {
		this.budgetHeader = budgetHeader;
	}

	public void setPrincipalInvestigator(String principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getApplicationActivityType() {
		return applicationActivityType;
	}

	public void setApplicationActivityType(String applicationActivityType) {
		this.applicationActivityType = applicationActivityType;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public List<ProposalSpecialReview> getPropSpecialReviews() {
		return propSpecialReviews;
	}

	public void setPropSpecialReviews(List<ProposalSpecialReview> propSpecialReviews) {
		this.propSpecialReviews = propSpecialReviews;
	}

	public String getSponsorProposalNumber() {
		return sponsorProposalNumber;
	}

	public void setSponsorProposalNumber(String sponsorProposalNumber) {
		this.sponsorProposalNumber = sponsorProposalNumber;
	}

	public Timestamp getInternalDeadLineDate() {
		return internalDeadLineDate;
	}

	public void setInternalDeadLineDate(Timestamp internalDeadLineDate) {
		this.internalDeadLineDate = internalDeadLineDate;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public List<ProposalPreReview> getProposalPreReviews() {
		return proposalPreReviews;
	}

	public void setProposalPreReviews(List<ProposalPreReview> proposalPreReviews) {
		this.proposalPreReviews = proposalPreReviews;
	}

	public ProposalPreReview getReviewerReview() {
		return reviewerReview;
	}

	public void setReviewerReview(ProposalPreReview reviewerReview) {
		this.reviewerReview = reviewerReview;
	}

	public Boolean getPreReviewExist() {
		return preReviewExist;
	}

	public void setPreReviewExist(Boolean preReviewExist) {
		this.preReviewExist = preReviewExist;
	}

	public Boolean getIsPreReviewer() {
		return isPreReviewer;
	}

	public void setIsPreReviewer(Boolean isPreReviewer) {
		this.isPreReviewer = isPreReviewer;
	}

	public Boolean getIsSubcontract() {
		return isSubcontract;
	}

	public void setIsSubcontract(Boolean isSubcontract) {
		this.isSubcontract = isSubcontract;
	}

	public List<Workflow> getWorkflowList() {
		return workflowList;
	}

	public void setWorkflowList(List<Workflow> workflowList) {
		this.workflowList = workflowList;
	}

	public Boolean getIsMultisiteStudy() {
		return isMultisiteStudy;
	}

	public void setIsMultisiteStudy(Boolean isMultisiteStudy) {
		this.isMultisiteStudy = isMultisiteStudy;
	}

	public Boolean getIsDomesticSite() {
		return isDomesticSite;
	}

	public void setIsDomesticSite(Boolean isDomesticSite) {
		this.isDomesticSite = isDomesticSite;
	}

	public Timestamp getSponsorDeadlineDate() {
		return sponsorDeadlineDate;
	}

	public void setSponsorDeadlineDate(Timestamp sponsorDeadlineDate) {
		this.sponsorDeadlineDate = sponsorDeadlineDate;
	}

	public Boolean getIsInactive() {
		return isInactive;
	}

	public void setIsInactive(Boolean isInactive) {
		this.isInactive = isInactive;
	}

	public String getSubcontractText() {
		return subcontractText;
	}

	public void setSubcontractText(String subcontractText) {
		this.subcontractText = subcontractText;
	}

}
