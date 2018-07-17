package com.polus.fibicomp.grantcall.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachType;
import com.polus.fibicomp.grantcall.pojo.GrantCallAttachment;
import com.polus.fibicomp.grantcall.pojo.GrantCallContact;
import com.polus.fibicomp.grantcall.pojo.GrantCallCriteria;
import com.polus.fibicomp.grantcall.pojo.GrantCallEligibility;
import com.polus.fibicomp.grantcall.pojo.GrantCallEligibilityType;
import com.polus.fibicomp.grantcall.pojo.GrantCallKeyword;
import com.polus.fibicomp.grantcall.pojo.GrantCallResearchArea;
import com.polus.fibicomp.grantcall.pojo.GrantCallStatus;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.grantcall.vo.GrantCallVO;
import com.polus.fibicomp.pojo.ActivityType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.pojo.Sponsor;
import com.polus.fibicomp.pojo.SponsorType;
import com.polus.fibicomp.pojo.Unit;

@Transactional
@Service(value = "grantCallService")
public class GrantCallServiceImpl implements GrantCallService {

	protected static Logger logger = Logger.getLogger(GrantCallServiceImpl.class.getName());

	@Autowired
	private GrantCallDao grantCallDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public String createGrantCall(GrantCallVO grantCallVO) {
		String sponsorTypeCode = grantCallVO.getGrantCall().getSponsorTypeCode();
		if (sponsorTypeCode != null && !sponsorTypeCode.isEmpty()) {
			List<Sponsor> sponsors = grantCallDao.fetchSponsorsBySponsorType(sponsorTypeCode);
			grantCallVO.setSponsors(sponsors);
		}
		List<GrantCallType> grantCallTypes = grantCallDao.fetchAllGrantCallTypes();
		grantCallVO.setGrantCallTypes(grantCallTypes);

		GrantCallStatus grantCallStatus = grantCallDao.fetchStatusByStatusCode(Constants.GRANT_CALL_STATUS_CODE_DRAFT);
		GrantCall grantCall = grantCallVO.getGrantCall();
		grantCall.setGrantStatusCode(Constants.GRANT_CALL_STATUS_CODE_DRAFT);
		grantCall.setGrantCallStatus(grantCallStatus);

		List<ScienceKeyword> scienceKeywords = grantCallDao.fetchAllScienceKeywords();
		grantCallVO.setScienceKeywords(scienceKeywords);

		List<SponsorType> sponsorTypes = grantCallDao.fetchAllSponsorTypes();
		grantCallVO.setSponsorTypes(sponsorTypes);

		List<ActivityType> activityTypes = grantCallDao.fetchAllResearchTypes();
		grantCallVO.setActivityTypes(activityTypes);

		List<FundingSourceType> fundingSourceTypes = grantCallDao.fetchAllFundingSourceTypes();
		grantCallVO.setFundingSourceTypes(fundingSourceTypes);

		List<GrantCallCriteria> grantCallCriterias = grantCallDao.fetchAllGrantCallCriteria();
		grantCallVO.setGrantCallCriterias(grantCallCriterias);

		List<GrantCallEligibilityType> grantCallEligibilityTypes = grantCallDao.fetchAllEligibilityTypes();
		grantCallVO.setGrantCallEligibilityTypes(grantCallEligibilityTypes);

		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		grantCallVO.setResearchAreas(researchAreas);

		List<GrantCallAttachType> grantCallAttachTypes = grantCallDao.fetchAllGrantCallAttachTypes();
		grantCallVO.setGrantCallAttachTypes(grantCallAttachTypes);

		List<Unit> units = committeeDao.fetchAllHomeUnits();
		grantCallVO.setHomeUnits(units);

		String response = committeeDao.convertObjectToJSON(grantCallVO);
		return response;
	}

