package com.polus.fibicomp.workflow.vo;

import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;

public class WorkflowVO {

	private WorkflowAttachment newAttachment;

	private Integer workflowId;

	private String personId;

	public WorkflowAttachment getNewAttachment() {
		return newAttachment;
	}

	public void setNewAttachment(WorkflowAttachment newAttachment) {
		this.newAttachment = newAttachment;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}
