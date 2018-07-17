package com.polus.fibicomp.workflow.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

/**
 * This comparator sorts a list of WorkflowDetail by approvalStopNumber, and
 * approverNumber into ascending order.
 *
 */
public class WorkflowMapDetailComparator implements Comparator<WorkflowMapDetail> {

	@Override
	public int compare(WorkflowMapDetail wmd1, WorkflowMapDetail wmd2) {
		return new CompareToBuilder().append(wmd1.getApprovalStopNumber(), wmd2.getApprovalStopNumber())
				.append(wmd1.getApproverNumber(), wmd2.getApproverNumber()).toComparison();
	}

}
