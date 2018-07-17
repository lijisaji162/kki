package com.polus.fibicomp.dao;

import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.pojo.ServiceRequest;
import com.polus.fibicomp.vo.AwardDetailsVO;
import com.polus.fibicomp.vo.AwardHierarchyVO;
import com.polus.fibicomp.vo.AwardTermsAndReportsVO;
import com.polus.fibicomp.vo.CommitmentsVO;
import com.polus.fibicomp.vo.CommonVO;

public interface AwardDao {

	/**
	 * This method is to retrieve details regarding a corresponding award
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public AwardDetailsVO fetchAwardSummaryData(String awardId) throws Exception;

	/**
	 * This method is to retrieve award reports and terms details
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award reports and terms view
	 * @throws Exception
	 */
	public AwardTermsAndReportsVO fetchAwardReportsAndTerms(String awardId);

	/**
	 * This method is to retrieve award hierarchy details
	 * @param awardNumber - number of award
	 * @param selectedAwardNumber - selected award number from the hierarchy
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public AwardHierarchyVO fetchAwardHierarchyData(String awardNumber, String selectedAwardNumber);

	/**
	 * This method is to retrieve award commitments details
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public CommitmentsVO fetchAwardCommitmentsData(String awardId);

	/**
	 * This method is used to get all dropdown values for creating variation request.
	 * @param vo - Object of CommonVO.
	 * @return A set of dropdown values.
	 */
	public CommonVO getDropDownDatas(CommonVO vo);

	/**
	 * This method is used to fetch template description based on type.
	 * @param categoryCode - Category code
	 * @param serviceTypeCode - Type Code
	 * @return Template description.
	 */
	public List<HashMap<String, Object>> viewTemplate(Integer categoryCode, Integer serviceTypeCode);

	/**
	 * This method is used to generate next service request Id.
	 * @return Service request Id.
	 */
	public Integer generateServiceRequestId();

	/**
	 * This method is used to update service request.
	 * @param serviceRequest - Object of ServiceRequest.
	 * @param userName - Username of the logged in user.
	 * @param fullName - Fullname of the logged in user.
	 * @param isUnitAdmin - Flag to indicate is unit admin.
	 * @return A value to indicate successful updation.
	 * @throws Exception
	 */
	public Integer updateServiceRequest(ServiceRequest serviceRequest, String userName, String fullName,
			Boolean isUnitAdmin) throws Exception;

	/**
	 * This method is used to generate log action Id.
	 * @param serviceRequest - Object of ServiceRequest.
	 * @param actionCode - Action code.
	 * @param userName - Username of the logged in user.
	 * @return Log action Id.
	 * @throws Exception
	 */
	public Integer generateAndLogActionId(ServiceRequest serviceRequest, Integer actionCode, String userName)
			throws Exception;

	/**
	 * This method is used to update process flow.
	 * @param serviceRequestId - Id of service request.
	 * @param actionLogId
	 * @param statusCode
	 * @param roleType
	 * @param returnToMeForReview 
	 * @param userName
	 * @throws Exception
	 */
	public void updateProcessFlow(Integer serviceRequestId, Integer actionLogId, Integer statusCode, Integer roleType,
			String returnToMeForReview, String userName) throws Exception;

	/**
	 * This method is used to get details of service request.
	 * @param serviceRequestId
	 * @return Details of service request.
	 */
	public List<HashMap<String, Object>> getServiceDetails(Integer serviceRequestId);

	/**
	 * This method is used to fetch contract admin detail.
	 * @param unitNumber
	 * @return contract admin detail.
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getCADetails(String unitNumber) throws Exception;

	/**
	 * This method is used to update project detail.
	 * @param serviceRequestId - service request Id.
	 * @param moduleCode - module code of the project.
	 * @param moduleItemKey - project number.
	 * @param personId - logged in person Id.
	 * @param ostprojectId - Project Id
	 * @param acType - action Type
	 */
	public void updateOSTProject(Integer serviceRequestId, Integer moduleCode, String moduleItemKey, String personId,
			Integer ostprojectId, String acType);
}
