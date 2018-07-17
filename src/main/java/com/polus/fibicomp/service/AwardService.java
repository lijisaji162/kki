package com.polus.fibicomp.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.vo.CommonVO;

@Service
public interface AwardService {

	/**
	 * This method is used to retrieve award data for award view.
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about an award
	 * @throws Exception
	 */
	public String getAwardSummaryData(String awardId) throws Exception;

	/**
	 * This method is used to retrieve award reports and terms.
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about an award
	 * @throws Exception
	 */
	public String getAwardReportsAndTerms(String awardId) throws Exception;

	/**
	 * This method is used to retrieve award hierarchy data.
	 * @param awardNumber
	 * @return set of values to figure out hierarchy details about an award
	 * @throws Exception
	 */
	public String getAwardHierarchyData(String awardNumber, String selectedAwardNumber) throws Exception;

	/**
	 * This method is used to retrieve award commitments data.
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about award commitments
	 * @throws Exception
	 */
	public String getAwardCommitmentsData(String awardId) throws Exception;

	/**
	 * This method is used to create project variation request.
	 * @param vo - Object of CommonVO.
	 * @return A set of values to create variation request
	 * @throws Exception
	 */
	public String createProjectVariationRequest(CommonVO vo) throws Exception;

	/**
	 * This method is used to fetch template description based on type.
	 * @param vo - Object of CommonVO.
	 * @return Template description.
	 * @throws Exception
	 */
	public String viewTemplate(CommonVO vo) throws Exception;

	/**
	 * This method is used to fetch contract admin.
	 * @param vo - Object of CommonVO.
	 * @return A set of values of contract admin.
	 * @throws Exception
	 */
	public String getContractAdmin(CommonVO vo) throws Exception;

	/**
	 * This method is used to submit project variation request.
	 * @param vo - Object of CommonVO.
	 * @return A set of values of variation request.
	 * @throws Exception
	 */
	public String submitOSTDetails(CommonVO vo) throws Exception;
}
