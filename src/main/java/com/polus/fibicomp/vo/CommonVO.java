package com.polus.fibicomp.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.pojo.ServiceRequest;

/**
 * Value object for service argument.
 *
 */
public class CommonVO {

	private String userName;

	private String password;

	private Integer pageNumber;

	private String sortBy;

	private String reverse;

	private String tabIndex;

	private String inputData;

	private String property1;

	private String property2;

	private String property3;

	private String property4;

	private Integer currentPage;

	private String personId;

	private String sponsorCode;

	private String pieChartIndex;

	private String researchSummaryIndex;

	private String donutChartIndex;

	private String awardId;

	private String accessToken;

	private String deviceToken;

	private String deviceType;

	private String awardNumber;

	private String selectedAwardNumber;

	private String documentNo;

	private String actionType;

	private String questionnaireId;

	private String comments;

	private List<HashMap<String, String>> answerList;

	private Date filterStartDate;

	private Date filterEndDate;

	private String unitNumber;

	private Boolean isUnitAdmin;

	private boolean isProvost;

	private boolean isReviewer;

	private ServiceRequest serviceRequest;

	private String fullName;

	private List<HashMap<String, Object>> categoryMap;

	private List<HashMap<String, Object>> typeMap;

	private List<HashMap<String, Object>> templateList;

	private Integer categoryCode;

	private Integer serviceTypeCode;

	private String successMsg;

	private List<HashMap<String, Object>> departmentList;

	private List<HashMap<String, Object>> userList;

	private Integer moduleCode;

	private String moduleItemKey;

	private Integer ostprojectId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public String getProperty3() {
		return property3;
	}

	public void setProperty3(String property3) {
		this.property3 = property3;
	}

	public String getProperty4() {
		return property4;
	}

	public void setProperty4(String property4) {
		this.property4 = property4;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getPieChartIndex() {
		return pieChartIndex;
	}

	public void setPieChartIndex(String pieChartIndex) {
		this.pieChartIndex = pieChartIndex;
	}

	public String getResearchSummaryIndex() {
		return researchSummaryIndex;
	}

	public void setResearchSummaryIndex(String researchSummaryIndex) {
		this.researchSummaryIndex = researchSummaryIndex;
	}

	public String getDonutChartIndex() {
		return donutChartIndex;
	}

	public void setDonutChartIndex(String donutChartIndex) {
		this.donutChartIndex = donutChartIndex;
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}

	public String getSelectedAwardNumber() {
		return selectedAwardNumber;
	}

	public void setSelectedAwardNumber(String selectedAwardNumber) {
		this.selectedAwardNumber = selectedAwardNumber;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<HashMap<String, String>> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<HashMap<String, String>> answerList) {
		this.answerList = answerList;
	}

	public Date getFilterEndDate() {
		return filterEndDate;
	}

	public void setFilterEndDate(Date filterEndDate) {
		this.filterEndDate = filterEndDate;
	}

	public Date getFilterStartDate() {
		return filterStartDate;
	}

	public void setFilterStartDate(Date filterStartDate) {
		this.filterStartDate = filterStartDate;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public Boolean getIsUnitAdmin() {
		return isUnitAdmin;
	}

	public void setIsUnitAdmin(Boolean isUnitAdmin) {
		this.isUnitAdmin = isUnitAdmin;
	}

	public boolean isProvost() {
		return isProvost;
	}

	public void setProvost(boolean isProvost) {
		this.isProvost = isProvost;
	}

	public boolean isReviewer() {
		return isReviewer;
	}

	public void setReviewer(boolean isReviewer) {
		this.isReviewer = isReviewer;
	}

	public ServiceRequest getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(ServiceRequest serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<HashMap<String, Object>> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(List<HashMap<String, Object>> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public List<HashMap<String, Object>> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(List<HashMap<String, Object>> typeMap) {
		this.typeMap = typeMap;
	}

	public List<HashMap<String, Object>> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<HashMap<String, Object>> templateList) {
		this.templateList = templateList;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(Integer serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public List<HashMap<String, Object>> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<HashMap<String, Object>> departmentList) {
		this.departmentList = departmentList;
	}

	public List<HashMap<String, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<HashMap<String, Object>> userList) {
		this.userList = userList;
	}

	public Integer getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(Integer moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleItemKey() {
		return moduleItemKey;
	}

	public void setModuleItemKey(String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
	}

	public Integer getOstprojectId() {
		return ostprojectId;
	}

	public void setOstprojectId(Integer ostprojectId) {
		this.ostprojectId = ostprojectId;
	}

}
