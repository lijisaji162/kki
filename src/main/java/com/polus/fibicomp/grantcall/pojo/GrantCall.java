package com.polus.fibicomp.grantcall.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;

@Entity
@Table(name = "FIBI_GRANT_CALL_HEDAER")
public class GrantCall implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "grantCallIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "grantCallIdGenerator")
	@Column(name = "GRANT_HEADER_ID")
	private Integer grantCallId;

	@Column(name = "OPENING_DATE")
	private Date openingDate;

	@Column(name = "CLOSING_DATE")
	private Date closingDate;

	@Column(name = "NAME")
	private String grantCallName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "MAX_BUDGET")
	private Integer maximumBudget;

	@Column(name = "QUANTUM")
	private Integer quantum;

	@Column(name = "GRANT_THEME")
	private String grantTheme;

	@Column(name = "GRANT_TYPE_CODE")
	private Integer grantTypeCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_GRANT_CALL_HEDAER"), name = "GRANT_TYPE_CODE", referencedColumnName = "GRANT_TYPE_CODE", insertable = false, updatable = false)
	private GrantCallType grantCallType;

	@Column(name = "GRANT_STATUS_CODE")
	private Integer grantStatusCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_GRANT_CALL_HEDAER"), name = "GRANT_STATUS_CODE", referencedColumnName = "GRANT_STATUS_CODE", insertable = false, updatable = false)
	private GrantCallStatus grantCallStatus;

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK6_FIBI_GRANT_CALL_HEDAER"), name = "SPONSOR_CODE", referencedColumnName = "SPONSOR_CODE", insertable = false, updatable = false)
	private Sponsor sponsor;

	@Column(name = "SPONSOR_TYPE_CODE")
	private String sponsorTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK4_FIBI_GRANT_CALL_HEDAER"), name = "SPONSOR_TYPE_CODE", referencedColumnName = "SPONSOR_TYPE_CODE", insertable = false, updatable = false)
	private SponsorType sponsorType;

	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK5_FIBI_GRANT_CALL_HEDAER"), name = "ACTIVITY_TYPE_CODE", referencedColumnName = "ACTIVITY_TYPE_CODE", insertable = false, updatable = false)
	private ActivityType activityType;

	@Column(name = "FUNDING_SOURCE_TYPE_CODE")
	private String fundingSourceTypeCode;

	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK3_FIBI_GRANT_CALL_HEDAER"), name = "FUNDING_SOURCE_TYPE_CODE", referencedColumnName = "FUNDING_SOURCE_TYPE_CODE", insertable = false, updatable = false)
	private FundingSourceType fundingSourceType;

	@Column(name = "APPLICATION_PROCEDURE")
	private String applicationProcedure;

	@Column(name = "OTHER_INFORMATION")
	private String otherInformation;

	@Column(name = "EXTERNAL_URL")
	private String externalUrl;

	@JsonManagedReference
	@OneToMany(mappedBy = "grantCall", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<GrantCallKeyword> grantCallKeywords;

	@JsonManagedReference
	@OneToMany(mappedBy = "grantCall", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<GrantCallAttachment> grantCallAttachments;

	@JsonManagedReference
	@OneToMany(mappedBy = "grantCall", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<GrantCallContact> grantCallContacts;

	@JsonManagedReference
	@OneToMany(mappedBy = "grantCall", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<GrantCallResearchArea> grantCallResearchAreas;

	@JsonManagedReference
	@OneToMany(mappedBy = "grantCall", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<GrantCallEligibility> grantCallEligibilities;

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "HOME_UNIT_NUMBER")
	private String homeUnitNumber;

	@Column(name = "HOME_UNIT_NAME")
	private String homeUnitName;

	@Transient
	private String grantCallTypeDesc;

	@Transient
	private String grantCallStatusDesc;

	@Transient
	private String sponsorName;

	public GrantCall() {
		grantCallKeywords = new ArrayList<GrantCallKeyword>();
		grantCallAttachments = new ArrayList<GrantCallAttachment>();
		grantCallContacts = new ArrayList<GrantCallContact>();
		grantCallResearchAreas = new ArrayList<GrantCallResearchArea>();
		grantCallEligibilities = new ArrayList<GrantCallEligibility>();
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public String getGrantCallName() {
		return grantCallName;
	}

	public void setGrantCallName(String grantCallName) {
		this.grantCallName = grantCallName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMaximumBudget() {
		return maximumBudget;
	}

	public void setMaximumBudget(Integer maximumBudget) {
		this.maximumBudget = maximumBudget;
	}

	public Integer getQuantum() {
		return quantum;
	}

	public void setQuantum(Integer quantum) {
		this.quantum = quantum;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public Integer getGrantTypeCode() {
		return grantTypeCode;
	}

	public void setGrantTypeCode(Integer grantTypeCode) {
		this.grantTypeCode = grantTypeCode;
	}

	public String getGrantTheme() {
		return grantTheme;
	}

	public void setGrantTheme(String grantTheme) {
		this.grantTheme = grantTheme;
	}

	public GrantCallType getGrantCallType() {
		return grantCallType;
	}

	public void setGrantCallType(GrantCallType grantCallType) {
		this.grantCallType = grantCallType;
	}

	public Integer getGrantStatusCode() {
		return grantStatusCode;
	}

	public void setGrantStatusCode(Integer grantStatusCode) {
		this.grantStatusCode = grantStatusCode;
	}

	public GrantCallStatus getGrantCallStatus() {
		return grantCallStatus;
	}

	public void setGrantCallStatus(GrantCallStatus grantCallStatus) {
		this.grantCallStatus = grantCallStatus;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public String getSponsorTypeCode() {
		return sponsorTypeCode;
	}

	public void setSponsorTypeCode(String sponsorTypeCode) {
		this.sponsorTypeCode = sponsorTypeCode;
	}

	public SponsorType getSponsorType() {
		return sponsorType;
	}

	public void setSponsorType(SponsorType sponsorType) {
		this.sponsorType = sponsorType;
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

	public String getFundingSourceTypeCode() {
		return fundingSourceTypeCode;
	}

	public void setFundingSourceTypeCode(String fundingSourceTypeCode) {
		this.fundingSourceTypeCode = fundingSourceTypeCode;
	}

	public FundingSourceType getFundingSourceType() {
		return fundingSourceType;
	}

	public void setFundingSourceType(FundingSourceType fundingSourceType) {
		this.fundingSourceType = fundingSourceType;
	}

	public String getApplicationProcedure() {
		return applicationProcedure;
	}

	public void setApplicationProcedure(String applicationProcedure) {
		this.applicationProcedure = applicationProcedure;
	}

	public String getOtherInformation() {
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public List<GrantCallKeyword> getGrantCallKeywords() {
		return grantCallKeywords;
	}

	public void setGrantCallKeywords(List<GrantCallKeyword> grantCallKeywords) {
		this.grantCallKeywords = grantCallKeywords;
	}

	public List<GrantCallAttachment> getGrantCallAttachments() {
		return grantCallAttachments;
	}

	public void setGrantCallAttachments(List<GrantCallAttachment> grantCallAttachments) {
		this.grantCallAttachments = grantCallAttachments;
	}

	public List<GrantCallContact> getGrantCallContacts() {
		return grantCallContacts;
	}

	public void setGrantCallContacts(List<GrantCallContact> grantCallContacts) {
		this.grantCallContacts = grantCallContacts;
	}

	public List<GrantCallResearchArea> getGrantCallResearchAreas() {
		return grantCallResearchAreas;
	}

	public void setGrantCallResearchAreas(List<GrantCallResearchArea> grantCallResearchAreas) {
		this.grantCallResearchAreas = grantCallResearchAreas;
	}

	public List<GrantCallEligibility> getGrantCallEligibilities() {
		return grantCallEligibilities;
	}

	public void setGrantCallEligibilities(List<GrantCallEligibility> grantCallEligibilities) {
		this.grantCallEligibilities = grantCallEligibilities;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Date updateTimeStamp) {
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

	public String getGrantCallTypeDesc() {
		return grantCallTypeDesc;
	}

	public void setGrantCallTypeDesc(String grantCallTypeDesc) {
		this.grantCallTypeDesc = grantCallTypeDesc;
	}

	public String getGrantCallStatusDesc() {
		return grantCallStatusDesc;
	}

	public void setGrantCallStatusDesc(String grantCallStatusDesc) {
		this.grantCallStatusDesc = grantCallStatusDesc;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

}
