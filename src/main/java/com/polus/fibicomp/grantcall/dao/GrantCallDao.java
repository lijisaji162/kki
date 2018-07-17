package com.polus.fibicomp.grantcall.dao;

import java.util.List;

import org.springframework.stereotype.Service;

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

@Service
public interface GrantCallDao {

	/**
	 * This method is used to fetch all grant call types.
	 * @return A list of grant call types.
	 */
	public List<GrantCallType> fetchAllGrantCallTypes();

	/**
	 * This method is used to fetch all grant call status.
	 * @return A list of grant call status.
	 */
	public List<GrantCallStatus> fetchAllGrantCallStatus();

	/**
	 * This method is used to fetch all science keywords.
	 * @return A list of science keywords.
	 */
	public List<ScienceKeyword> fetchAllScienceKeywords();

	/**
	 * This method is used to fetch all sponsor types.
	 * @return A list of sponsor types.
	 */
	public List<SponsorType> fetchAllSponsorTypes();

	/**
	 * This method is used to fetch sponsors based on sponsor type.
	 * @param sponsorTypeCode - type code of sponsor.
	 * @return A list of sponsors corresponding to the type.
	 */
	public List<Sponsor> fetchSponsorsBySponsorType(String sponsorTypeCode);

	/**
	 * This method is used to fetch all activity types.
	 * @return A list of activity type.
	 */
	public List<ActivityType> fetchAllResearchTypes();

	/**
	 * This method is used to fetch all funding source types.
	 * @return A list of funding source types.
	 */
	public List<FundingSourceType> fetchAllFundingSourceTypes();

	/**
	 * This method is used to fetch all grant call criteria.
	 * @return A list of grant call criteria.
	 */
	public List<GrantCallCriteria> fetchAllGrantCallCriteria();

	/**
	 * This method is used to fetch all eligibility types.
	 * @return A list of eligibility types.
	 */
	public List<GrantCallEligibilityType> fetchAllEligibilityTypes();

	/**
	 * This method is used to fetch all grant call attachment types.
	 * @return A list of grant call attachment types.
	 */
	public List<GrantCallAttachType> fetchAllGrantCallAttachTypes();

	/**
	 * This method is used to fetch grant call by Id.
	 * @param grantCallId - Id of the grant call.
	 * @return An object of grant call.
	 */
	public GrantCall fetchGrantCallById(Integer grantCallId);

	/**
	 * This method is used to save or update grant call.
	 * @param grantCall - Object of GrantCall.
	 * @return set of values to figure out details about a grant call.
	 */
	public GrantCall saveOrUpdateGrantCall(GrantCall grantCall);

	/**
	 * This method is used to fetch grant call status by status code.
	 * @param grantStatusCode - Status code of grant call status.
	 * @return An object of grant call status.
	 */
	public GrantCallStatus fetchStatusByStatusCode(Integer grantStatusCode);

	/**
	 * This method is used to fetch attachment by Id.
	 * @param attachmentId - Id of the attachment.
	 * @return an object of GrantCallAttachment.
	 */
	public GrantCallAttachment fetchAttachmentById(Integer attachmentId);

	/**
	 * This method is used to fetch grantCallType by grantcallTypeCode.
	 * @param grantTypeCode - Id of the grantCallType.
	 * @return an object of GrantCallType.
	 */
	public GrantCallType fetchGrantCallTypeByGrantTypeCode(Integer grantTypeCode);

}
