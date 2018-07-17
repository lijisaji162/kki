package com.polus.fibicomp.workflow.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "FIBI_WORKFLOW")
public class Workflow implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "workflowIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "workflowIdGenerator")
	@Column(name = "WORKFLOW_ID")
	private Integer workflowId;

	@Column(name = "MODULE_CODE")
	private Integer moduleCode;

	@Column(name = "MODULE_ITEM_ID")
	private Integer moduleItemId;

	@Column(name = "IS_WORKFLOW_ACTIVE")
	@Convert(converter = JpaCharBooleanConversion.class)
	private Boolean isWorkflowActive;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimeStamp;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp UpdateTimeStamp;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "COMMENTS")
	private String comments;

	@JsonManagedReference
	@OneToMany(mappedBy = "workflow", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<WorkflowDetail> workflowDetails;

	@Column(name = "WORKFLOW_SEQUENCE")
	private Integer workflowSequence;

	@Column(name = "WORKFLOW_START_DATE")
	private Date workflowStartDate;

	@Column(name = "WORKFLOW_END_DATE")
	private Date workflowEndDate;

	@Column(name = "WORKFLOW_START_PERSON")
	private String workflowStartPerson;

	@Column(name = "WORKFLOW_END_PERSON")
	private String workflowEndPerson;

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public Integer getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(Integer moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Integer getModuleItemId() {
		return moduleItemId;
	}

	public void setModuleItemId(Integer moduleItemId) {
		this.moduleItemId = moduleItemId;
	}

	public Boolean getIsWorkflowActive() {
		return isWorkflowActive;
	}

	public void setIsWorkflowActive(Boolean isWorkflowActive) {
		this.isWorkflowActive = isWorkflowActive;
	}

	public Timestamp getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Timestamp createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Timestamp getUpdateTimeStamp() {
		return UpdateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		UpdateTimeStamp = updateTimeStamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<WorkflowDetail> getWorkflowDetails() {
		return workflowDetails;
	}

	public void setWorkflowDetails(List<WorkflowDetail> workflowDetails) {
		this.workflowDetails = workflowDetails;
	}

	public Integer getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(Integer workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public Date getWorkflowStartDate() {
		return workflowStartDate;
	}

	public void setWorkflowStartDate(Date workflowStartDate) {
		this.workflowStartDate = workflowStartDate;
	}

	public Date getWorkflowEndDate() {
		return workflowEndDate;
	}

	public void setWorkflowEndDate(Date workflowEndDate) {
		this.workflowEndDate = workflowEndDate;
	}

	public String getWorkflowStartPerson() {
		return workflowStartPerson;
	}

	public void setWorkflowStartPerson(String workflowStartPerson) {
		this.workflowStartPerson = workflowStartPerson;
	}

	public String getWorkflowEndPerson() {
		return workflowEndPerson;
	}

	public void setWorkflowEndPerson(String workflowEndPerson) {
		this.workflowEndPerson = workflowEndPerson;
	}
}
