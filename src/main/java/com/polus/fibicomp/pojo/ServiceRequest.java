package com.polus.fibicomp.pojo;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class ServiceRequest {

	private Integer serviceRequestId;

	private Integer statusCode;

	private String status;

	private String serviceType;

	private Integer serviceTypeCode;

	private Integer prevServiceTypeCode;

	private Integer priorityId;

	private String unitNumber;

	private String priority;

	private String unitName;

	private String summary;

	private String description;

	private String reporterPersonId;

	private String reporterUserName;

	private String reporterName;

	private String previousReporterId;

	private String previousReporterName;

	private String createTimestamp;

	private String createUser;

	private String updateTimestamp;

	private String updateUser;

	private String assigneePersonId;

	private String assigneePersonName;

	private String previousAsgnPersonId;

	private String previousAsgnPersonName;

	private String contractAdminPersonId;

	private String contractAdminName;

	private String contractAdminUserName;

	private Integer caReviewStatusCode;

	private Integer ostCategoryCode;

	private String ostCategory;

	private Boolean fdpFlag;

	private Date arrivalDate;

	private Boolean negotiationFlag;

	private String subject;

	private String sendToTeamDate;

	private String statusComment;

	private Integer negotiatorId;

	private String negotiatorName;

	private String acType;

	private String holdInterval;

	private String ospTime;

	private String dlcTime;

	private String reviewPersonId;

	private String reviewPersonName;

	private Boolean isSubmittedOnce;

	private Integer workFlowTaskCode;

	private String workFlowTaskName;

	private java.sql.Timestamp sqlUpdateDate;

	private List<HashMap<String, Object>> serviceList;

	public Integer getServiceRequestId() {
		return serviceRequestId;
	}

	public void setServiceRequestId(Integer serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(Integer serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public Integer getPrevServiceTypeCode() {
		return prevServiceTypeCode;
	}

	public void setPrevServiceTypeCode(Integer prevServiceTypeCode) {
		this.prevServiceTypeCode = prevServiceTypeCode;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReporterPersonId() {
		return reporterPersonId;
	}

	public void setReporterPersonId(String reporterPersonId) {
		this.reporterPersonId = reporterPersonId;
	}

	public String getReporterUserName() {
		return reporterUserName;
	}

	public void setReporterUserName(String reporterUserName) {
		this.reporterUserName = reporterUserName;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getPreviousReporterId() {
		return previousReporterId;
	}

	public void setPreviousReporterId(String previousReporterId) {
		this.previousReporterId = previousReporterId;
	}

	public String getPreviousReporterName() {
		return previousReporterName;
	}

	public void setPreviousReporterName(String previousReporterName) {
		this.previousReporterName = previousReporterName;
	}

	public String getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getAssigneePersonId() {
		return assigneePersonId;
	}

	public void setAssigneePersonId(String assigneePersonId) {
		this.assigneePersonId = assigneePersonId;
	}

	public String getAssigneePersonName() {
		return assigneePersonName;
	}

	public void setAssigneePersonName(String assigneePersonName) {
		this.assigneePersonName = assigneePersonName;
	}

	public String getPreviousAsgnPersonId() {
		return previousAsgnPersonId;
	}

	public void setPreviousAsgnPersonId(String previousAsgnPersonId) {
		this.previousAsgnPersonId = previousAsgnPersonId;
	}

	public String getPreviousAsgnPersonName() {
		return previousAsgnPersonName;
	}

	public void setPreviousAsgnPersonName(String previousAsgnPersonName) {
		this.previousAsgnPersonName = previousAsgnPersonName;
	}

	public String getContractAdminPersonId() {
		return contractAdminPersonId;
	}

	public void setContractAdminPersonId(String contractAdminPersonId) {
		this.contractAdminPersonId = contractAdminPersonId;
	}

	public String getContractAdminName() {
		return contractAdminName;
	}

	public void setContractAdminName(String contractAdminName) {
		this.contractAdminName = contractAdminName;
	}

	public String getContractAdminUserName() {
		return contractAdminUserName;
	}

	public void setContractAdminUserName(String contractAdminUserName) {
		this.contractAdminUserName = contractAdminUserName;
	}

	public Integer getCaReviewStatusCode() {
		return caReviewStatusCode;
	}

	public void setCaReviewStatusCode(Integer caReviewStatusCode) {
		this.caReviewStatusCode = caReviewStatusCode;
	}

	public Integer getOstCategoryCode() {
		return ostCategoryCode;
	}

	public void setOstCategoryCode(Integer ostCategoryCode) {
		this.ostCategoryCode = ostCategoryCode;
	}

	public String getOstCategory() {
		return ostCategory;
	}

	public void setOstCategory(String ostCategory) {
		this.ostCategory = ostCategory;
	}

	public Boolean getFdpFlag() {
		return fdpFlag;
	}

	public void setFdpFlag(Boolean fdpFlag) {
		this.fdpFlag = fdpFlag;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Boolean getNegotiationFlag() {
		return negotiationFlag;
	}

	public void setNegotiationFlag(Boolean negotiationFlag) {
		this.negotiationFlag = negotiationFlag;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSendToTeamDate() {
		return sendToTeamDate;
	}

	public void setSendToTeamDate(String sendToTeamDate) {
		this.sendToTeamDate = sendToTeamDate;
	}

	public String getStatusComment() {
		return statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	public Integer getNegotiatorId() {
		return negotiatorId;
	}

	public void setNegotiatorId(Integer negotiatorId) {
		this.negotiatorId = negotiatorId;
	}

	public String getNegotiatorName() {
		return negotiatorName;
	}

	public void setNegotiatorName(String negotiatorName) {
		this.negotiatorName = negotiatorName;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getHoldInterval() {
		return holdInterval;
	}

	public void setHoldInterval(String holdInterval) {
		this.holdInterval = holdInterval;
	}

	public String getOspTime() {
		return ospTime;
	}

	public void setOspTime(String ospTime) {
		this.ospTime = ospTime;
	}

	public String getDlcTime() {
		return dlcTime;
	}

	public void setDlcTime(String dlcTime) {
		this.dlcTime = dlcTime;
	}

	public String getReviewPersonId() {
		return reviewPersonId;
	}

	public void setReviewPersonId(String reviewPersonId) {
		this.reviewPersonId = reviewPersonId;
	}

	public String getReviewPersonName() {
		return reviewPersonName;
	}

	public void setReviewPersonName(String reviewPersonName) {
		this.reviewPersonName = reviewPersonName;
	}

	public Boolean getIsSubmittedOnce() {
		return isSubmittedOnce;
	}

	public void setIsSubmittedOnce(Boolean isSubmittedOnce) {
		this.isSubmittedOnce = isSubmittedOnce;
	}

	public Integer getWorkFlowTaskCode() {
		return workFlowTaskCode;
	}

	public void setWorkFlowTaskCode(Integer workFlowTaskCode) {
		this.workFlowTaskCode = workFlowTaskCode;
	}

	public String getWorkFlowTaskName() {
		return workFlowTaskName;
	}

	public void setWorkFlowTaskName(String workFlowTaskName) {
		this.workFlowTaskName = workFlowTaskName;
	}

	public java.sql.Timestamp getSqlUpdateDate() {
		return sqlUpdateDate;
	}

	public void setSqlUpdateDate(java.sql.Timestamp sqlUpdateDate) {
		this.sqlUpdateDate = sqlUpdateDate;
	}

	public List<HashMap<String, Object>> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<HashMap<String, Object>> serviceList) {
		this.serviceList = serviceList;
	}

}
