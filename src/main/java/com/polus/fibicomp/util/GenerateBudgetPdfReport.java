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
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.proposal.pojo.Proposal;

public class GenerateBudgetPdfReport {

	public static ByteArrayInputStream proposalPdfReport(Proposal budgetPdfData) throws DocumentException {

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
					.addCell(getGeneralDetailsCell("Proposal Number: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell(budgetPdfData.getTitle(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell("Principal Investigator: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getPrincipalInvestigator(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Lead Unit: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getHomeUnitName(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal Category: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getActivityType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal Type: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getProposalType().getDescription(),
					PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell("Proposal Start Date: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(
					getGeneralDetailsCell("Proposal End Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			proposalDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			document.add(proposalDetailsTable);

			document.add(new Chunk(ls));
			document.add(new Paragraph("Budget Overview", subHeadFont));
			document.add(new Chunk(ls));

			if(budgetPdfData.getBudgetHeader() != null) {
			PdfPTable budgetDetailsTable = new PdfPTable(2);
			budgetDetailsTable.setWidthPercentage(100);
			budgetDetailsTable.setWidths(new int[] { 6, 6 });
			budgetDetailsTable.setSpacingAfter(5f);

			budgetDetailsTable
					.addCell(getGeneralDetailsCell("Budget Start Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			budgetDetailsTable.addCell(
					getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getStartDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			budgetDetailsTable.addCell(getGeneralDetailsCell("Budget End Date: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			budgetDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getEndDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			budgetDetailsTable.addCell(
					getGeneralDetailsCell("Direct Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPdfData.getBudgetHeader().getTotalDirectCost() == null) {
				budgetDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				budgetDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getTotalDirectCost().toString(), PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			budgetDetailsTable.addCell(
					getGeneralDetailsCell("Indirect Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPdfData.getBudgetHeader().getTotalIndirectCost() == null) {
				budgetDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				budgetDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getTotalIndirectCost().toString(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			}
			budgetDetailsTable.addCell(
					getGeneralDetailsCell("Total Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPdfData.getBudgetHeader().getTotalCost() == null) {
				budgetDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				budgetDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getTotalCost().toString(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			}
			budgetDetailsTable.addCell(getGeneralDetailsCell("OH Base: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			budgetDetailsTable.addCell(getGeneralDetailsCell(budgetPdfData.getBudgetHeader().getRateType().getDescription(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
			document.add(budgetDetailsTable);

            document.add(new Chunk(ls));
            document.add(new Paragraph("Periods And Total", subHeadFont));
            document.add(new Chunk(ls));

        	PdfPTable periodsAndTotalTable = new PdfPTable(5);
        	periodsAndTotalTable.setWidthPercentage(100);
        	periodsAndTotalTable.setWidths(new int[]{3, 3, 2, 2, 2});
        	periodsAndTotalTable.setSpacingBefore(10f);
        	periodsAndTotalTable.setSpacingAfter(5f);

        	periodsAndTotalTable.addCell(getTableCell("Period Start Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	periodsAndTotalTable.addCell(getTableCell("Period End Date", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	periodsAndTotalTable.addCell(getTableCell("Total Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	periodsAndTotalTable.addCell(getTableCell("Direct Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
        	periodsAndTotalTable.addCell(getTableCell("Indirect Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));

            for (BudgetPeriod budgetPeriods : budgetPdfData.getBudgetHeader().getBudgetPeriods()) {
            	periodsAndTotalTable.addCell(getTableCell(budgetPeriods.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
            	periodsAndTotalTable.addCell(getTableCell(budgetPeriods.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	if(budgetPeriods.getTotalCost() == null) {
	            		periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
	            	else {
	            		periodsAndTotalTable.addCell(getTableCell(budgetPeriods.getTotalCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
	            	if(budgetPeriods.getTotalDirectCost() == null) {
	            		periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
	            	else {
	            		periodsAndTotalTable.addCell(getTableCell(budgetPeriods.getTotalDirectCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
	            	if(budgetPeriods.getTotalIndirectCost() == null) {
	            		periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
	            	else {
	            		periodsAndTotalTable.addCell(getTableCell(budgetPeriods.getTotalIndirectCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
	            	}
            	}
            periodsAndTotalTable.addCell(getTableCell("", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
            periodsAndTotalTable.addCell(getTableCell("Total:", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
            if(budgetPdfData.getBudgetHeader().getTotalCost() == null) {
            	periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
        	}
            else {
            	periodsAndTotalTable.addCell(getTableCell(budgetPdfData.getBudgetHeader().getTotalCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
            }
            if(budgetPdfData.getBudgetHeader().getTotalDirectCost() == null) {
            	periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
            }
            else {
            	periodsAndTotalTable.addCell(getTableCell(budgetPdfData.getBudgetHeader().getTotalDirectCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
            }
            if(budgetPdfData.getBudgetHeader().getTotalIndirectCost() == null) {
            	periodsAndTotalTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
            }
            else {
            	periodsAndTotalTable.addCell(getTableCell(budgetPdfData.getBudgetHeader().getTotalIndirectCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
            }
            document.add(periodsAndTotalTable);

            document.add(new Chunk(ls));
            document.add(new Paragraph("Period details", subHeadFont));
            document.add(new Chunk(ls));

            for (BudgetPeriod budgetPeriods : budgetPdfData.getBudgetHeader().getBudgetPeriods()) {
            document.add(new Chunk(ls));
            document.add(new Paragraph("Period"+ budgetPeriods.getBudgetPeriod(), subHeadFont));
            document.add(new Chunk(ls));
            PdfPTable periodDetailsTable = new PdfPTable(2);
            periodDetailsTable.setWidthPercentage(100);
            periodDetailsTable.setWidths(new int[] { 6, 6 });
            periodDetailsTable.setSpacingAfter(5f);

            periodDetailsTable
					.addCell(getGeneralDetailsCell("Period Start Date: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
            periodDetailsTable.addCell(
					getGeneralDetailsCell(budgetPeriods.getStartDate().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
            periodDetailsTable.addCell(getGeneralDetailsCell("Period End Date: ", PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
            periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriods.getEndDate().toString(), PdfPCell.ALIGN_MIDDLE,
					PdfPCell.ALIGN_LEFT, bodyFont));
            periodDetailsTable.addCell(
					getGeneralDetailsCell("Direct Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPeriods.getTotalDirectCost() == null) {
				periodDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriods.getTotalDirectCost().toString(), PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			periodDetailsTable.addCell(
					getGeneralDetailsCell("Indirect Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPeriods.getTotalIndirectCost() == null) {
				periodDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriods.getTotalIndirectCost().toString(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			}
			periodDetailsTable.addCell(
					getGeneralDetailsCell("Total Cost: ", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			if(budgetPeriods.getTotalCost() == null) {
				periodDetailsTable.addCell(getGeneralDetailsCell("$0.00", PdfPCell.ALIGN_MIDDLE,
						PdfPCell.ALIGN_LEFT, bodyFont));
			}
			else {
				periodDetailsTable.addCell(getGeneralDetailsCell(budgetPeriods.getTotalCost().toString(),
						PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_LEFT, bodyFont));
			}
			document.add(periodDetailsTable);
			if(!budgetPeriods.getBudgetDetails().isEmpty()) {
				PdfPTable lineItemTable = new PdfPTable(4);
	        	lineItemTable.setWidthPercentage(100);
	        	lineItemTable.setWidths(new int[]{3, 3, 3, 3});
	        	lineItemTable.setSpacingBefore(10f);
	        	lineItemTable.setSpacingAfter(5f);
	
	        	lineItemTable.addCell(getTableCell("Budget Category", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
	        	lineItemTable.addCell(getTableCell("Cost Element", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
	        	lineItemTable.addCell(getTableCell("Line Item Description", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
	        	lineItemTable.addCell(getTableCell("Line Item Cost", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, headFont));
	
				for (BudgetDetail budgetDetails : budgetPeriods.getBudgetDetails()) {
					lineItemTable.addCell(getTableCell(budgetDetails.getBudgetCategory().getDescription(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
					lineItemTable.addCell(getTableCell(budgetDetails.getCostElement().getDescription(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
			            	if(budgetDetails.getLineItemDescription() == null) {
			            		lineItemTable.addCell(getTableCell("", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
			            	}
			            	else {
			            		lineItemTable.addCell(getTableCell(budgetDetails.getLineItemDescription(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
			            	}
			            	if(budgetDetails.getLineItemCost() == null) {
			            		lineItemTable.addCell(getTableCell("$0.00", PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
			            	}
			            	else {
			            		lineItemTable.addCell(getTableCell(budgetDetails.getLineItemCost().toString(), PdfPCell.ALIGN_MIDDLE, PdfPCell.ALIGN_CENTER, bodyFont));
			            	}
		            	}
		            document.add(lineItemTable);
			}
          }
		 }
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
