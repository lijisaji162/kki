package com.polus.fibicomp.proposal.print.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.common.service.DateTimeService;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalPersonUnit;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;

@Transactional
@Service(value = "proposalPrintService")
public class ProposalPrintServiceImpl implements ProposalPrintService {

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	@Qualifier(value = "dateTimeService")
	private DateTimeService dateTimeService;

	@Override
	public ByteArrayInputStream proposalPdfReport(Integer proposalId) throws DocumentException, ParseException {
		Proposal pdfData = proposalDao.fetchProposalById(proposalId);
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();
		Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		headFont.setSize(10);
		Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);
		bodyFont.setSize(10);
		Font subHeadFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		subHeadFont.setColor(38, 166, 154);
		subHeadFont.setSize(15);
		LineSeparator ls = new LineSeparator();

		try {
			Paragraph pdfHeading = new Paragraph("Proposal Summary", subHeadFont);
			pdfHeading.setAlignment(Paragraph.ALIGN_CENTER);
			pdfHeading.setSpacingAfter(5f);
			document.add(pdfHeading);
			document.add(new Chunk(ls));
			document.add(new Paragraph("General Details", subHeadFont));
			document.add(new Chunk(ls));

			PdfPTable generalDetailsTable = new PdfPTable(2);
			generalDetailsTable.setWidthPercentage(100);
			generalDetailsTable.setWidths(new int[] { 6, 6 });
			generalDetailsTable.setSpacingAfter(5f);

			generalDetailsTable
					.addCell(getGeneralDetailsCell("Title: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(
					getGeneralDetailsCell(pdfData.getTitle(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell("Principal Investigator: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getPrincipalInvestigator(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(
					getGeneralDetailsCell("Lead Unit: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getHomeUnitName(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable
					.addCell(getGeneralDetailsCell("Category: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getActivityType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable
					.addCell(getGeneralDetailsCell("Type: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getProposalType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(
					getGeneralDetailsCell("Start Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(pdfData.getStartDate()).toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable
					.addCell(getGeneralDetailsCell("End Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(pdfData.getEndDate()).toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			document.add(generalDetailsTable);

			document.add(new Chunk(ls));
			document.add(new Paragraph("Project Personnel", subHeadFont));
			document.add(new Chunk(ls));

			PdfPTable personTable = new PdfPTable(4);
			personTable.setWidthPercentage(100);
			personTable.setWidths(new int[] { 3, 2, 2, 5 });
			personTable.setSpacingBefore(5f);
			personTable.setSpacingAfter(5f);

			personTable.addCell(getTableCell("Name", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("Role", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("% of Effort", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("Department", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

			for (ProposalPerson persons : pdfData.getProposalPersons()) {
				personTable.addCell(
						getTableCell(persons.getFullName(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				personTable.addCell(getTableCell(persons.getProposalPersonRole().getDescription(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				if (persons.getPercentageOfEffort() != null) {
					personTable.addCell(getTableCell(persons.getPercentageOfEffort().toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					personTable.addCell(getTableCell("", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
				}
				PdfPTable departmentsTable = new PdfPTable(1);
				departmentsTable.setWidthPercentage(100);
				departmentsTable.setWidths(new int[] { 12 });
				departmentsTable.setSpacingAfter(5f);
				if (!persons.getUnits().isEmpty()) {
					for ( ProposalPersonUnit unit : persons.getUnits()) {
						departmentsTable.addCell(getGeneralDetailsCell(unit.getUnit().getUnitDetail(), PdfPCell.ALIGN_MIDDLE,
								PdfPCell.ALIGN_CENTER, bodyFont));
					}
				} else {
					departmentsTable.addCell(getGeneralDetailsCell("", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
				}
				personTable.addCell(departmentsTable);
			}
			document.add(personTable);

			if (!pdfData.getProposalSponsors().isEmpty()) {
				document.add(new Chunk(ls));
				document.add(new Paragraph("Funding Support", subHeadFont));
				document.add(new Chunk(ls));

				PdfPTable sponsorTable = new PdfPTable(4);
				sponsorTable.setWidthPercentage(100);
				sponsorTable.setWidths(new int[] { 3, 3, 3, 3 });
				sponsorTable.setSpacingBefore(5f);
				sponsorTable.setSpacingAfter(5f);

				sponsorTable.addCell(getTableCell("Name", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				sponsorTable
						.addCell(getTableCell("Start Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				sponsorTable.addCell(getTableCell("End Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				sponsorTable.addCell(getTableCell("Amount", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

				for (ProposalSponsor sponsor : pdfData.getProposalSponsors()) {
					sponsorTable.addCell(getTableCell(sponsor.getSponsor().getSponsorName(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(dateTimeService.convertToSqlDate(sponsor.getStartDate()).toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(dateTimeService.convertToSqlDate(sponsor.getEndDate()).toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(sponsor.getAmount().toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
				}
				document.add(sponsorTable);
			}

			if (pdfData.getBudgetHeader() != null) {
				document.add(new Chunk(ls));
				document.add(new Paragraph("Budget Summary", subHeadFont));
				document.add(new Chunk(ls));

				PdfPTable budgetTable = new PdfPTable(5);
				budgetTable.setWidthPercentage(100);
				budgetTable.setWidths(new int[] { 3, 3, 2, 2, 2 });
				budgetTable.setSpacingBefore(10f);
				budgetTable.setSpacingAfter(5f);

				budgetTable.addCell(
						getTableCell("Period Start Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				budgetTable.addCell(
						getTableCell("Period End Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				budgetTable.addCell(getTableCell("Total Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				budgetTable
						.addCell(getTableCell("Direct Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				budgetTable
						.addCell(getTableCell("Indirect Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

				for (BudgetPeriod budgetPeriod : pdfData.getBudgetHeader().getBudgetPeriods()) {
					budgetTable.addCell(getTableCell(dateTimeService.convertToSqlDate(budgetPeriod.getStartDate()).toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					budgetTable.addCell(getTableCell(dateTimeService.convertToSqlDate(budgetPeriod.getEndDate()).toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					if (budgetPeriod.getTotalCost() == null) {
						budgetTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						budgetTable.addCell(getTableCell(budgetPeriod.getTotalCost().toString(), PdfPCell.ALIGN_MIDDLE,
								PdfPCell.ALIGN_CENTER, bodyFont));
					}
					if (budgetPeriod.getTotalDirectCost() == null) {
						budgetTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						budgetTable.addCell(getTableCell(budgetPeriod.getTotalDirectCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					}
					if (budgetPeriod.getTotalIndirectCost() == null) {
						budgetTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						budgetTable.addCell(getTableCell(budgetPeriod.getTotalIndirectCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					}
				}
				budgetTable.addCell(getTableCell("", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				budgetTable.addCell(getTableCell("Total:", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				if (pdfData.getBudgetHeader().getTotalCost() == null) {
					budgetTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					budgetTable.addCell(getTableCell(pdfData.getBudgetHeader().getTotalCost().toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				if (pdfData.getBudgetHeader().getTotalDirectCost() == null) {
					budgetTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					budgetTable.addCell(getTableCell(pdfData.getBudgetHeader().getTotalDirectCost().toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				if (pdfData.getBudgetHeader().getTotalIndirectCost() == null) {
					budgetTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					budgetTable.addCell(getTableCell(pdfData.getBudgetHeader().getTotalIndirectCost().toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				document.add(budgetTable);
			}
			document.close();
		} catch (DocumentException ex) {
			Logger.getLogger(ProposalPrintServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public ByteArrayInputStream proposalBudgetPdfReport(Integer proposalId) throws DocumentException, ParseException {
		Proposal budgetPdfData = proposalDao.fetchProposalById(proposalId);
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();
		Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		headFont.setSize(10);
		Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);
		bodyFont.setSize(10);
		Font subHeadFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		subHeadFont.setColor(38, 166, 154);
		subHeadFont.setSize(15);
		LineSeparator ls = new LineSeparator();

		try {
			Paragraph pdfHeading = new Paragraph("Budget Summary", subHeadFont);
			pdfHeading.setAlignment(Paragraph.ALIGN_CENTER);
			pdfHeading.setSpacingAfter(5f);
			document.add(pdfHeading);
			document.add(new Chunk(ls));
			document.add(new Paragraph("Proposal Overview", subHeadFont));
			document.add(new Chunk(ls));

			PdfPTable proposalDetailsTable = new PdfPTable(2);
			proposalDetailsTable.setWidthPercentage(100);
			proposalDetailsTable.setWidths(new int[] { 6, 6 });
			proposalDetailsTable.setSpacingAfter(5f);

			proposalDetailsTable
					.addCell(getGeneralDetailsCell("Title: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getTitle(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell("Principal Investigator: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getPrincipalInvestigator(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Lead Unit: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getHomeUnitName(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable
					.addCell(getGeneralDetailsCell("Category: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getActivityType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable
					.addCell(getGeneralDetailsCell("Type: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getProposalType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Start Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetPdfData.getStartDate()).toString(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable
					.addCell(getGeneralDetailsCell("End Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetPdfData.getEndDate()).toString(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			document.add(proposalDetailsTable);

			document.add(new Chunk(ls));
			document.add(new Paragraph("Budget Overview", subHeadFont));
			document.add(new Chunk(ls));

			BudgetHeader budgetHeader = budgetPdfData.getBudgetHeader();
			if (budgetHeader != null) {
				PdfPTable budgetDetailsTable = new PdfPTable(2);
				budgetDetailsTable.setWidthPercentage(100);
				budgetDetailsTable.setWidths(new int[] { 6, 6 });
				budgetDetailsTable.setSpacingAfter(5f);

				budgetDetailsTable.addCell(
						getGeneralDetailsCell("Start Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				budgetDetailsTable
						.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetHeader.getStartDate()).toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				budgetDetailsTable.addCell(
						getGeneralDetailsCell("End Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				budgetDetailsTable
						.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetHeader.getEndDate()).toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				budgetDetailsTable.addCell(
						getGeneralDetailsCell("Direct Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				if (budgetHeader.getTotalDirectCost() == null) {
					budgetDetailsTable.addCell(
							getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				} else {
					budgetDetailsTable.addCell(
							getGeneralDetailsCell(budgetHeader.getTotalDirectCost().toString(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				}
				budgetDetailsTable.addCell(
						getGeneralDetailsCell("Indirect Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				if (budgetHeader.getTotalIndirectCost() == null) {
					budgetDetailsTable.addCell(
							getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				} else {
					budgetDetailsTable.addCell(
							getGeneralDetailsCell(budgetHeader.getTotalIndirectCost().toString(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				}
				budgetDetailsTable.addCell(
						getGeneralDetailsCell("Total Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				if (budgetHeader.getTotalCost() == null) {
					budgetDetailsTable.addCell(
							getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				} else {
					budgetDetailsTable
							.addCell(getGeneralDetailsCell(budgetHeader.getTotalCost().toString(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				}
				budgetDetailsTable.addCell(
						getGeneralDetailsCell("OH Base: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				budgetDetailsTable
						.addCell(getGeneralDetailsCell(budgetHeader.getRateType().getDescription(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
				document.add(budgetDetailsTable);

				document.add(new Chunk(ls));
				document.add(new Paragraph("Periods And Total", subHeadFont));
				document.add(new Chunk(ls));

				PdfPTable periodsAndTotalTable = new PdfPTable(5);
				periodsAndTotalTable.setWidthPercentage(100);
				periodsAndTotalTable.setWidths(new int[] { 3, 3, 2, 2, 2 });
				periodsAndTotalTable.setSpacingBefore(10f);
				periodsAndTotalTable.setSpacingAfter(5f);

				periodsAndTotalTable.addCell(
						getTableCell("Period Start Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				periodsAndTotalTable.addCell(
						getTableCell("Period End Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				periodsAndTotalTable
						.addCell(getTableCell("Total Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				periodsAndTotalTable
						.addCell(getTableCell("Direct Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				periodsAndTotalTable
						.addCell(getTableCell("Indirect Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

				for (BudgetPeriod budgetPeriod : budgetHeader.getBudgetPeriods()) {
					periodsAndTotalTable.addCell(getTableCell(dateTimeService.convertToSqlDate(budgetPeriod.getStartDate()).toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					periodsAndTotalTable.addCell(getTableCell(dateTimeService.convertToSqlDate(budgetPeriod.getEndDate()).toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					if (budgetPeriod.getTotalCost() == null) {
						periodsAndTotalTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						periodsAndTotalTable.addCell(getTableCell(budgetPeriod.getTotalCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					}
					if (budgetPeriod.getTotalDirectCost() == null) {
						periodsAndTotalTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						periodsAndTotalTable.addCell(getTableCell(budgetPeriod.getTotalDirectCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					}
					if (budgetPeriod.getTotalIndirectCost() == null) {
						periodsAndTotalTable
								.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					} else {
						periodsAndTotalTable.addCell(getTableCell(budgetPeriod.getTotalIndirectCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					}
				}
				periodsAndTotalTable.addCell(getTableCell("", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				periodsAndTotalTable
						.addCell(getTableCell("Total:", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				if (budgetHeader.getTotalCost() == null) {
					periodsAndTotalTable
							.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					periodsAndTotalTable.addCell(getTableCell(budgetHeader.getTotalCost().toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				if (budgetHeader.getTotalDirectCost() == null) {
					periodsAndTotalTable
							.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					periodsAndTotalTable
							.addCell(getTableCell(budgetHeader.getTotalDirectCost().toString(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				if (budgetHeader.getTotalIndirectCost() == null) {
					periodsAndTotalTable
							.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				} else {
					periodsAndTotalTable
							.addCell(getTableCell(budgetHeader.getTotalIndirectCost().toString(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
				}
				document.add(periodsAndTotalTable);

				document.add(new Chunk(ls));
				document.add(new Paragraph("Period details", subHeadFont));
				document.add(new Chunk(ls));

				for (BudgetPeriod budgetPeriod : budgetHeader.getBudgetPeriods()) {
					document.add(new Chunk(ls));
					document.add(new Paragraph("Period" + budgetPeriod.getBudgetPeriod(), subHeadFont));
					document.add(new Chunk(ls));
					PdfPTable periodDetailsTable = new PdfPTable(2);
					periodDetailsTable.setWidthPercentage(100);
					periodDetailsTable.setWidths(new int[] { 6, 6 });
					periodDetailsTable.setSpacingAfter(5f);

					periodDetailsTable.addCell(getGeneralDetailsCell("Period Start Date: ", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_LEFT, bodyFont));
					periodDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetPeriod.getStartDate()).toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					periodDetailsTable.addCell(getGeneralDetailsCell("Period End Date: ", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_LEFT, bodyFont));
					periodDetailsTable.addCell(getGeneralDetailsCell(dateTimeService.convertToSqlDate(budgetPeriod.getEndDate()).toString(),
							PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					periodDetailsTable.addCell(getGeneralDetailsCell("Direct Cost: ", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_LEFT, bodyFont));
					if (budgetPeriod.getTotalDirectCost() == null) {
						periodDetailsTable.addCell(
								getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					} else {
						periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriod.getTotalDirectCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					}
					periodDetailsTable.addCell(getGeneralDetailsCell("Indirect Cost: ", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_LEFT, bodyFont));
					if (budgetPeriod.getTotalIndirectCost() == null) {
						periodDetailsTable.addCell(
								getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					} else {
						periodDetailsTable
								.addCell(getGeneralDetailsCell(budgetPeriod.getTotalIndirectCost().toString(),
										PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					}
					periodDetailsTable.addCell(getGeneralDetailsCell("Total Cost: ", PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_LEFT, bodyFont));
					if (budgetPeriod.getTotalCost() == null) {
						periodDetailsTable.addCell(
								getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					} else {
						periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriod.getTotalCost().toString(),
								PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
					}
					document.add(periodDetailsTable);
					if (!budgetPeriod.getBudgetDetails().isEmpty()) {
						PdfPTable lineItemTable = new PdfPTable(4);
						lineItemTable.setWidthPercentage(100);
						lineItemTable.setWidths(new int[] { 3, 3, 3, 3 });
						lineItemTable.setSpacingBefore(10f);
						lineItemTable.setSpacingAfter(5f);

						lineItemTable.addCell(getTableCell("Budget Category", PdfPCell.ALIGN_MIDDLE,
								PdfPCell.ALIGN_CENTER, headFont));
						lineItemTable.addCell(
								getTableCell("Cost Element", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
						lineItemTable.addCell(getTableCell("Line Item Description", PdfPCell.ALIGN_MIDDLE,
								PdfPCell.ALIGN_CENTER, headFont));
						lineItemTable.addCell(
								getTableCell("Line Item Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

						for (BudgetDetail budgetDetails : budgetPeriod.getBudgetDetails()) {
							lineItemTable.addCell(getTableCell(budgetDetails.getBudgetCategory().getDescription(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							lineItemTable.addCell(getTableCell(budgetDetails.getCostElement().getDescription(),
									PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							if (budgetDetails.getLineItemDescription() == null) {
								lineItemTable.addCell(
										getTableCell("", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							} else {
								lineItemTable.addCell(getTableCell(budgetDetails.getLineItemDescription(),
										PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							}
							if (budgetDetails.getLineItemCost() == null) {
								lineItemTable.addCell(
										getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							} else {
								lineItemTable.addCell(getTableCell(budgetDetails.getLineItemCost().toString(),
										PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
							}
						}
						document.add(lineItemTable);
					}
				}
			}
			document.close();
		} catch (DocumentException ex) {
			Logger.getLogger(ProposalPrintServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public static PdfPCell getGeneralDetailsCell(String text, int verticalAlignment, int horizontalAlignmentt, Font bodyFont) {
		PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
		cell.setPadding(5);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setHorizontalAlignment(horizontalAlignmentt);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}

	public static PdfPCell getTableCell(String text, int verticalAlignment, int horizontalAlignmentt, Font bodyFont) {
		PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
		cell.setPadding(10);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setHorizontalAlignment(horizontalAlignmentt);
		return cell;
	}

}
