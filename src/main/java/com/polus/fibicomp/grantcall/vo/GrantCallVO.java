package com.polus.fibicomp.grantcall.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachType;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachment;
import com.polus.fibicomp.grantcall.pojo.GrantCallCriteria;
import com.polus.fibicomp.grantcall.pojo.GrantCallEligibilityType;
import com.polus.fibicomp.grantcall.pojo.GrantCallStatus;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;
import com.polus.fibicomp.pojo.Unit;

public class GrantCallVO {

	private Integer grantCallId;

	private Integer grantKeywordId;

	private List<GrantCallType> grantCallTypes;

	private List<GrantCallStatus> grantCallStatus;

	private List<ResearchArea> researchAreas;

	private List<ScienceKeyword> scienceKeywords;

	private List<SponsorType> sponsorTypes;

	private List<Sponsor> sponsors;

	private List<ActivityType> activityTypes;

	private List<FundingSourceType> fundingSourceTypes;

	private List<GrantCallCriteria> grantCallCriterias;

	private List<GrantCallEligibilityType> grantCallEligibilityTypes;

	private List<GrantCallAttachType> grantCallAttachTypes;

	private GrantCall grantCall;

	private String sponsorTypeCode;

	private Boolean status;

	private String message;

	private String updateType;

	private Integer grantContactId;

	private Integer grantResearchAreaId;

	private Integer grantEligibilityId;

	private Integer attachmentId;

	private Integer grantStatusCode;

	private GrantCallStatus grantStatus;

	private GrantCallAttachment newAttachment;

	private List<Unit> homeUnits;

	public GrantCallVO() {
		grantCall = new GrantCall();
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Integer getGrantKeywordId() {
		return grantKeywordId;
	}

	public void setGrantKeywordId(Integer grantKeywordId) {
		this.grantKeywordId = grantKeywordId;
	}

	public List<GrantCallType> getGrantCallTypes() {
		return grantCallTypes;
	}

	public void setGrantCallTypes(List<GrantCallType> grantCallTypes) {
		this.grantCallTypes = grantCallTypes;
	}

	public List<GrantCallStatus> getGrantCallStatus() {
		return grantCallStatus;
	}

	public void setGrantCallStatus(List<GrantCallStatus> grantCallStatus) {
		this.grantCallStatus = grantCallStatus;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public List<ScienceKeyword> getScienceKeywords() {
		return scienceKeywords;
	}

	public void setScienceKeywords(List<ScienceKeyword> scienceKeywords) {
		this.scienceKeywords = scienceKeywords;
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

	public List<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(List<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public List<FundingSourceType> getFundingSourceTypes() {
		return fundingSourceTypes;
	}

	public void setFundingSourceTypes(List<FundingSourceType> fundingSourceTypes) {
		this.fundingSourceTypes = fundingSourceTypes;
	}

	public List<GrantCallCriteria> getGrantCallCriterias() {
		return grantCallCriterias;
	}

	public void setGrantCallCriterias(List<GrantCallCriteria> grantCallCriterias) {
		this.grantCallCriterias = grantCallCriterias;
	}

	public List<GrantCallEligibilityType> getGrantCallEligibilityTypes() {
		return grantCallEligibilityTypes;
	}

	public void setGrantCallEligibilityTypes(List<GrantCallEligibilityType> grantCallEligibilityTypes) {
		this.grantCallEligibilityTypes = grantCallEligibilityTypes;
	}

	public List<GrantCallAttachType> getGrantCallAttachTypes() {
		return grantCallAttachTypes;
	}

	public void setGrantCallAttachTypes(List<GrantCallAttachType> grantCallAttachTypes) {
		this.grantCallAttachTypes = grantCallAttachTypes;
	}

	public GrantCall getGrantCall() {
		return grantCall;
	}

	public void setGrantCall(GrantCall grantCall) {
		this.grantCall = grantCall;
	}

	public String getSponsorTypeCode() {
		return sponsorTypeCode;
	}

	public void setSponsorTypeCode(String sponsorTypeCode) {
		this.sponsorTypeCode = sponsorTypeCode;
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

	public Integer getGrantContactId() {
		return grantContactId;
	}

	public void setGrantContactId(Integer grantContactId) {
		this.grantContactId = grantContactId;
	}

	public Integer getGrantResearchAreaId() {
		return grantResearchAreaId;
	}

	public void setGrantResearchAreaId(Integer grantResearchAreaId) {
		this.grantResearchAreaId = grantResearchAreaId;
	}

	public Integer getGrantEligibilityId() {
		return grantEligibilityId;
	}

	public void setGrantEligibilityId(Integer grantEligibilityId) {
		this.grantEligibilityId = grantEligibilityId;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getGrantStatusCode() {
		return grantStatusCode;
	}

	public void setGrantStatusCode(Integer grantStatusCode) {
		this.grantStatusCode = grantStatusCode;
	}

	public GrantCallStatus getGrantStatus() {
		return grantStatus;
	}

	public void setGrantStatus(GrantCallStatus grantStatus) {
		this.grantStatus = grantStatus;
	}

	public GrantCallAttachment getNewAttachment() {
		return newAttachment;
	}

	public void setNewAttachment(GrantCallAttachment newAttachment) {
		this.newAttachment = newAttachment;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

}
