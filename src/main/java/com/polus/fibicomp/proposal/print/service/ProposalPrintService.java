package com.polus.fibicomp.proposal.print.service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;

@Transactional
@Service(value = "proposalPrintService")
public interface ProposalPrintService {

	/**
	 * This method is used to generate proposal in PDF format.
	 * @param proposalId - Id of the proposal.
	 * @return - Proposal in PDF format.
	 * @throws DocumentException, ParseException
	 */
	public ByteArrayInputStream proposalPdfReport(Integer proposalId) throws DocumentException, ParseException;

	/**
	 * This method is used to generate budget in PDF format.
	 * @param proposalId - Id of the proposal.
	 * @return - budget in PDF format.
	 * @throws DocumentException, ParseException
	 */
	public ByteArrayInputStream proposalBudgetPdfReport(Integer proposalId) throws DocumentException, ParseException;

}
