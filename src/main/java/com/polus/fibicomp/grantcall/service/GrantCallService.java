package com.polus.fibicomp.grantcall.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.grantcall.vo.GrantCallVO;

@Service
public interface GrantCallService {

	/**
	 * This method is used to create a grant call.
	 * @param vo - Object of GrantCallVO class.
	 * @return vo object to create grant call.
	 */
	public String createGrantCall(GrantCallVO vo);

	/**
	 * This method is used to load grant call by id.
	 * @param grantCallId - Id of the grant call.
	 * @return An object of grant call.
	 */
	public String loadGrantCallById(Integer grantCallId);

	/**
	 * This method is used to save or update grant call.
	 * @param vo - Object of GrantCallVO class.
	 * @return set of values to figure out details about a grant call.
	 */
	public String saveUpdateGrantCall(GrantCallVO vo);

	/**
	 * This method is used to publish grant call.
	 * @param vo - Object of GrantCallVO class.
	 * @return set of values to figure out details about a grant call.
	 */
	public String publishGrantCall(GrantCallVO vo);

	/**
	 * This method is used to fetch sponsors based on sponsor type.
	 * @param vo - Object of GrantCallVO class.
	 * @return A list of sponsors.
	 */
	public String fetchSponsorsBySponsorType(GrantCallVO vo);

	/**
	 * This method is used to delete keywords from grant call.
	 * @param vo - Object of GrantCallVO class. 
	 * @return a String of details of grant call with updated list of keywords.
	 */
	public String deleteGrantCallKeyword(GrantCallVO vo);

	/**
	 * This method is used to delete point of contacts from grant call.
	 * @param vo - Object of GrantCallVO class. 
	 * @return a String of details of grant call with updated list of grant call contacts.
	 */
	public String deleteGrantCallContact(GrantCallVO vo);

	/**
	 * This method is used to delete area of research from grant call.
	 * @param vo - Object of GrantCallVO class. 
	 * @return a String of details of grant call with updated list of area of research.
	 */
	public String deleteGrantCallAreaOfResearch(GrantCallVO vo);

	/**
	 * This method is used to delete eligibility from grant call.
	 * @param vo - Object of GrantCallVO class. 
	 * @return a String of details of grant call with updated list of grant call eligibility.
	 */
	public String deleteGrantCallEligibility(GrantCallVO voo);

	/**
	 * This method is used to delete attachment from grant call.
	 * @param vo - Object of GrantCallVO class. 
	 * @return a String of details of grant call with updated list of grant call attachment.
	 */
	public String deleteGrantCallAttachment(GrantCallVO vo);

	/**
	 * This method is used to add grant call attachment.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the attachment.
	 * @return a String of details of grant call data with list of attachments.
	 */
	public String addGrantCallAttachment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to download grant call attachment.
	 * @param attachmentId - Id of the attachment to download.
	 * @return attachmentData.
	 */
	public ResponseEntity<byte[]> downloadGrantCallAttachment(Integer attachmentId);

}
