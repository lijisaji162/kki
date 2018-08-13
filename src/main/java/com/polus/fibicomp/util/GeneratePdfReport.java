package com.polus.fibicomp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;

public class GeneratePdfReport {

	public static ByteArrayInputStream proposalPdfReport(Proposal pdfData) throws DocumentException {

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
			generalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal Category: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getActivityType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal Type: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getProposalType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell("Proposal Start Date: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal End Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			generalDetailsTable.addCell(getGeneralDetailsCell(pdfData.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			document.add(generalDetailsTable);

			document.add(new Chunk(ls));
			document.add(new Paragraph("Project Personnel", subHeadFont));
			document.add(new Chunk(ls));

			PdfPTable personTable = new PdfPTable(4);
			personTable.setWidthPercentage(100);
			personTable.setWidths(new int[] { 3, 3, 3, 3 });
			personTable.setSpacingBefore(5f);
			personTable.setSpacingAfter(5f);

			personTable.addCell(getTableCell("Name", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("Role", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("Home Unit", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
			personTable.addCell(getTableCell("Department", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

			for (ProposalPerson persons : pdfData.getProposalPersons()) {
				personTable.addCell(
						getTableCell(persons.getFullName(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				personTable.addCell(getTableCell(persons.getProposalPersonRole().getDescription(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
				personTable.addCell(getTableCell(persons.getLeadUnitName(), PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_CENTER, bodyFont));
				personTable.addCell(
						getTableCell(persons.getDepartment(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
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

				for (ProposalSponsor sponsors : pdfData.getProposalSponsors()) {
					sponsorTable.addCell(getTableCell(sponsors.getSponsor().getSponsorName(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(sponsors.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(sponsors.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
					sponsorTable.addCell(getTableCell(sponsors.getAmount().toString(), PdfPCell.ALIGN_MIDDLE,
							PdfPCell.ALIGN_CENTER, bodyFont));
				}
				document.add(sponsorTable);
			}

            /*if(pdfData.getBudgetHeader() != null) {
            document.add(new Chunk(ls));
            document.add(new Paragraph("Budget Summary", subHeadFont));
            document.add(new Chunk(ls));
            document.add(new Paragraph("Periods And Totals", subHeadFont));

        	PdfPTable budgetTable = new PdfPTable(5);
        	budgetTable.setWidthPercentage(100);
        	budgetTable.setWidths(new int[]{3, 3, 2, 2, 2});
        	budgetTable.setSpacingBefore(10f);
        	budgetTable.setSpacingAfter(5f);

        	budgetTable.addCell(getTableCell("Period Start Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	budgetTable.addCell(getTableCell("Period End Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	budgetTable.addCell(getTableCell("Months", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	budgetTable.addCell(getTableCell("Total Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	budgetTable.addCell(getTableCell("Direct Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

            for (ProposalSponsor sponsors : pdfData.getProposalSponsors()) {
	            	budgetTable.addCell(getTableCell(sponsors.getSponsor().getSponsorName(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	budgetTable.addCell(getTableCell(sponsors.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	budgetTable.addCell(getTableCell(sponsors.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	budgetTable.addCell(getTableCell(sponsors.getAmount().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
            }
            document.add(budgetTable);
            }*/

			document.close();
		} catch (DocumentException ex) {
			Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public static PdfPCell getGeneralDetailsCell(String text, int verticalAlignment, int horizontalAlignmentt,
			Font bodyFont) {
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