	@Override
	public String loadGrantCallById(Integer grantCallId) {
		GrantCallVO grantCallVO = new GrantCallVO();
		GrantCall grantCall = grantCallDao.fetchGrantCallById(grantCallId);
		grantCallVO.setGrantCall(grantCall);
		if (grantCall.getGrantStatusCode().equals(Constants.GRANT_CALL_STATUS_CODE_DRAFT)) {
			createGrantCall(grantCallVO);
		}
		String response = committeeDao.convertObjectToJSON(grantCallVO);
		return response;
	}

	@Override
	public String saveUpdateGrantCall(GrantCallVO vo) {
		GrantCall grantCall = vo.getGrantCall();
		grantCall = grantCallDao.saveOrUpdateGrantCall(grantCall);
		vo.setStatus(true);
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			vo.setMessage("Grant Call saved successfully");
		} else {
			vo.setMessage("Grant Call updated successfully");
		}
		vo.setGrantCall(grantCall);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String publishGrantCall(GrantCallVO vo) {
		GrantCall grantCall = vo.getGrantCall();
		grantCall.setGrantStatusCode(Constants.GRANT_CALL_STATUS_CODE_OPEN);
		GrantCallStatus grantCallStatus = grantCallDao.fetchStatusByStatusCode(Constants.GRANT_CALL_STATUS_CODE_OPEN);
		grantCall.setGrantCallStatus(grantCallStatus);
		grantCall = grantCallDao.saveOrUpdateGrantCall(grantCall);
		vo.setGrantCall(grantCall);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String fetchSponsorsBySponsorType(GrantCallVO vo) {
		List<Sponsor> sponsors = grantCallDao.fetchSponsorsBySponsorType(vo.getSponsorTypeCode());
		vo.setSponsors(sponsors);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String deleteGrantCallKeyword(GrantCallVO vo) {
		try {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(vo.getGrantCallId());
			List<GrantCallKeyword> list = grantCall.getGrantCallKeywords();
			List<GrantCallKeyword> updatedlist = new ArrayList<GrantCallKeyword>(list);
			Collections.copy(updatedlist, list);
			for (GrantCallKeyword grantCallKeyword : list) {
				if (grantCallKeyword.getGrantKeywordId().equals(vo.getGrantKeywordId())) {
					updatedlist.remove(grantCallKeyword);
				}
			}
			grantCall.getGrantCallKeywords().clear();
			grantCall.getGrantCallKeywords().addAll(updatedlist);
			grantCallDao.saveOrUpdateGrantCall(grantCall);
			vo.setGrantCall(grantCall);
			vo.setStatus(true);
			vo.setMessage("Grant call keyword deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting grant call keyword");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteGrantCallContact(GrantCallVO vo) {
		try {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(vo.getGrantCallId());
			List<GrantCallContact> list = grantCall.getGrantCallContacts();
			List<GrantCallContact> updatedlist = new ArrayList<GrantCallContact>(list);
			Collections.copy(updatedlist, list);
			for (GrantCallContact grantCallContact : list) {
				if (grantCallContact.getGrantContactId().equals(vo.getGrantContactId())) {
					updatedlist.remove(grantCallContact);
				}
			}
			grantCall.getGrantCallContacts().clear();
			grantCall.getGrantCallContacts().addAll(updatedlist);
			grantCallDao.saveOrUpdateGrantCall(grantCall);
			vo.setGrantCall(grantCall);
			vo.setStatus(true);
			vo.setMessage("Grant call contact deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting grant call contact");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteGrantCallAreaOfResearch(GrantCallVO vo) {
		try {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(vo.getGrantCallId());
			List<GrantCallResearchArea> list = grantCall.getGrantCallResearchAreas();
			List<GrantCallResearchArea> updatedlist = new ArrayList<GrantCallResearchArea>(list);
			Collections.copy(updatedlist, list);
			for (GrantCallResearchArea grantCallResearchArea : list) {
				if (grantCallResearchArea.getGrantResearchAreaId().equals(vo.getGrantResearchAreaId())) {
					updatedlist.remove(grantCallResearchArea);
				}
			}
			grantCall.getGrantCallResearchAreas().clear();
			grantCall.getGrantCallResearchAreas().addAll(updatedlist);
			grantCallDao.saveOrUpdateGrantCall(grantCall);
			vo.setGrantCall(grantCall);
			vo.setStatus(true);
			vo.setMessage("Grant call research area deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting grant call research area");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteGrantCallEligibility(GrantCallVO vo) {
		try {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(vo.getGrantCallId());
			List<GrantCallEligibility> list = grantCall.getGrantCallEligibilities();
			List<GrantCallEligibility> updatedlist = new ArrayList<GrantCallEligibility>(list);
			Collections.copy(updatedlist, list);
			for (GrantCallEligibility grantCallEligibility : list) {
				if (grantCallEligibility.getGrantEligibilityId().equals(vo.getGrantEligibilityId())) {
					updatedlist.remove(grantCallEligibility);
				}
			}
			grantCall.getGrantCallEligibilities().clear();
			grantCall.getGrantCallEligibilities().addAll(updatedlist);
			grantCallDao.saveOrUpdateGrantCall(grantCall);
			vo.setGrantCall(grantCall);
			vo.setStatus(true);
			vo.setMessage("Grant call eligibility deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting grant call eligibility");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteGrantCallAttachment(GrantCallVO vo) {
		try {
			GrantCall grantCall = grantCallDao.fetchGrantCallById(vo.getGrantCallId());
			List<GrantCallAttachment> list = grantCall.getGrantCallAttachments();
			List<GrantCallAttachment> updatedlist = new ArrayList<GrantCallAttachment>(list);
			Collections.copy(updatedlist, list);
			for (GrantCallAttachment grantCallAttachment : list) {
				if (grantCallAttachment.getAttachmentId().equals(vo.getAttachmentId())) {
					updatedlist.remove(grantCallAttachment);
				}
			}
			grantCall.getGrantCallAttachments().clear();
			grantCall.getGrantCallAttachments().addAll(updatedlist);
			grantCallDao.saveOrUpdateGrantCall(grantCall);
			vo.setGrantCall(grantCall);
			vo.setStatus(true);
			vo.setMessage("Grant call attachment deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting grant call attachment");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String addGrantCallAttachment(MultipartFile[] files, String formDataJSON) {
		GrantCallVO grantCallVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			grantCallVO = mapper.readValue(formDataJSON, GrantCallVO.class);
			GrantCall grantCall = grantCallVO.getGrantCall();
			GrantCallAttachment newAttachment = grantCallVO.getNewAttachment();
			List<GrantCallAttachment> grantCallAttachments = new ArrayList<GrantCallAttachment>();
			for (int i = 0; i < files.length; i++) {
				GrantCallAttachment grantCallAttachment = new GrantCallAttachment();
				grantCallAttachment.setGrantCallAttachType(newAttachment.getGrantCallAttachType());
				grantCallAttachment.setGrantAttachmentTypeCode(newAttachment.getGrantAttachmentTypeCode());
				grantCallAttachment.setDescription(newAttachment.getDescription());
				grantCallAttachment.setUpdateTimestamp(newAttachment.getUpdateTimestamp());
				grantCallAttachment.setUpdateUser(newAttachment.getUpdateUser());
				grantCallAttachment.setAttachment(files[i].getBytes());
				grantCallAttachment.setFileName(files[i].getOriginalFilename());
				grantCallAttachment.setMimeType(files[i].getContentType());
				grantCallAttachments.add(grantCallAttachment);
			}
			grantCall.getGrantCallAttachments().addAll(grantCallAttachments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(grantCallVO);
		return response;
	}

	@Override
	public ResponseEntity<byte[]> downloadGrantCallAttachment(Integer attachmentId) {
		GrantCallAttachment attachment = grantCallDao.fetchAttachmentById(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] data = attachment.getAttachment();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
			String filename = attachment.getFileName();
			headers.setContentDispositionFormData(filename, filename);
			headers.setContentLength(data.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentData;
	}

}
