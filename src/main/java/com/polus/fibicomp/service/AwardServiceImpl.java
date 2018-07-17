package com.polus.fibicomp.service;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.dao.AwardDao;
import com.polus.fibicomp.pojo.ServiceRequest;
import com.polus.fibicomp.vo.AwardDetailsVO;
import com.polus.fibicomp.vo.AwardHierarchyVO;
import com.polus.fibicomp.vo.AwardTermsAndReportsVO;
import com.polus.fibicomp.vo.CommitmentsVO;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "awardService")
public class AwardServiceImpl implements AwardService {

	protected static Logger logger = Logger.getLogger(AwardServiceImpl.class.getName());

	@Autowired
	private AwardDao awardDao;

	@Override
	public String getAwardSummaryData(String awardId) throws Exception {
		AwardDetailsVO awardDetailsVO = awardDao.fetchAwardSummaryData(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardDetailsVO);
	}

	@Override
	public String getAwardReportsAndTerms(String awardId) throws Exception {
		AwardTermsAndReportsVO awardTermsAndReportsVO = awardDao.fetchAwardReportsAndTerms(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardTermsAndReportsVO);
	}

	@Override
	public String getAwardHierarchyData(String awardNumber, String selectedAwardNumber) throws Exception {
		AwardHierarchyVO awardHierarchyVO = awardDao.fetchAwardHierarchyData(awardNumber, selectedAwardNumber);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardHierarchyVO);
	}

	@Override
	public String getAwardCommitmentsData(String awardId) throws Exception {
		CommitmentsVO commitmentsVO = awardDao.fetchAwardCommitmentsData(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(commitmentsVO);
	}

	@Override
	public String createProjectVariationRequest(CommonVO vo) throws Exception {
		ServiceRequest serviceRequest = vo.getServiceRequest();
		if (serviceRequest.getServiceRequestId() == null) {
			serviceRequest.setAcType("I");
			serviceRequest.setStatusCode(Constants.STATUS_AT_DLC_NOT_SUBMITTED_CODE);
			serviceRequest.setStatus(Constants.STATUS_AT_DLC_NOT_SUBMITTED);
			serviceRequest.setIsSubmittedOnce(false);
			serviceRequest.setReporterPersonId(vo.getPersonId());
			serviceRequest.setReporterUserName(vo.getUserName());
			serviceRequest.setReporterName(vo.getFullName());
		}
		vo = awardDao.getDropDownDatas(vo);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(vo);
	}

	@Override
	public String viewTemplate(CommonVO vo) throws Exception {
		vo.setTemplateList(awardDao.viewTemplate(vo.getCategoryCode(), vo.getServiceTypeCode()));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(vo);
	}

	public String submitOSTDetails(CommonVO vo) throws Exception {
		ServiceRequest serviceRequest = vo.getServiceRequest();
		serviceRequest.setStatusCode(Constants.STATUS_AT_OSP_CODE);
		if (serviceRequest.getOstCategoryCode() == Constants.POPS_CATEGORY_CODE) {
			serviceRequest.setStatusCode(Constants.STATUS_AT_POPS_CODE);
		}
		if (serviceRequest.getServiceRequestId() == null) {
			Integer serviceRequestId = awardDao.generateServiceRequestId();
			serviceRequest.setServiceRequestId(serviceRequestId);
		}
		if (serviceRequest.getIsSubmittedOnce() != true
				&& serviceRequest.getOstCategoryCode() == Constants.POPS_CATEGORY_CODE) {
			serviceRequest.setAssigneePersonId(null);
			serviceRequest.setAssigneePersonName(null);
		}
		Integer successCode = awardDao.updateServiceRequest(serviceRequest, vo.getUserName(), vo.getFullName(),
				vo.getIsUnitAdmin());
		if (successCode == 20100) {
			vo.setSuccessMsg("Data changed between retrieval and update. Do you want to update with the latest?");
		} else {
			if (serviceRequest.getServiceRequestId() != null) {
				awardDao.updateOSTProject(serviceRequest.getServiceRequestId(), vo.getModuleCode(),
						vo.getModuleItemKey(), vo.getPersonId(), vo.getOstprojectId(), serviceRequest.getAcType());
			}
			Integer actionLogId = awardDao.generateAndLogActionId(serviceRequest, Constants.ACTION_SUBMIT,
					vo.getUserName());
			awardDao.updateProcessFlow(serviceRequest.getServiceRequestId(), actionLogId, Constants.STATUS_AT_OSP_CODE,
					null, null, vo.getUserName());
			getServiceRequest(serviceRequest.getServiceRequestId(), vo);
			vo.setSuccessMsg("Successfully submitted service request.");
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(vo);
	}

	public String getServiceRequest(Integer serviceRequestId, CommonVO vo) throws Exception {
		try {
			ServiceRequest serviceRequest = new ServiceRequest();
			if (serviceRequestId == null) {
				serviceRequest.setAcType("I");
				serviceRequest.setStatusCode(Constants.STATUS_AT_DLC_NOT_SUBMITTED_CODE);
				serviceRequest.setStatus(Constants.STATUS_AT_DLC_NOT_SUBMITTED);
				serviceRequest.setIsSubmittedOnce(false);
				serviceRequest.setReporterPersonId(vo.getPersonId());
				serviceRequest.setReporterUserName(vo.getUserName());
				serviceRequest.setPriorityId(Constants.OST_PRIORITY_NORMAL);
				serviceRequest.setReporterName(vo.getFullName());
				if (vo.getIsUnitAdmin() == false) {
					serviceRequest.setUnitName(vo.getServiceRequest().getUnitName());
					serviceRequest.setUnitNumber(vo.getServiceRequest().getUnitNumber());
				}
			} else {
				serviceRequest.setServiceList(awardDao.getServiceDetails(serviceRequestId));
				serviceRequest.setAcType("U");
				vo.setServiceRequest(serviceRequest);
			}
		} catch (Exception e) {
			logger.error("Error in method getServiceRequest", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(vo);
	}

	@Override
	public String getContractAdmin(CommonVO vo) throws Exception {
		try {
			List<HashMap<String, Object>> userList = awardDao.getCADetails(vo.getUnitNumber());
			vo.setUserList(userList);
		} catch (Exception e) {
			logger.error("Error in method getContractAdmin", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(vo);
	}

}
