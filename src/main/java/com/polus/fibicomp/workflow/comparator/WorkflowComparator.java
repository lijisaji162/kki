package com.polus.fibicomp.workflow.comparator;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.polus.fibicomp.workflow.pojo.Workflow;

/**
 * This comparator sorts a list of Workflow by WorkflowSequence
 * in ascending order.
 *
 */
public class WorkflowComparator implements Comparator<Workflow> {

	@Override
	public int compare(Workflow workflow1, Workflow workflow2) {
		return new CompareToBuilder().append(workflow1.getWorkflowSequence(), workflow2.getWorkflowSequence())
				.append(workflow1.getWorkflowSequence(), workflow2.getWorkflowSequence()).toComparison();
	}

}
