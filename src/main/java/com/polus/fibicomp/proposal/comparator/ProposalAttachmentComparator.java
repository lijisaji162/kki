package com.polus.fibicomp.proposal.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.polus.fibicomp.proposal.pojo.ProposalAttachment;

public class ProposalAttachmentComparator implements Comparator<ProposalAttachment> {

	@Override
	public int compare(ProposalAttachment pa1, ProposalAttachment pa2) {
		return new CompareToBuilder().append(pa1.getAttachmentTypeCode(), pa2.getAttachmentTypeCode()).toComparison();
	}

}
