package com.polus.fibicomp.proposal.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;
import com.polus.fibicomp.budget.service.BudgetService;
import com.polus.fibicomp.proposal.print.service.ProposalPrintService;
import com.polus.fibicomp.proposal.service.ProposalService;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.vo.SponsorSearchResult;

@RestController
public class ProposalController {

	protected static Logger logger = Logger.getLogger(ProposalController.class.getName());

	@Autowired
	@Qualifier(value = "proposalService")
	private ProposalService proposalService;

	@Autowired
	@Qualifier(value = "budgetService")
	private BudgetService budgetService;

	@Autowired
	@Qualifier(value = "proposalPrintService")
	private ProposalPrintService proposalPrintService;

	@RequestMapping(value = "/createProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createProposal");
		return proposalService.createProposal(vo);
	}

	@RequestMapping(value = "/addProposalAttachment", method = RequestMethod.POST)
	public String addProposalAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addProposalAttachment");
		return proposalService.addProposalAttachment(files, formDataJson);
	}

	@RequestMapping(value = "/saveOrUpdateProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveOrUpdateProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveOrUpdateProposal");
		return proposalService.saveOrUpdateProposal(vo);
	}

	@RequestMapping(value = "/loadProposalById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadProposalById(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadProposalById");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("personId : " + vo.getPersonId());
		return proposalService.loadProposalById(vo.getProposalId(), vo.getPersonId());
	}

	@RequestMapping(value = "/fetchCostElementByBudgetCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchCostElementByBudgetCategory(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchCostElementByBudgetCategory");
		logger.info("BudgetCategoryCode : " + vo.getBudgetCategoryCode());
		return proposalService.fetchCostElementByBudgetCategory(vo);
	}

	@RequestMapping(value = "/fetchAllAreaOfExcellence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchAllAreaOfExcellence(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchAllAreaOfExcellence");
		return proposalService.fetchAllAreaOfExcellence(vo);
	}

	@RequestMapping(value = "/deleteProposalKeyword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalKeyword(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalKeyword");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("KeywordId : " + vo.getKeywordId());
		return proposalService.deleteProposalKeyword(vo);
	}

	@RequestMapping(value = "/deleteProposalResearchArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalResearchArea(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalResearchArea");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("researchAreaId : " + vo.getResearchAreaId());
		return proposalService.deleteProposalResearchArea(vo);
	}

	@RequestMapping(value = "/deleteProposalPerson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalPerson(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalPerson");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("proposalPersonId : " + vo.getProposalPersonId());
		return proposalService.deleteProposalPerson(vo);
	}

	@RequestMapping(value = "/deleteProposalSponsor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalSponsor(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalSponsor");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("sponsorId : " + vo.getSponsorId());
		return proposalService.deleteProposalSponsor(vo);
	}

	@RequestMapping(value = "/deleteIrbProtocol", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteIrbProtocol(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteIrbProtocol");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("irbProtocolId : " + vo.getIrbProtocolId());
		return proposalService.deleteIrbProtocol(vo);
	}

	@RequestMapping(value = "/deleteProposalAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalAttachment(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalBudget");
		logger.info("propodalId : " + vo.getProposalId());
		logger.info("attachmentId : " + vo.getAttachmentId());
		return proposalService.deleteProposalAttachment(vo);
	}

	@RequestMapping(value = "/downloadProposalAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadProposalAttachment(HttpServletResponse response, @RequestHeader("attachmentId") String attachmentId) {
		logger.info("Requesting for downloadProposalAttachment");
		logger.info("attachmentId : " + attachmentId);
		Integer attachmentid = Integer.parseInt(attachmentId);
		return proposalService.downloadProposalAttachment(attachmentid);
	}

	@RequestMapping(value = "/submitProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String submitProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for submitProposal");
		return proposalService.submitProposal(vo);
	}

	@RequestMapping(value = "/approveOrRejectProposal", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String approveProposal(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for approveOrRejectProposal");
		return proposalService.approveOrRejectProposal(files, formDataJson);
	}

	@RequestMapping(value = "/createProposalBudget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createProposalBudget(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createProposalBudget");
		return budgetService.createProposalBudget(vo);
	}

	@RequestMapping(value = "/printProposalPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> proposalPdfReport(HttpServletResponse response,
			@RequestHeader(value = "proposalId", required = true) String proposalIdInput)
			throws IOException, DocumentException, ParseException {
		Integer proposalId = Integer.parseInt(proposalIdInput);
		logger.info("Requesting for printProposalPdfReport");
		logger.info("proposalId : " + proposalId);
		ByteArrayInputStream bis = proposalPrintService.proposalPdfReport(proposalId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=ProposalSummary.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/printBudgetPdfReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> budgetPdfReport(HttpServletResponse response,
			@RequestHeader(value = "proposalId", required = true) String proposalIdInput)
			throws IOException, DocumentException, ParseException {
		Integer proposalId = Integer.parseInt(proposalIdInput);
		logger.info("Requesting for printBudgetPdfReport");
		logger.info("proposalId : " + proposalId);
		ByteArrayInputStream bis = proposalPrintService.proposalBudgetPdfReport(proposalId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=BudgetSummary.pdf");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/deleteProposalSpecialReview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalSpecialReview(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalSpecialReview");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("proposalSpecialReviewId : " + vo.getProposalSpecialReviewId());
		return proposalService.deleteProposalSpecialReview(vo);
	}

	@RequestMapping(value = "/findSponsors", method = RequestMethod.GET)
	public List<SponsorSearchResult> getNext(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for findSponsors");
		logger.info("searchString : " + searchString);
		return proposalService.findSponsor(searchString);
	}

	@RequestMapping(value = "/copyProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String copyProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for copyProposal");
		return proposalService.copyProposal(vo);
	}

}
